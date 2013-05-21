package com.app.express.activity;

import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectFragment;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import com.app.express.R;
import com.app.express.db.DatabaseHelper;
import com.app.express.helper.App;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.server.erp.Erp;

@ContentView(R.layout.activity_next_delivery)
public class NextDelivery extends RoboActivity implements LocationListener {

	private LocationManager locationManager;

	private GoogleMap map;

	private Marker delivererMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the service.
		locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

		// Initialize Gmap.
		setUpMap();

		// If GPS is available, get it.
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			getProviderGPS();
		}

		delivererMarker = map.addMarker(new MarkerOptions().title("Vous êtes ici").position(new LatLng(0, 0)));

		// // Initialize helpers.
		// App.context = getApplicationContext();
		// App.dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		//
		// // Get the round xml file from the ERP. (Simulation)
		// StringBuffer rounds = Erp.getRoundsByUser(this);
		// if(rounds != null){
		// // Rounds are available. Display content.
		// Toast.makeText(this, rounds.toString(), Toast.LENGTH_LONG).show();
		// }
	}

	@Override
	public void onPause() {
		super.onPause();

		// Stop use the GPS.
		removeProviderGPS();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.next_delivery, menu);
		return true;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// If the provider disabled is the GPS, remove provider.
		if ("gps".equals(provider)) {
			removeProviderGPS();
		}

	}

	@Override
	public void onProviderEnabled(String provider) {
		if ("gps".equals(provider)) {
			getProviderGPS();
		}
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// Do nothing.
	}

	@Override
	public void onLocationChanged(Location location) {
		String msg = "lat: " + location.getLatitude() + "; lng : " + location.getLongitude();

		// Update the map location.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

		// Update the deliverer marker.
		delivererMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
		delivererMarker.setSnippet(msg);

		// Display for debug.
		Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show();
		Log.i(getClass().getSimpleName(), msg.toString());
	}

	/**
	 * If the map isn't already instantiated, do it.
	 */
	private void setUpMap() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (this.map == null) {
			this.map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		}
	}

	/**
	 * Get GPS provider.
	 */
	public void getProviderGPS() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
	}

	/**
	 * Remove GPS provider.
	 */
	public void removeProviderGPS() {
		locationManager.removeUpdates(this);
	}

}
