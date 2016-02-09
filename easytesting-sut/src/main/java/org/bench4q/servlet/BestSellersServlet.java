package org.bench4q.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BestSellersServlet extends HttpServlet {

	private static final long serialVersionUID = -9007706622743406361L;
	private static Logger LOGGER = LoggerFactory.getLogger(BestSellersServlet.class);

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		LOGGER.debug("Enter function: doGet");
		String url;
		PrintWriter out = res.getWriter();

		HttpSession session = req.getSession(false);

		// by xiaowei zhou, determine session-based differentiated service
		// priority level, 20101116
		String strSessionPriorityLevel = req.getParameter(Util.SESSION_PRIORITY_KEY);
		Integer igrSessionPri = null;
		if (strSessionPriorityLevel != null && !strSessionPriorityLevel.equals("")) {
			try {
				igrSessionPri = Integer.valueOf(strSessionPriorityLevel);
			} catch (NumberFormatException e) {
				// ignore, use default
			}
			if (igrSessionPri != null) {
				if (igrSessionPri < 1 || igrSessionPri > Util.PRIORITY_LEVELS) {
					igrSessionPri = Util.DEFAULT_PRIORITY;
				}
				if (session != null) {
					session.setAttribute(Util.DIFFSERV_SESSION_PRIORITY_KEY, igrSessionPri);
				}
			}
		}

		String subject = req.getParameter("subject");
		String C_ID = req.getParameter("C_ID");
		String SHOPPING_ID = req.getParameter("SHOPPING_ID");

		// Set the content type of this servlet's result.
		res.setContentType("text/html");
		out.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD W3 HTML//EN\">\n");
		out.print("<HTML><HEAD><TITLE> Best Sellers: " + subject + "</TITLE></HEAD>\n");
		out.print("<BODY BGCOLOR=\"#ffffff\">\n");
		out.print("<P ALIGN=\"center\">\n");

		out.print("<H2 ALIGN=\"center\">Best Sellers Page - Subject: " + subject + "</H2>\n");

		// Display promotions
		promotional_processing.DisplayPromotions(out, req, res, -1);

		// Display new products

		out.print("<TABLE BORDER=\"1\" CELLPADDING=\"1\" CELLSPACING=\"1\">\n");
		out.print("<TR> <TD WIDTH=\"30\"></TD>\n");
		out.print("<TD><FONT SIZE=\"+1\">Author</FONT></TD>\n");
		out.print("<TD><FONT SIZE=\"+1\">Title</FONT></TD></TR>\n");

		// Get best sellers from DB
		Vector<ShortBook> books = Database.getBestSellers(subject);

		// Print out the best sellers.
		int i;
		for (i = 0; i < books.size(); i++) {
			ShortBook book = (ShortBook) books.elementAt(i);
			out.print("<TR><TD>" + (i + 1) + "</TD>\n");
			out.print("<TD><I>" + book.a_fname + " " + book.a_lname + "</I></TD>\n");
			url = "product_detail?I_ID=" + String.valueOf(book.i_id);
			if (SHOPPING_ID != null)
				url = url + "&SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
			out.print("<TD><A HREF=\"" + res.encodeURL(url));
			out.print("\">" + book.i_title + "</A></TD></TR>\n");
		}

		out.print("</TABLE><P><CENTER>\n");

		url = "shopping_cart?ADD_FLAG=N";
		if (SHOPPING_ID != null)
			url = url + "&SHOPPING_ID=" + SHOPPING_ID;
		if (C_ID != null)
			url = url + "&C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeURL(url));
		out.print("\"><IMG SRC=\"Images/shopping_cart_B.gif\" " + "ALT=\"Shopping Cart\"></A>\n");
		url = "search_request";
		if (SHOPPING_ID != null) {
			url = url + "?SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
		} else if (C_ID != null)
			url = url + "?C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeURL(url));
		out.print("\"><IMG SRC=\"Images/search_B.gif\" " + "ALT=\"Search\"></A>\n");
		url = "home";
		if (SHOPPING_ID != null) {
			url = url + "?SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
		} else if (C_ID != null)
			url = url + "?C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeURL(url));
		out.print("\"><IMG SRC=\"Images/home_B.gif\" " + "ALT=\"Home\"></A></P></CENTER>\n");

		out.print("</BODY> </HTML>\n");
		out.close();
		LOGGER.debug("Exit function: doGet");
	}
}
