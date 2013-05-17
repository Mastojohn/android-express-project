package com.server.erp;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.widget.Toast;

public class Erp {
	/**
	 * Users who are authorized to access to the app.
	 */
	private static final String[] USERS_AUTH = new String[] {
			"livreur@gmail.com:livreur", "ambroise@gmail.com:ambroise",
			"leo@gmail.com:leo", "julien@gmail.com:julien" };

	/**
	 * Check if the couple email/password is correct for access to the app.
	 * [Simulation]
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
	 * Get the rounds of the day for the connected user.
	 * [Simulation]
	 * @param context
	 * @return InputStream who contains the readable XML file.
	 */
	public static InputStream getRoundsByUser(Context context) {
		try {
			return context.getAssets().open("file:///assets/erp.xml");
		} catch (IOException e) {
			Toast.makeText(context, "Impossible de récupérer le fichier de configuration de votre tournée.", Toast.LENGTH_LONG).show();
			return null;
		}
	}
}
