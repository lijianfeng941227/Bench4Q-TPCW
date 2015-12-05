package org.bench4Q.console.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.EventListener;
import java.util.List;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.common.util.ListenerSupport;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public final class ConfigModel {

	private File m_selectedFile;

	private final ListenerSupport m_listeners = new ListenerSupport();

	private static final String xml = "org/bench4Q/resources/bench4Q.xml";
	private static final String schema = "org/bench4Q/resources/bench4Q-schema.xsd";
	private SAXBuilder m_builder;
	private Document m_doc;
	private Args args;

	public ConfigModel() {
		try {
			m_builder = new SAXBuilder(false);
			m_builder.setFeature("http://apache.org/xml/features/validation/schema", true);
			m_builder.setProperty(
					"http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
					getClass().getClassLoader().getResource(schema).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		args = new Args();

		InputStream file = getClass().getClassLoader().getResourceAsStream(xml);
		try {
			m_doc = m_builder.build(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ConfigModel(File defaultFile) {
		this();
		m_selectedFile = defaultFile;
	}

	public boolean CheckFile(File file) {
		try {
			m_doc = m_builder.build(new FileReader(file));
		} catch (JDOMException e) {
			return false;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public void newEmptyFile(File file) {

		CheckFile(file);
		initFile(file);
	}

	public void initFile(File XMLFile) {

		Element root = new Element("bench4Q");
		m_doc.setRootElement(root);

		Element testName = new Element("testName").setText(args.getTestName());
		root.addContent(testName);
		Element testDescription = new Element("testDescription").setText(args.getTestDescription());
		root.addContent(testDescription);

		Element rbe = new Element("rbe");
		root.addContent(rbe);
		rbe.setAttribute(new Attribute("rbetype", "closed"));
		Element interval = new Element("interval").setText(String.valueOf(args.getInterval()));
		rbe.addContent(interval);
		Element prepair = new Element("prepair").setText(String.valueOf(args.getPrepair()));
		rbe.addContent(prepair);
		Element cooldown = new Element("cooldown").setText(String.valueOf(args.getCooldown()));
		rbe.addContent(cooldown);
		Element out = new Element("out").setText(args.getOut());
		rbe.addContent(out);
		Element tolerance = new Element("tolerance").setText(String.valueOf(args.getTolerance()));
		rbe.addContent(tolerance);
		Element retry = new Element("retry").setText(String.valueOf(args.getRetry()));
		rbe.addContent(retry);
		Element thinktime = new Element("thinktime").setText(String.valueOf(args.getThinktime()));
		rbe.addContent(thinktime);
		Element urlConnectionTimeOut = new Element("urlConnectionTimeOut").setText(String
				.valueOf(args.getUrlConnectionTimeOut()));
		rbe.addContent(urlConnectionTimeOut);
		Element urlReadTimeOut = new Element("urlReadTimeOut").setText(String.valueOf(args
				.getUrlReadTimeOut()));
		rbe.addContent(urlReadTimeOut);
		Element mix = new Element("mix").setText(args.getMix());
		rbe.addContent(mix);
		Element slow = new Element("slow").setText(String.valueOf(args.getSlow()));
		rbe.addContent(slow);
		Element getImage = new Element("getImage").setText(String.valueOf(args.isGetImage()));
		rbe.addContent(getImage);
		Element baseURL = new Element("baseURL").setText(args.getBaseURL());
		rbe.addContent(baseURL);
		if (args.getEbs().isEmpty()) {
			Element ebs = new Element("ebs");
			rbe.addContent(ebs);
			Element baseLoad = new Element("baseLoad").setText("0");
			ebs.addContent(baseLoad);
			Element randomLoad = new Element("randomLoad").setText("0");
			ebs.addContent(randomLoad);

			Element rate = new Element("rate").setText("0");
			ebs.addContent(rate);
			Element triggerTime = new Element("triggerTime").setText("0");
			ebs.addContent(triggerTime);
			Element stdyTime = new Element("stdyTime").setText("0");
			ebs.addContent(stdyTime);
		} else {
			for (TestPhase testPhase : args.getEbs()) {
				Element ebs = new Element("ebs");
				rbe.addContent(ebs);
				Element baseLoad = new Element("baseLoad").setText(String.valueOf(testPhase
						.getBaseLoad()));
				ebs.addContent(baseLoad);
				Element randomLoad = new Element("randomLoad").setText(String.valueOf(testPhase
						.getRandomLoad()));
				ebs.addContent(randomLoad);
				Element rate = new Element("rate").setText(String.valueOf(testPhase.getRate()));
				ebs.addContent(rate);
				Element triggerTime = new Element("triggerTime").setText(String.valueOf(testPhase
						.getTriggerTime()));
				ebs.addContent(triggerTime);
				Element stdyTime = new Element("stdyTime").setText(String.valueOf(testPhase
						.getStdyTime()));
				ebs.addContent(stdyTime);
			}
		}
		Format format = Format.getCompactFormat();
		format.setEncoding("UTF-8");
		format.setIndent("  ");

		XMLOutputter XMLOut = new XMLOutputter(format);
		try {
			XMLOut.output(m_doc, new FileOutputStream(XMLFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		resetConfigPanel();
	}

	public void SaveToFile() {
		SaveToFile(m_selectedFile);
	}

	public void SaveToFile(File XMLFile) {

		Element root = m_doc.getRootElement();
		root.removeChildren("testName");
		Element testName = new Element("testName").setText(args.getTestName());
		root.addContent(testName);
		root.removeChildren("testDescription");
		Element testDescription = new Element("testDescription").setText(args.getTestDescription());
		root.addContent(testDescription);

		root.removeChildren("rbe");
		Element rbe = new Element("rbe");
		root.addContent(rbe);
		rbe.setAttribute(new Attribute("rbetype", args.getRbetype()));
		Element interval = new Element("interval").setText(String.valueOf(args.getInterval()));
		rbe.addContent(interval);
		Element prepair = new Element("prepair").setText(String.valueOf(args.getPrepair()));
		rbe.addContent(prepair);
		Element cooldown = new Element("cooldown").setText(String.valueOf(args.getCooldown()));
		rbe.addContent(cooldown);
		Element out = new Element("out").setText(args.getOut());
		rbe.addContent(out);
		Element tolerance = new Element("tolerance").setText(String.valueOf(args.getTolerance()));
		rbe.addContent(tolerance);
		Element retry = new Element("retry").setText(String.valueOf(args.getRetry()));
		rbe.addContent(retry);
		Element thinktime = new Element("thinktime").setText(String.valueOf(args.getThinktime()));
		rbe.addContent(thinktime);
		Element urlConnectionTimeOut = new Element("urlConnectionTimeOut").setText(String
				.valueOf(args.getUrlConnectionTimeOut()));
		rbe.addContent(urlConnectionTimeOut);
		Element urlReadTimeOut = new Element("urlReadTimeOut").setText(String.valueOf(args
				.getUrlReadTimeOut()));
		rbe.addContent(urlReadTimeOut);
		Element mix = new Element("mix").setText(args.getMix());
		rbe.addContent(mix);
		Element slow = new Element("slow").setText(String.valueOf(args.getSlow()));
		rbe.addContent(slow);
		Element getImage = new Element("getImage").setText(String.valueOf(args.isGetImage()));
		rbe.addContent(getImage);
		Element baseURL = new Element("baseURL").setText(args.getBaseURL());
		rbe.addContent(baseURL);

		for (TestPhase testPhase : args.getEbs()) {
			Element ebs = new Element("ebs");
			rbe.addContent(ebs);
			Element baseLoad = new Element("baseLoad").setText(String.valueOf(testPhase
					.getBaseLoad()));
			ebs.addContent(baseLoad);
			Element randomLoad = new Element("randomLoad").setText(String.valueOf(testPhase
					.getRandomLoad()));
			ebs.addContent(randomLoad);
			Element rate = new Element("rate").setText(String.valueOf(testPhase.getRate()));
			ebs.addContent(rate);
			Element triggerTime = new Element("triggerTime").setText(String.valueOf(testPhase
					.getTriggerTime()));
			ebs.addContent(triggerTime);
			Element stdyTime = new Element("stdyTime").setText(String.valueOf(testPhase
					.getStdyTime()));
			ebs.addContent(stdyTime);

		}

		Format format = Format.getCompactFormat();
		format.setEncoding("UTF-8");
		format.setIndent("  ");

		XMLOutputter XMLOut = new XMLOutputter(format);
		try {
			FileOutputStream outstream = new FileOutputStream(XMLFile);
			XMLOut.output(m_doc, outstream);
			outstream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean initRBEWithXMLFile() {
		if ((m_selectedFile == null) || !CheckFile(m_selectedFile)) {
			return false;
		}
		Element root = m_doc.getRootElement();
		Element rbe = root.getChild("rbe");
		Args args = new Args();

		args.setTestName(root.getChildText("testName"));
		args.setTestDescription(root.getChildText("testDescription"));
		args.setRbetype(rbe.getAttribute("rbetype").getValue());
		args.setInterval(Double.parseDouble(rbe.getChildText("interval")));
		args.setCooldown(Integer.parseInt(rbe.getChildText("cooldown")));
		args.setPrepair(Integer.parseInt(rbe.getChildText("prepair")));
		args.setMix(rbe.getChildText("mix"));
		args.setOut(rbe.getChildText("out"));
		args.setTolerance(Double.parseDouble(rbe.getChildText("tolerance")));
		args.setRetry(Integer.parseInt(rbe.getChildText("retry")));
		args.setThinktime(Double.parseDouble(rbe.getChildText("thinktime")));
		args.setUrlConnectionTimeOut(Integer.parseInt(rbe.getChildText("urlConnectionTimeOut")));
		args.setUrlReadTimeOut(Integer.parseInt(rbe.getChildText("urlReadTimeOut")));
		if (rbe.getChildText("slow") != null)
			args.setSlow(Double.parseDouble(rbe.getChildText("slow")));
		if (rbe.getChildText("getImage") != null) {
			if (rbe.getChildText("getImage").equals("true")) {
				args.setGetImage(true);
			} else {
				args.setGetImage(false);
			}
		}
		args.setBaseURL(rbe.getChildText("baseURL"));

		List ebs = rbe.getChildren("ebs");
		for (int j = 0; j < ebs.size(); j++) {
			Element eb = (Element) ebs.get(j);
			TestPhase testPhase = new TestPhase();
			testPhase.setBaseLoad(Integer.parseInt(eb.getChildText("baseLoad")));
			testPhase.setRandomLoad(Integer.parseInt(eb.getChildText("randomLoad")));
			testPhase.setRate(Integer.parseInt(eb.getChildText("rate")));
			testPhase.setTriggerTime(Integer.parseInt(eb.getChildText("triggerTime")));
			testPhase.setStdyTime(Integer.parseInt(eb.getChildText("stdyTime")));
			args.getEbs().add(testPhase);
		}
		this.args = args;
		resetConfigPanel();
		return true;
	}

	/**
	 * Add a new listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	public void addListener(Listener listener) {
		m_listeners.add(listener);
	}

	public File getSelectedFile() {
		return m_selectedFile;
	}

	public void setSelectedFile(File file) {
		m_selectedFile = file;
	}

	public interface Listener extends EventListener {

		void isArgsChanged();

	}

	public abstract static class AbstractListener implements Listener {

		public void isArgsChanged() {
		}
	}

	public Args getArgs() {
		return args;
	}

	public void setArgs(Args args) {
		this.args = args;
	}
	
	private void resetConfigPanel(){
		m_listeners.apply(
			      new ListenerSupport.Informer() {
			        public void inform(Object listener) {
			          ((Listener)listener).isArgsChanged();
			        }
			      });
	}

}
