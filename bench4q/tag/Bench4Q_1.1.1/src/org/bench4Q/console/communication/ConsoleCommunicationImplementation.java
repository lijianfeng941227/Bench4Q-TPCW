/**
 * =========================================================================
 * 					Bench4Q version 1.1.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there.  
 * 
 * Distributed according to the GNU Lesser General Public Licence. 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by   
 * the Free Software Foundation; either version 2.1 of the License, or any
 * later version.
 * 
 * SEE Copyright.txt FOR FULL COPYRIGHT INFORMATION.
 * 
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 *
 * This version is a based on the implementation of TPC-W from University of Wisconsin. 
 * This version used some source code of The Grinder.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *  * Initial developer(s): Zhiquan Duan.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */

package org.bench4Q.console.communication;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.bench4Q.common.communication.Acceptor;
import org.bench4Q.common.communication.Address;
import org.bench4Q.common.communication.CommunicationException;
import org.bench4Q.common.communication.ConnectionType;
import org.bench4Q.common.communication.FanOutServerSender;
import org.bench4Q.common.communication.Message;
import org.bench4Q.common.communication.MessageDispatchRegistry;
import org.bench4Q.common.communication.MessageDispatchSender;
import org.bench4Q.common.communication.ServerReceiver;
import org.bench4Q.common.util.thread.BooleanCondition;
import org.bench4Q.console.ConsoleProperties;
import org.bench4Q.console.common.DisplayMessageConsoleException;
import org.bench4Q.console.common.ErrorHandler;
import org.bench4Q.console.common.Resources;

/**
 * Handles communication for the console.
 */
public final class ConsoleCommunicationImplementation implements ConsoleCommunication {

	private final int m_idlePollDelay;
	private final Resources m_resources;
	private final ConsoleProperties m_properties;
	private final ErrorHandler m_errorHandler;

	private final MessageDispatchSender m_messageDispatcher = new MessageDispatchSender();

	private final BooleanCondition m_processing = new BooleanCondition();
	private final BooleanCondition m_shutdown = new BooleanCondition();

	private Acceptor m_acceptor = null;
	private ServerReceiver m_receiver = null;
	private FanOutServerSender m_sender = null;

	/**
	 * Constructor that uses a default idlePollDelay.
	 * 
	 * @param resources
	 *            Resources.
	 * @param properties
	 *            Console properties.
	 * @param errorHandler
	 *            Error handler.
	 * @throws DisplayMessageConsoleException
	 *             If properties are invalid.
	 */
	public ConsoleCommunicationImplementation(Resources resources, ConsoleProperties properties,
			ErrorHandler errorHandler) throws DisplayMessageConsoleException {
		this(resources, properties, errorHandler, 500);
	}

