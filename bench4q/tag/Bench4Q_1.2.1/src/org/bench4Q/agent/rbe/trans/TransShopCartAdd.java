/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
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
package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.util.CharSetStrPattern;
import org.bench4Q.agent.rbe.util.StrStrPattern;
import org.bench4Q.agent.rbe.util.URLUtil;

/**
 * @author duanzhiquan
 *
 */
public class TransShopCartAdd extends TransShopCart {
	/* protected String url; inherited. */

	private static final StrStrPattern iid = new StrStrPattern("I_ID=");

	public String request(EB eb, String html) {
		int i, e, id;

		/* Find the I_ID to add. */
		i = iid.find(html);
		if (i == -1) {
			EBStats.getEBStats().error(14, "Unable to find I_ID in product detail page.", "???");
			return ("");
		}
		i = i + iid.length();

		e = CharSetStrPattern.notDigit.find(html.substring(i));
		if (e == -1) {
			EBStats.getEBStats().error(14, "Unable to find I_ID in product detail page.", "???");
			return ("");
		}
		e = e + i;
		id = Integer.parseInt(html.substring(i, e));

		url = EB.shopCartURL + "?" + URLUtil.field_addflag + "=Y&" + URLUtil.field_iid + "=" + id;

		return (eb.addIDs(url));
	}

	/* Find C_ID and SHOPPING_ID, if not already known. */
	/*
	 * public void postProcess(EB eb, String html) inherited from
	 * EBWShopCartTrans
	 */
}
