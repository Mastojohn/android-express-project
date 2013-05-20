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
	public static boolean isGoogleMapsInstalled()
	{
		PackageManager packageManager = App.context.getPackageManager();
	    try
	    {
	        @SuppressWarnings("unused")
			ApplicationInfo info = packageManager.getApplicationInfo("com.google.android.apps.maps", 0 );
	        return true;
	    } 
	    catch(Exception e)
	    {
	        return false;
	    }
	}
	 
	public static OnClickListener getGoogleMapsListener()
	{
	    return new OnClickListener() 
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) 
	        {
	            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
	            App.context.startActivity(intent);
	 
	            //Finish the activity so they can't circumvent the check
	            ((Activity) App.context).finish();
	        }
	    };
	}
	
	public static void displayError(){
		Builder builder = new AlertDialog.Builder(App.context);
        builder.setMessage("Please install Google Maps");
        builder.setCancelable(false);
        builder.setPositiveButton("Install", getGoogleMapsListener());
        AlertDialog dialog = builder.create();
        dialog.show();
	}
}
