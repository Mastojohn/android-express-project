package com.app.express.activity;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.app.express.R;
import com.app.express.R.id;
import com.app.express.R.layout;
import com.app.express.db.DatabaseHelper;
import com.app.express.helper.App;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * An activity representing a single Delivery detail screen. This activity is only used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link DeliveryListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than a {@link DeliveryDetailFragment}.
 */
@ContentView(R.layout.activity_delivery_detail)
public class DeliveryDetailActivity extends RoboFragmentActivity {

	@InjectView(R.id.button_fillDelivery)
	private Button button_fillDelivery;
	
	@InjectView(R.id.button_map)
	private Button button_map;
	
	@InjectView(R.id.textView_receiverName)
	private TextView textView_receiverName;
	
	@InjectView(R.id.textView_receiverAddress)
	private TextView textView_receiverAddress;
	
	@InjectView(R.id.textView_ReceiverStatsDelivery)
	private TextView textView_ReceiverStatsDelivery;
	
	private Bundle extras;
	
	private String deliveryId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize helpers.
		App.context = getApplicationContext();
 		App.dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
 		
 		// Get extras.
 		extras = getIntent().getExtras();
 		
 		// Get the delivery id to use.
 		deliveryId = extras.getString(DeliveryDetailFragment.ARG_ITEM_ID);
 		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(DeliveryDetailFragment.ARG_ITEM_ID, getIntent().getStringExtra(DeliveryDetailFragment.ARG_ITEM_ID));
			DeliveryDetailFragment fragment = new DeliveryDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.delivery_detail_container, fragment).commit();
		}
		
		// Define the onclick for fill the formulaire of delivery.
		button_fillDelivery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO appeler l'interface que doit faire léo en ce moment.
				Intent intent = new Intent(App.context, CustomerPresence.class);// Scan pour le moment.
				intent.putExtra("deliveryId", Integer.parseInt(deliveryId));
				startActivity(intent);
			}
		});
		
		// Define the onclick for display the map.
		button_map.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(App.context, NextDelivery.class);// Scan pour le moment.
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				NavUtils.navigateUpTo(this, new Intent(this, DeliveryListActivity.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
