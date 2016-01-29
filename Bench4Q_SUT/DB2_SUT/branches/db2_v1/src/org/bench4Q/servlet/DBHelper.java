/**
 * 
 * Create at:   2009-5-14
 * @version:    1.0 
 *
 * 
 * Copyright Technology Center for Software Engineering,Institute of Software, Chinese Academy of Sciences 
 * and distributed according to the GNU Lesser General Public Licence. 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by   
 * the Free Software Foundation; either version 2.1 of the License, or any
 * later version.
 * See Copyright.txt for full copyright information.
 *
 *
 *  http://otc.iscas.ac.cn/
 * Technology Center for Software Engineering
 * Institute of Software, Chinese Academy of Sciences
 * Beijing 100190, China
 *
 * This version is a based on the implementation from University of Wisconsin
 *
 *
 *  * Initial developer(s): Zhiquan Duan, Wei Wang.
 * 
 */
package org.bench4Q.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class DBHelper {
	private static Properties prop = new Properties(); 
    private static DBHelper instance = new DBHelper(); 
     
    private DBHelper(){        
        InputStream in = this.getClass().getResourceAsStream("database.properties"); 
        if ( in != null ) {
        	try { 
                prop.load(in); 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
		} else {
			System.out.println("Can't find database.properties");

		}
        
    } 

    public  static DBHelper getInstance() { 
        return instance; 
    } 

    public String getProperty(String key){ 
        return (String) prop.get(key); 
    }    
}