	/**
	 * Constructor.
	 * 
	 * @param resources
	 *            Resources.
	 * @param properties
	 *            Console properties.
	 * @param errorHandler
	 *            Error handler.
	 * @param idlePollDelay
	 *            Time in milliseconds that our ServerReceiver threads should
	 *            sleep for if there's no incoming messages.
	 * @throws DisplayMessageConsoleException
	 *             If properties are invalid.
	 */
	public ConsoleCommunicationImplementation(Resources resources, ConsoleProperties properties,
			ErrorHandler errorHandler, int idlePollDelay) throws DisplayMessageConsoleException {

		m_resources = resources;
		m_properties = properties;
		m_idlePollDelay = idlePollDelay;
		m_errorHandler = errorHandler;

		properties.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				final String property = event.getPropertyName();

				if (property.equals(ConsoleProperties.CONSOLE_HOST_PROPERTY)
						|| property.equals(ConsoleProperties.CONSOLE_PORT_PROPERTY)) {
					reset();
				}
			}
		});

		reset();
	}

	private void reset() {
		try {
			if (m_acceptor != null) {
				m_acceptor.shutdown();
			}
		} catch (CommunicationException e) {
			m_errorHandler.handleException(e);
			return;
		}

		if (m_sender != null) {
			m_sender.shutdown();
		}

		if (m_receiver != null) {
			m_receiver.shutdown();

			// Wait until we're deaf. This requires that some other thread
			// executes
			// processOneMessage(). We can't suck on m_receiver ourself as there
			// may
			// be valid pending messages queued up.

			m_processing.await(false);
		}

		if (m_shutdown.get()) {
			return;
		}

		try {
			m_acceptor = new Acceptor(m_properties.getConsoleHost(), m_properties.getConsolePort(),
					1);
		} catch (CommunicationException e) {
			m_errorHandler.handleException(new DisplayMessageConsoleException(m_resources,
					"localBindError.text", e));

			// Wake up any threads waiting in processOneMessage().
			m_processing.wakeUpAllWaiters();

			return;
		}

		final Thread acceptorProblemListener = new Thread("Acceptor problem listener") {
			public void run() {
				while (true) {
					final Exception exception = m_acceptor.getPendingException(true);

					if (exception == null) {
						// Acceptor is shutting down.
						break;
					}

					m_errorHandler.handleException(exception);
				}
			}
		};

		acceptorProblemListener.setDaemon(true);
		acceptorProblemListener.start();

		m_receiver = new ServerReceiver();

		try {
			m_receiver.receiveFrom(m_acceptor, new ConnectionType[] { ConnectionType.AGENT,
					ConnectionType.CONSOLE_CLIENT, ConnectionType.WORKER, }, 5, m_idlePollDelay);
		} catch (CommunicationException e) {
			throw new AssertionError(e);
		}

		try {
			m_sender = new FanOutServerSender(m_acceptor, ConnectionType.AGENT, 3);
		} catch (Acceptor.ShutdownException e) {
			// I am tempted to make this an assertion.
			// Currently, this condition can only happen if the accept() call
			// throws
			// an exception. I guess this might reasonably happen if a network
			// i/f
			// goes away immediately after we create the Acceptor. It's not easy
			// for
			// us to reset ourselves at this point (I certainly don't want to
			// recurse), so we notify the user. Users could get going again by
			// reseting new console address info, but most likely they'll just
			// restart
			// the console.
			m_processing.wakeUpAllWaiters();
			m_errorHandler.handleException(e);
			return;
		}

		m_processing.set(true);
	}

	/**
	 * Returns the message dispatch registry which callers can use to register
	 * new message handlers.
	 * 
	 * @return The registry.
	 */
	public MessageDispatchRegistry getMessageDispatchRegistry() {
		return m_messageDispatcher;
	}

	/**
	 * Shut down communication.
	 */
	public void shutdown() {
		m_shutdown.set(true);
		m_processing.set(false);
		reset();
	}

	/**
	 * Wait to receive a message, then process it.
	 * 
	 * @return <code>true</code> if we processed a message successfully;
	 *         <code>false</code> if we've been shut down.
	 * @see #shutdown()
	 */
	public boolean processOneMessage() {
		while (true) {
			if (m_shutdown.get()) {
				return false;
			}

			if (m_processing.await(true)) {
				try {
					final Message message = m_receiver.waitForMessage();

					if (message == null) {
						// Current receiver has been shut down.
						m_processing.set(false);
					} else {
						m_messageDispatcher.send(message);
						return true;
					}
				} catch (CommunicationException e) {
					// The receive or send failed. We only set m_processing to
					// false when
					// our receiver has been shut down.
					m_errorHandler.handleException(e);
				}
			}
		}
	}

	/**
	 * The number of connections that have been accepted and are still active.
	 * Used by the unit tests.
	 * 
	 * @return The number of accepted connections.
	 */
	public int getNumberOfConnections() {
		return m_acceptor == null ? 0 : m_acceptor.getNumberOfConnections();
	}

	/**
	 * Send the given message to the agent processes (which may pass it on to
	 * their workers).
	 * 
	 * <p>
	 * Any errors that occur will be handled with the error handler.
	 * </p>
	 * 
	 * @param message
	 *            The message to send.
	 */
	public void sendToAgents(Message message) {
		if (m_sender == null) {
			m_errorHandler.handleErrorMessage(m_resources.getString("sendError.text"));
		} else {
			try {
				m_sender.send(message);
			} catch (CommunicationException e) {
				m_errorHandler.handleException(new DisplayMessageConsoleException(m_resources,
						"sendError.text", e));
			}
		}
	}

	/**
	 * Send the given message to the given agent processes (which may pass it on
	 * to its workers).
	 * 
	 * <p>
	 * Any errors that occur will be handled with the error handler.
	 * </p>
	 * 
	 * @param address
	 *            The address to which the message should be sent.
	 * @param message
	 *            The message to send.
	 */
	public void sendToAddressedAgents(Address address, Message message) {
		if (m_sender == null) {
			m_errorHandler.handleErrorMessage(m_resources.getString("sendError.text"));
		} else {
			try {
				m_sender.send(address, message);
			} catch (CommunicationException e) {
				m_errorHandler.handleException(new DisplayMessageConsoleException(m_resources,
						"sendError.text", e));
			}
		}
	}
}
