package com.server.erp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.widget.Toast;

public class Erp {
	/**
	 * Users who are authorized to access to the app.
	 */
	private static final String[] USERS_AUTH = new String[] {
			"eric.lambert@gmail.com:eric", "ambroise@gmail.com:amb",
			"leo@gmail.com:leo", "julien@gmail.com:julien" };

	/**
	 * Check if the couple email/password is correct for access to the app.
	 * [Simulation]
	 * 
	 * @param email
	 * @param password
	 * @return boolean
	 */
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

	/**
	 * Get the rounds of the day for the connected user. [Simulation]
	 * 
	 * @param context
	 * @return InputStream who contains the readable XML file.
	 */
	public static StringBuffer getRoundsByUser(Context context) {
		try {
			InputStream input = context.getAssets().open("erp.xml");

			StringBuffer content = new StringBuffer();
			int value;

			while ((value = input.read()) != -1) {
				// Write into the content the content read.
				content.append((char) value);
			}

			return content;
		} catch (FileNotFoundException e) {
			Toast.makeText(context, "Impossible de récupérer le fichier de configuration de votre tournée.\n" + e.getMessage(), Toast.LENGTH_LONG).show();
			return null;
		} catch (IOException e) {
			Toast.makeText(context, "Impossible d'ouvrir le fichier de configuration de votre tournée.\n" + e.getMessage(), Toast.LENGTH_LONG).show();
			return null;
		}
	}
}
