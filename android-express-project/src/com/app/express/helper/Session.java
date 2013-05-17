package com.app.express.helper;

import java.util.Date;

public class Session {
	/**
	 * Email used by the user for the connection.
	 */
	public static String email = null;
	
	/**
	 * User connected ?
	 */
	public static boolean connected = false;
	
	/**
	 * Date when the user was connected.
	 */
	public static Date connected_at = null;
	
	/**
	 * Connect the user.
	 * 
	 * @param email
	 */
	public static void doConnect(String email){
		Session.email = email;
		Session.connected = true;
		Session.connected_at = new Date();
	}
}
