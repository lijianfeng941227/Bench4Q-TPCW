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
package org.bench4Q.agent.rbe.communication;

import java.util.ArrayList;

/**
 * @author duanzhiquan
 *
 */
public class Args implements Sendable {

	private static final long serialVersionUID = 5555852443194024065L;
	private String testName;
	private String testDescription;
	private String rbetype;
	private double interval;
	private int prepair;
	private int cooldown;
	private String out;
	private String mix;
	private double slow;
	private boolean getImage;
	private String baseURL;
	private double tolerance;
	private int retry;
	private double thinktime;
	private int urlConnectionTimeOut;
	private int urlReadTimeOut;
	private ArrayList<TestPhase> testPhase;

	/**
	 * constructor
	 */
	public Args() {
		testPhase = new ArrayList<TestPhase>();
		// initiate the argument
		rbetype = "closed";
		interval = 1;
		prepair = 600;
		cooldown = 300;
		out = "out";
		mix = "shopping";
		slow = 1.0;
		getImage = true;
		tolerance = 1.0;
		thinktime = 1.0;
		baseURL = "http://localhost:8080/jaspte";
		urlConnectionTimeOut = 0;
		urlReadTimeOut = 0;
	}

	/**
	 * @return rbe type
	 */
	public String getRbetype() {
		return rbetype;
	}

	/**
	 * @param rbetype
	 */
	public void setRbetype(String rbetype) {
		this.rbetype = rbetype;
	}

	/**
	 * @return prepair
	 */
	public int getPrepair() {
		return prepair;
	}

	/**
	 * @param prepair
	 */
	public void setPrepair(int prepair) {
		this.prepair = prepair;
	}

	/**
	 * @return cooldown
	 */
	public int getCooldown() {
		return cooldown;
	}

	/**
	 * @param cooldown
	 */
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	/**
	 * @return out name
	 */
	public String getOut() {
		return out;
	}

	/**
	 * @param out
	 */
	public void setOut(String out) {
		this.out = out;
	}

	/**
	 * @return mix
	 */
	public String getMix() {
		return mix;
	}

	/**
	 * @param mix
	 */
	public void setMix(String mix) {
		this.mix = mix;
	}

	/**
	 * @return slow rate
	 */
	public double getSlow() {
		return slow;
	}

	/**
	 * @param slow
	 */
	public void setSlow(double slow) {
		this.slow = slow;
	}

	/**
	 * @return base URL
	 */
	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * @param baseURL
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * @return tolerance time
	 */
	public double getTolerance() {
		return tolerance;
	}

	/**
	 * @param tolerance
	 */
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * @return think time
	 */
	public double getThinktime() {
		return thinktime;
	}

	/**
	 * @param thinktime
	 */
	public void setThinktime(double thinktime) {
		this.thinktime = thinktime;
	}

	/**
	 * @return whether get Image
	 */
	public boolean isGetImage() {
		return getImage;
	}

	/**
	 * @param getImage
	 */
	public void setGetImage(boolean getImage) {
		this.getImage = getImage;
	}

	/**
	 * @return retry time
	 */
	public int getRetry() {
		return retry;
	}

	/**
	 * @param retry
	 */
	public void setRetry(int retry) {
		this.retry = retry;
	}

	/**
	 * @return testPhase
	 */
	public ArrayList<TestPhase> getEbs() {
		return testPhase;
	}

	/**
	 * @param testPhase
	 */
	public void setEbs(ArrayList<TestPhase> testPhase) {
		this.testPhase = testPhase;
	}

	/**
	 * @param testPhase
	 */
	public void addEB(TestPhase testPhase) {
		this.testPhase.add(testPhase);
	}

	/**
	 * @param index
	 */
	public void deleteEB(int index) {
		this.testPhase.remove(index);
	}

	/**
	 * @return url Connection Time Out
	 */
	public int getUrlConnectionTimeOut() {
		return urlConnectionTimeOut;
	}

	/**
	 * @param urlConnectionTimeOut
	 */
	public void setUrlConnectionTimeOut(int urlConnectionTimeOut) {
		this.urlConnectionTimeOut = urlConnectionTimeOut;
	}

	/**
	 * @return url Read Time Out
	 */
	public int getUrlReadTimeOut() {
		return urlReadTimeOut;
	}

	/**
	 * @param urlReadTimeOut
	 */
	public void setUrlReadTimeOut(int urlReadTimeOut) {
		this.urlReadTimeOut = urlReadTimeOut;
	}

	/**
	 * @return interval
	 */
	public double getInterval() {
		return interval;
	}

	/**
	 * @param interval
	 */
	public void setInterval(double interval) {
		this.interval = interval;
	}

	/**
	 * @return test name
	 */
	public String getTestName() {
		return testName;
	}

	/**
	 * @param testName
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}

	/**
	 * @return test description
	 */
	public String getTestDescription() {
		return testDescription;
	}

	/**
	 * @param testDescription
	 */
	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}

}
