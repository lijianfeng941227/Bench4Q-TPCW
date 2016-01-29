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
package org.bench4Q.servlet;

import java.util.Date;

// Glorified struct to pass order information from the DB to servlets

public class Order {
//	public Order(ResultSet rs) {
//		try {
//			o_id = rs.getInt("o_id");
//			c_fname = rs.getString("c_fname");
//			c_lname = rs.getString("c_lname");
//			c_passwd = rs.getString("c_passwd");
//			c_uname = rs.getString("c_uname");
//			c_phone = rs.getString("c_phone");
//			c_email = rs.getString("c_email");
//			o_date = rs.getDate("o_date");
//			o_subtotal = rs.getDouble("o_sub_total");
//			o_tax = rs.getDouble("o_tax");
//			o_total = rs.getDouble("o_total");
//			o_ship_type = rs.getString("o_ship_type");
//			o_ship_date = rs.getDate("o_ship_date");
//			o_status = rs.getString("o_status");
//			cx_type = rs.getString("cx_type");
//
//			bill_addr_street1 = rs.getString("bill_addr_street1");
//			bill_addr_street2 = rs.getString("bill_addr_street2");
//			bill_addr_state = rs.getString("bill_addr_state");
//			bill_addr_zip = rs.getString("bill_addr_zip");
//			bill_co_name = rs.getString("bill_co_name");
//
//			ship_addr_street1 = rs.getString("ship_addr_street1");
//			ship_addr_street2 = rs.getString("ship_addr_street2");
//			ship_addr_state = rs.getString("ship_addr_state");
//			ship_addr_zip = rs.getString("ship_addr_zip");
//			ship_co_name = rs.getString("ship_co_name");
//		} catch (java.lang.Exception ex) {
//			ex.printStackTrace();
//		}
//	}
	
	public Order(org.bench4Q.hibernate.Orders ordersHib,
			org.bench4Q.hibernate.Customer customerHib, String cxType,
			org.bench4Q.hibernate.Address shipHib,
			org.bench4Q.hibernate.Country ship_coHib,
			org.bench4Q.hibernate.Address billHib,
			org.bench4Q.hibernate.Country bill_coHib) {
		o_id = ordersHib.getOId();
		c_fname = customerHib.getCFname();
		c_lname = customerHib.getCLname();
		c_passwd = customerHib.getCPasswd();
		c_uname = customerHib.getCUname();
		c_phone = customerHib.getCPhone();
		c_email = customerHib.getCEmail();
		o_date = ordersHib.getODate();
		o_subtotal = ordersHib.getOSubTotal();
		o_tax = ordersHib.getOTax();
		o_total = ordersHib.getOTotal();
		o_ship_type = ordersHib.getOShipType();
		o_ship_date = ordersHib.getOShipDate();
		o_status = ordersHib.getOStatus();
		cx_type = cxType;

		bill_addr_street1 = billHib.getAddrStreet1();
		bill_addr_street2 = billHib.getAddrStreet2();
		bill_addr_state = billHib.getAddrState();
		bill_addr_zip = billHib.getAddrZip();
		bill_co_name = bill_coHib.getCoName();

		ship_addr_street1 = shipHib.getAddrStreet1();
		ship_addr_street2 = shipHib.getAddrStreet2();
		ship_addr_state = shipHib.getAddrState();
		ship_addr_zip = shipHib.getAddrZip();
		ship_co_name = ship_coHib.getCoName();
	}

	public int o_id;
	public String c_fname;
	public String c_lname;
	public String c_passwd;
	public String c_uname;
	public String c_phone;
	public String c_email;
	public Date o_date;
	public double o_subtotal;
	public double o_tax;
	public double o_total;
	public String o_ship_type;
	public Date o_ship_date;
	public String o_status;

	// Billing address
	public String bill_addr_street1;
	public String bill_addr_street2;
	public String bill_addr_state;
	public String bill_addr_zip;
	public String bill_co_name;

	// Shipping address
	public String ship_addr_street1;
	public String ship_addr_street2;
	public String ship_addr_state;
	public String ship_addr_zip;
	public String ship_co_name;

	public String cx_type;
}
