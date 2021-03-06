package org.bench4q.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerRegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = -1887034331494955755L;
	private static Logger LOGGER = LoggerFactory.getLogger(CustomerRegistrationServlet.class);

	/**
	 * 2009-3-6 author: duanzhiquan Technology Center for Software Engineering
	 * Institute of Software, Chinese Academy of Sciences Beijing 100190, China
	 * Email:duanzhiquan07@otcaix.iscas.ac.cn
	 * 
	 * 
	 */

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		UUID uuid = UUID.randomUUID();
		Date before = new Date(System.currentTimeMillis());

		String url;

		Util.determinePriorityLevel(req);

		PrintWriter out = res.getWriter();
		// Set the content type of this servlet's result.
		res.setContentType("text/html");

		String C_ID = req.getParameter("C_ID");
		String SHOPPING_ID = req.getParameter("SHOPPING_ID");

		// String username;
		// if (C_ID != null) {
		// int c_idnum = Integer.parseInt(C_ID);
		// username = Database.GetUserName(c_idnum);
		// } else {
		// username = "";
		// }

		out.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD W3 HTML//EN\">\n");
		out.print("<HTML>\n");
		out.print("<HEAD><TITLE>Customer Registration</TITLE></HEAD>\n");
		out.print("<BODY BGCOLOR=\"#ffffff\">\n");
		out.print("<H1 ALIGN=\"center\">Bench4Q</H1>\n");
		out.print("<H1 ALIGN=\"center\">A QoS oriented B2C benchmark for Internetware Middleware</H1>\n");
		out.print("</H1><H2 ALIGN=\"center\">Customer Registration Page</H2>\n");

		String sessionIdStrToAppend = Util.appendSessionId(req);

		// by xiaowei zhou, change "$sessionid$" to "jsessionid=", 2010.11.4
		out.print("<FORM ACTION=\"buy_request" + sessionIdStrToAppend + "\" METHOD=\"get\">");

		out.print("<BLOCKQUOTE><BLOCKQUOTE>\n");
		out.print("<HR><TABLE BORDER=\"0\"><TR>\n");
		out.print("<TD><INPUT CHECKED=\"CHECKED\" NAME=\"RETURNING_FLAG\" "
				+ "TYPE=\"radio\" VALUE=\"Y\">I am an existing customer");
		out.print("</TD></TR><TR><TD>\n");
		out.print("<INPUT NAME=\"RETURNING_FLAG\" TYPE=\"radio\" VALUE=\"N\">"
				+ "I am a first time customer</TD></TR></TABLE>\n");
		out.print("<HR><P><B>If you're an existing customer, enter your User " + "ID and Password:</B><BR><BR></P>\n");
		out.print("<TABLE><TR ALIGN=\"left\">\n");
		out.print("<TD>User ID: <INPUT NAME=\"UNAME\" SIZE=\"23\"></TD></TR>\n");
		out.print("<TR ALIGN=\"left\">\n");
		out.print("<TD>Password: <INPUT SIZE=\"14\" NAME=\"PASSWD\" " + "TYPE=\"password\"></TD></TR></TABLE> \n");
		out.print("<HR><P><B>If you re a first time customer, enter the " + "details below:</B><BR></P>\n");
		out.print("<TABLE><TR><TD>Enter your birth date (mm/dd/yyyy):</TD>\n");
		out.print("<TD> <INPUT NAME=\"BIRTHDATE\" SIZE=\"10\"></TD></TR>");
		out.print("<TR><TD>Enter your First Name:</TD>\n");
		out.print("<TD> <INPUT NAME=\"FNAME\" SIZE=\"15\"></TD></TR>\n");
		out.print("<TR><TD>Enter your Last Name:</TD>\n");
		out.print("<TD><INPUT NAME=\"LNAME\" SIZE=\"15\"></TD></TR>\n");
		out.print("<TR><TD>Enter your Address 1:</TD>\n");
		out.print("<TD><INPUT NAME=\"STREET1\" SIZE=\"40\"></TD></TR>\n");
		out.print("<TR><TD>Enter your Address 2:</TD>\n");
		out.print("<TD> <INPUT NAME=\"STREET2\" SIZE=\"40\"></TD></TR>\n");

		out.print("<TR><TD>Enter your City, State, Zip:</TD>\n");
		out.print("<TD><INPUT NAME=\"CITY\" SIZE=\"30\">" + "<INPUT NAME=\"STATE\"><INPUT NAME=\"ZIP\" SIZE=\"10\">\n");
		out.print("</TD></TR>");

		out.print("<TR><TD>Enter your Country:</TD>\n");
		out.print("<TD><INPUT NAME=\"COUNTRY\" SIZE=\"50\"></TD></TR>\n");
		out.print("<TR><TD>Enter your Phone:</TD>\n");
		out.print("<TD><INPUT NAME=\"PHONE\" SIZE=\"16\"></TD></TR>\n");
		out.print("<TR><TD>Enter your E-mail:</TD>\n");
		out.print("<TD> <INPUT NAME=\"EMAIL\" SIZE=\"50\"></TD></TR></TABLE>\n");

		out.print("<HR><TABLE><TR><TD COLSPAN=\"2\">Special Instructions:");
		out.print("<TEXTAREA COLS=\"65\" NAME=\"DATA\" ROWS=\"4\">"
				+ "</TEXTAREA></TD></TR></TABLE></BLOCKQUOTE></BLOCKQUOTE>" + "<CENTER>\n");
		out.print("<INPUT TYPE=\"IMAGE\" NAME=\"Enter Order\" " + "SRC=\"Images/submit_B.gif\">\n");
		if (SHOPPING_ID != null)
			out.print("<INPUT TYPE=HIDDEN NAME=\"SHOPPING_ID\" value = \"" + SHOPPING_ID + "\">\n");
		if (C_ID != null)
			out.print("<INPUT TYPE=HIDDEN NAME=\"C_ID\" value = \"" + C_ID + "\">\n");
		url = "search_request";
		if (SHOPPING_ID != null) {
			url = url + "?SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
		} else if (C_ID != null)
			url = url + "?C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeURL(url));
		out.print("\"><IMG SRC=\"" + Util.buildImageUrl(this.getServletContext(), uuid, this.getClass(), "search_B.gif")
				+ "\" ALT=\"Search Item\"></A>");

		url = "home";
		if (SHOPPING_ID != null) {
			url = url + "?SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
		} else if (C_ID != null)
			url = url + "?C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeURL(url));
		out.print("\"><IMG SRC=\"" + Util.buildImageUrl(this.getServletContext(), uuid, this.getClass(), "home_B.gif")
				+ "\" ALT=\"Home\"></A>");
		out.print("</CENTER></FORM>");
		out.print("</BODY></HTML>");
		out.close();

		Date after = new Date(System.currentTimeMillis());
		LOGGER.debug("CustomerRegistrationServlet - " + uuid.toString() + " - Total - "
				+ (after.getTime() - before.getTime()) + " ms");
	}

}
