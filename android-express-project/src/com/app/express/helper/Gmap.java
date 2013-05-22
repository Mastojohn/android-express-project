package com.app.express.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class Gmap {

	/**
	 * Check if the package of Google Map is installed and at the last version.
	 * 
	 * @return boolean
	 */
	public static boolean isGoogleMapsInstalled() {
		PackageManager packageManager = App.context.getPackageManager();
		try {
			@SuppressWarnings("unused")
			ApplicationInfo info = packageManager.getApplicationInfo("com.google.android.apps.maps", 0);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get the Google Map listener who on click call the market for download the latest release of GMap.
	 * 
	 * @return OnClickListener
	 */
	public static OnClickListener getGoogleMapsListener() {
		return new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
				App.context.startActivity(intent);

				// Finish the activity so they can't circumvent the check
				((Activity) App.context).finish();
			}
		};
	}

	/**
	 * Error displayed if GMap is under version or not installed on the device. The dialog displayed provide a link for download the latest release of GMap.
	 */
	public static void displayError() {
		Builder builder = new AlertDialog.Builder(App.context);
		builder.setMessage("Please install Google Maps");
		builder.setCancelable(false);
		builder.setPositiveButton("Install", getGoogleMapsListener());
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
