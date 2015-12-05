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
package org.bench4Q.console.ui.section;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.bench4Q.console.common.Resources;
import org.bench4Q.console.model.ConfigModel;

/**
 * @author duanzhiquan
 *
 */
public class ConfigURLConSection extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8914016937226620685L;

	private final Resources m_resources;

	private ConfigModel m_configModel;

	private JLabel urlConnectionTimeOutLabel;
	private JLabel urlReadTimeOutLabel;
	private JLabel urlConnectionTimeOutExplain;
	private JLabel urlReadTimeOutExplain;
	private JTextField urlConnectionTimeOut;
	private JTextField urlReadTimeOut;

	/**
	 * @param resources
	 * @param configModel
	 */
	public ConfigURLConSection(Resources resources, ConfigModel configModel) {

		m_resources = resources;
		m_configModel = configModel;

		this.setLayout(new GridBagLayout());
		this.setBorder(new TitledBorder(m_resources.getString("ConfigURLConSection.section_name")));

		urlConnectionTimeOutLabel = new JLabel(m_resources
				.getString("ConfigURLConSection.urlConnectionTimeOut"), SwingConstants.RIGHT);
		urlReadTimeOutLabel = new JLabel(m_resources
				.getString("ConfigURLConSection.urlReadTimeOut"), SwingConstants.RIGHT);
		urlConnectionTimeOutExplain = new JLabel(m_resources
				.getString("ConfigURLConSection.urlConnectionTimeOutExplain"), SwingConstants.LEFT);
		urlReadTimeOutExplain = new JLabel(m_resources
				.getString("ConfigURLConSection.urlReadTimeOutExplain"), SwingConstants.LEFT);

		urlConnectionTimeOut = new JTextField(String.valueOf(m_configModel.getArgs()
				.getUrlConnectionTimeOut()), 10);
		urlConnectionTimeOut.getDocument().addDocumentListener(new ConfigueListener());

		urlReadTimeOut = new JTextField(
				String.valueOf(m_configModel.getArgs().getUrlReadTimeOut()), 10);
		urlReadTimeOut.getDocument().addDocumentListener(new ConfigueListener());

		this.add(urlConnectionTimeOutLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));
		this.add(urlConnectionTimeOut, new GridBagConstraints(1, 0, 1, 1, 90.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));
		this.add(new JLabel(), new GridBagConstraints(2, 0, 2, 1, 2.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));
		this.add(urlConnectionTimeOutExplain, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));

		this.add(urlReadTimeOutLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));
		this.add(urlReadTimeOut, new GridBagConstraints(1, 2, 1, 1, 90.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));
		this.add(urlReadTimeOutExplain, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));

		m_configModel.addListener(new ConfigModel.AbstractListener() {
			public void isArgsChanged() {
				resetConfig();
			}
		});
	}

	protected void resetConfig() {
		urlConnectionTimeOut.setText(String.valueOf(m_configModel.getArgs()
				.getUrlConnectionTimeOut()));
		urlReadTimeOut.setText(String.valueOf(m_configModel.getArgs().getUrlReadTimeOut()));
	}

	/**
	 * 
	 */
	public void setConfigue() {

		int urlCon = 0;
		try {
			if ((urlConnectionTimeOut.getText().trim() != null)
					&& (!urlConnectionTimeOut.getText().trim().equals(""))) {
				urlCon = Integer.parseInt(urlConnectionTimeOut.getText().trim());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		m_configModel.getArgs().setUrlConnectionTimeOut(urlCon);

		int urlRead = 0;
		try {
			if ((urlReadTimeOut.getText().trim() != null)
					&& (!urlReadTimeOut.getText().trim().equals(""))) {
				urlRead = Integer.parseInt(urlReadTimeOut.getText().trim());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		m_configModel.getArgs().setUrlReadTimeOut(urlRead);

	}

	private class ConfigueListener implements DocumentListener {
		public void insertUpdate(DocumentEvent event) {
			setConfigue();
		}

		public void removeUpdate(DocumentEvent event) {
			setConfigue();
		}

		public void changedUpdate(DocumentEvent event) {
			setConfigue();
		}
	}
}
