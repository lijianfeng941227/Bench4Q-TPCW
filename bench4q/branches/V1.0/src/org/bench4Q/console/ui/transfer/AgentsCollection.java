package org.bench4Q.console.ui.transfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.common.processidentity.AgentProcessReport;
import org.bench4Q.common.processidentity.AgentResultReport;
import org.bench4Q.common.util.ListenerSupport;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.ui.SwingDispatcherFactory;

public class AgentsCollection implements AgentsCollectionSubject {

	private Map agents;
	private ArrayList observers;
	private final ListenerSupport m_listeners = new ListenerSupport();

	private final Resources m_resources;
	private final ProcessControl m_processControl;

	public AgentsCollection(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory swingDispatcherFactory) {
		m_resources = resources;
		m_processControl = processControl;

		agents = Collections.synchronizedMap(new HashMap());
		observers = new ArrayList();

		m_processControl.addProcessStatusListener((ProcessControl.Listener) swingDispatcherFactory
				.create(new ProcessControl.Listener() {
					public void update(ProcessControl.ProcessReports[] processReports) {
						for (int i = 0; i < processReports.length; ++i) {
							final AgentProcessReport agentProcessStatus = processReports[i]
									.getAgentProcessReport();
							if (!agents.containsKey(agentProcessStatus.getAgentIdentity())) {
								try {
									AddAgent(agentProcessStatus.getAgentIdentity());
								} catch (ConsoleException e) {
									e.printStackTrace();
								}
							}
						}

					}
				}));

		m_processControl
				.addProcessResultListener((ProcessControl.ResultListener) swingDispatcherFactory
						.create(new ProcessControl.ResultListener() {
							public void update(ProcessControl.ResultReports[] resultReports) {
								final List rows = new ArrayList();
								long WIPS = 0;
								long WIRT = 0;
								long workerProcesses = 0;

								for (int i = 0; i < resultReports.length; ++i) {
									final AgentResultReport agentResultReport = resultReports[i]
											.getAgentResultReport();
									if (agents.containsKey(agentResultReport.getAgentIdentity())) {
										((AgentInfo) agents.get(agentResultReport.getAgentIdentity())).setStats(agentResultReport.getEBStats());
										notifyObserverResult((AgentInfo) agents.get(agentResultReport.getAgentIdentity()));
									}
								}
							}
						}));
	}

	public void AddAgent(AgentIdentity agentIdentity) throws ConsoleException {
		final AgentInfo created = new AgentInfo(agentIdentity);
		agents.put(agentIdentity, created);
		notifyObserverAdd(created);
	}

	public void DelAgent(AgentIdentity agentIdentity) {
		notifyObserverDel((AgentInfo) agents.get(agentIdentity));
		agents.remove(agentIdentity);
	}

	public void notifyObserverAdd(AgentInfo agentInfo) throws ConsoleException {
		for (int i = 0; i < observers.size(); i++) {
			AgentInfoObserver observer = (AgentInfoObserver) observers.get(i);
			observer.addAgent(agentInfo);
		}
	}

	public void notifyObserverDel(AgentInfo agentInfo) {
		for (int i = 0; i < observers.size(); i++) {
			AgentInfoObserver observer = (AgentInfoObserver) observers.get(i);
			observer.removeAgent(agentInfo);
		}
	}

	public void notifyObserverResult(AgentInfo agentInfo) {
		for (int i = 0; i < observers.size(); i++) {
			AgentInfoObserver observer = (AgentInfoObserver) observers.get(i);
			observer.getResult(agentInfo);
		}
	}

	public void registerObserver(AgentInfoObserver o) {
		observers.add(o);

	}

	public void removeObserver(AgentInfoObserver o) {
		int i = observers.indexOf(o);
		if (i >= 0) {
			observers.remove(i);
		}
	}

	public void resetAllResult() {
		// when a new test start, results need to reset.

	}

	public int getAgentNumber() {
		return agents.size();
	}

}
