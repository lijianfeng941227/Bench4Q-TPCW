/**
 * =========================================================================
 * 					Bench4Q version 1.0.0
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
package org.bench4Q.agent.rbe;

import java.net.MalformedURLException;
import java.net.URL;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;

public class EB_EBOpen extends EB {

	private static final boolean DEBUG = false;

	public EB_EBOpen(Args args) {
		m_args = args;
		maxTrans = 1000;
		curState = 0;
		nextReq = null;
		html = null;
		prevHTML = null;
		cid = ID_UNKNOWN;
		sessionID = null;
		shopID = ID_UNKNOWN;
		fname = null;
		lname = null;
		toHome = false;

		tt_scale = this.m_args.getThinktime();
		tolerance_scale = this.m_args.getTolerance();
		retry = this.m_args.getRetry();

		www = this.m_args.getBaseURL();
		if (www.endsWith("/")) {
			homeURL = www + "home";
			shopCartURL = www + "shopping_cart";
			orderInqURL = www + "order_inquiry";
			orderDispURL = www + "order_display";
			searchReqURL = www + "search_request";
			searchResultURL = www + "execute_search";
			newProdURL = www + "new_products";
			bestSellURL = www + "best_sellers";
			prodDetURL = www + "product_detail";
			custRegURL = www + "customer_registrationt";
			buyReqURL = www + "buy_request";
			buyConfURL = www + "buy_confirm";
			adminReqURL = www + "admin_request";
			adminConfURL = www + "admin_response";
		} else {
			homeURL = www + "/home";
			shopCartURL = www + "/shopping_cart";
			orderInqURL = www + "/order_inquiry";
			orderDispURL = www + "/order_display";
			searchReqURL = www + "/search_request";
			searchResultURL = www + "/execute_search";
			newProdURL = www + "/new_products";
			bestSellURL = www + "/best_sellers";
			prodDetURL = www + "/product_detail";
			custRegURL = www + "/customer_registration";
			buyReqURL = www + "/buy_request";
			buyConfURL = www + "/buy_confirm";
			adminReqURL = www + "/admin_request";
			adminConfURL = www + "/admin_response";
		}

		String mixType = m_args.getMix();
		Mix mix = new Mix();
		Mix.initialize();
		if (mixType.equals("browsing")) {
			transProb = mix.getTransProb("browsing");
			trans = mix.getTrans("browsing");
		} else if (mixType.equals("shopping")) {
			transProb = mix.getTransProb("shopping");
			trans = mix.getTrans("shopping");
		} else if (mixType.equals("ordering")) {
			transProb = mix.getTransProb("ordering");
			trans = mix.getTrans("ordering");
		} else {
			System.out.println("EB initiate mix ERROR");
		}
	}

	public void run() {
		long wirt_t1;
		long wirt_t2;
		long tt = 0L; // Think Time.
		boolean sign = true;
		wirt_t1 = System.currentTimeMillis();
		while ((maxTrans == -1) || (maxTrans > 0)) {
			if (nextReq != null) {
				// Check if user session is finished.
				if (toHome) {
					// User session is complete. Start new user session.
					EBStats.getEBStats().addCompleteSession();
					return;
				}
				if (nextReq.equals("")) {
					EBStats.getEBStats().addCompleteSession();
					EBStats.getEBStats().addErrorSession(curState);
					return;
				}
				// Send HTTP request.
				URL httpReq;
				try {
					httpReq = new URL(nextReq);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					return;
				}
				// Receive HTML response page.
				wirt_t1 = System.currentTimeMillis();
				sign = getHTML(curState, httpReq);
				if (sign == false) {
					EBStats.getEBStats().addCompleteSession();
					EBStats.getEBStats().addErrorSession(curState);
					return;
				}
				wirt_t2 = System.currentTimeMillis();
				double tolerance = tolerance();
				if (((wirt_t2 - wirt_t1) > tolerance) && (tolerance != 0)) {
					return;
				}
				// Compute and store Web Interaction Response Time (WIRT)
				EBStats.getEBStats().interaction(curState, wirt_t1, wirt_t2, tt);
				if (DEBUG) {
					System.out.println("Post process: " + curTrans.toString());
				}
				curTrans.postProcess(this, html);
			} else {
				html = null;
				wirt_t2 = wirt_t1;
			}
			nextState();
			if (nextReq != null) {
				// Pick think time (TT), and compute absolute request time
				tt = thinkTime();
				wirt_t1 = wirt_t2 + tt;
				try {
					sleep(tt);
				} catch (InterruptedException inte) {
					System.out.println(" Caught an interrupted exception!");
					return;
				}
				if (maxTrans > 0)
					maxTrans--;
			} else {
				System.out.println("ERROR: nextReq == null");
			}
		}
	}
}
