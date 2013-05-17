package com.server.erp;

import java.io.BufferedInputStream;
import java.io.File;

import android.app.Activity;
import android.content.Context;

import com.app.express.R;


public class Erp{
	private static final String[] USERS_AUTH = new String[] {
			"livreur@gmail.com:livreur", "ambroise@gmail.com:ambroise",
			"leo@gmail.com:leo", "julien@gmail.com:julien" };

	public static boolean checkLogin(String email, String password) {
		for (String credential : USERS_AUTH) {
			String[] pieces = credential.split(":");
			if (pieces[0].equals(email)) {
				// Account exists, return true if the password matches.
				return pieces[1].equals(password);
			}
		}

		// No account matchs, return false.
		return false;
	}
	
	public static File getXml(Context context){
		return new File("/assets/erp.xml");
	}
}
