package com.app.express.activity;

import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectFragment;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
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
import com.app.express.helper.Gmap;
import com.app.express.task.RoundTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.server.erp.Erp;

@ContentView(R.layout.activity_next_delivery)
public class NextDelivery extends RoboActivity {

	private FollowMeLocationSource followMeLocationSource;
	
	//@InjectFragment(R.id.map)
	private GoogleMap map;

	private Marker delivererMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize helpers.
		App.context = getApplicationContext();
		App.dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

		// Creates our custom LocationSource and initializes some of its members.
		followMeLocationSource = new FollowMeLocationSource();

		// Initialize map.
		setUpMapIfNeeded();
		
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
	public void onResume() {
		super.onResume();

		/*
		 * We query for the best Location Provider everytime this fragment is displayed just in case a better provider might have become available since we last
		 * displayed it
		 */
		followMeLocationSource.getBestAvailableProvider();

		// Get a reference to the map/GoogleMap object.
		setUpMapIfNeeded();

		/*
		 * Enable the my-location layer (this causes our LocationSource to be automatically activated.) While enabled, the my-location layer continuously draws
		 * an indication of a user's current location and bearing, and displays UI controls that allow a user to interact with their location (for example, to
		 * enable or disable camera tracking of their location and bearing).
		 */
		map.setMyLocationEnabled(true);
	}

	@Override
	public void onPause() {
		/* Disable the my-location layer (this causes our LocationSource to be automatically deactivated.) */
		map.setMyLocationEnabled(false);

		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.next_delivery, menu);
		return true;
	}

	/**
	 * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly installed) and the map has not already been instantiated.
	 * This will ensure that we only ever manipulate the map once when it {@link #map} is not null.
	 * <p>
	 * If it isn't installed {@link SupportMapFragment} (and {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
	 * install/update the Google Play services APK on their device.
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (map != null) {
				// The Map is verified. It is now safe to manipulate the map:

				// Replace the (default) location source of the my-location layer with our custom LocationSource
				map.setLocationSource(followMeLocationSource);

				// Set default zoom
				map.moveCamera(CameraUpdateFactory.zoomTo(15f));
			}
		}
	}

	/*
	 * Our custom LocationSource. We register this class to receive location updates from the Location Manager and for that reason we need to also implement the
	 * LocationListener interface.
	 */
	private class FollowMeLocationSource implements LocationSource, LocationListener {

		private OnLocationChangedListener mListener;
		private LocationManager locationManager;
		private final Criteria criteria = new Criteria();
		private String bestAvailableProvider;
		/*
		 * Updates are restricted to one every 10 seconds, and only when movement of more than 10 meters has been detected.
		 */
		private final int minTime = 50000; // minimum time interval between location updates, in milliseconds
		private final int minDistance = 10; // minimum distance between location updates, in meters

		private FollowMeLocationSource() {
			// Get reference to Location Manager
			locationManager = (LocationManager) App.context.getSystemService(Context.LOCATION_SERVICE);

			// Specify Location Provider criteria
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			criteria.setAltitudeRequired(true);
			criteria.setBearingRequired(true);
			criteria.setSpeedRequired(true);
			criteria.setCostAllowed(true);
		}

		private void getBestAvailableProvider() {
			/*
			 * The preffered way of specifying the location provider (e.g. GPS, NETWORK) to use is to ask the Location Manager for the one that best satisfies
			 * our criteria. By passing the 'true' boolean we ask for the best available (enabled) provider.
			 */
			bestAvailableProvider = locationManager.getBestProvider(criteria, true);
		}

		/*
		 * Activates this provider. This provider will notify the supplied listener periodically, until you call deactivate(). This method is automatically
		 * invoked by enabling my-location layer.
		 */
		@Override
		public void activate(OnLocationChangedListener listener) {
			// We need to keep a reference to my-location layer's listener so we can push forward
			// location updates to it when we receive them from Location Manager.
			mListener = listener;

			// Request location updates from Location Manager
			if (bestAvailableProvider != null) {
				locationManager.requestLocationUpdates(bestAvailableProvider, minTime, minDistance, this);
			} else {
				Toast.makeText(App.context, "Impossible d'accéder à vos coordonnées GPS. (Wifi, 3g, etc.)", Toast.LENGTH_LONG).show();
			}
		}

		/*
		 * Deactivates this provider. This method is automatically invoked by disabling my-location layer.
		 */
		@Override
		public void deactivate() {
			// Remove location updates from Location Manager
			locationManager.removeUpdates(this);

			mListener = null;
		}

		@Override
		public void onLocationChanged(Location location) {
			/*
			 * Push location updates to the registered listener.. (this ensures that my-location layer will set the blue dot at the new/received location)
			 */
			if (mListener != null) {
				mListener.onLocationChanged(location);
			}

			/*
			 * ..and Animate camera to center on that location ! (the reason for we created this custom Location Source !)
			 */
			map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
			
			// Display the round on the map.
			new RoundTask(((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap(), location).execute();

			String msg = "lat: " + location.getLatitude() + "; lng : " + location.getLongitude();

			// Update the deliverer marker.
			delivererMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
			delivererMarker.setSnippet(msg);

			// Display for debug.
			Toast.makeText(App.context, msg.toString(), Toast.LENGTH_SHORT).show();
			Log.i(getClass().getSimpleName(), msg.toString());
		}

		@Override
		public void onStatusChanged(String s, int i, Bundle bundle) {

		}

		@Override
		public void onProviderEnabled(String s) {

		}

		@Override
		public void onProviderDisabled(String s) {

		}

		@Override
		public String toString() {
			return "FollowMeLocationSource [mListener=" + mListener + ", locationManager=" + locationManager + ", criteria=" + criteria + ", bestAvailableProvider=" + bestAvailableProvider + ", minTime=" + minTime + ", minDistance=" + minDistance + "]";
		}
	}

}
