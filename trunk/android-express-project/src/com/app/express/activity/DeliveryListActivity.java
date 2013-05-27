package com.app.express.activity;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectFragment;

import com.app.express.R;
import com.app.express.R.id;
import com.app.express.R.layout;
import com.app.express.db.DatabaseHelper;
import com.app.express.db.persistence.Round;
import com.app.express.dummy.DeliveryContent;
import com.app.express.helper.App;
import com.app.express.task.RoundTask;
import com.google.android.gms.maps.MapFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An activity representing a list of Deliveries. This activity has different presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a {@link DeliveryDetailActivity} representing item details. On tablets, the activity presents the list
 * of items and item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a {@link DeliveryListFragment} and the item details (if present) is a
 * {@link DeliveryDetailFragment}.
 * <p>
 * This activity also implements the required {@link DeliveryListFragment.Callbacks} interface to listen for item selections.
 */
@ContentView(R.layout.activity_delivery_list)
public class DeliveryListActivity extends RoboFragmentActivity implements DeliveryListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize helpers.
		App.context = getApplicationContext();
		App.dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

		if (findViewById(R.id.delivery_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((DeliveryListFragment) getSupportFragmentManager().findFragmentById(R.id.delivery_list)).setActivateOnItemClick(true);
		}

		// Launch the thread only if it's useful, else infinite while.
		if (!App.currentRoundSet()) {
			Toast.makeText(this, "Veuillez patienter durant la récupération de votre parcours...", Toast.LENGTH_LONG).show();
			new RoundXmlTask().execute();
		} else {
			// Else, refresh the data content.
			// DeliveryContent.refreshView();
			
		}
	}

	/**
	 * Callback method from {@link DeliveryListFragment.Callbacks} indicating that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(DeliveryDetailFragment.ARG_ITEM_ID, id);
			DeliveryDetailFragment fragment = new DeliveryDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().replace(R.id.delivery_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, DeliveryDetailActivity.class);
			detailIntent.putExtra(DeliveryDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	private class RoundXmlTask extends AsyncTask<Void, Integer, Round> {
		private static final String TOAST_MSG_GET_ROUND = "Recherche du parcours à effectuer ...";
		private static final String TOAST_MSG_ROUND_FIND = "Récupération du parcours à effectuer ...";
		private static final String TOAST_MSG_ROUND_FIND_CACHE = "Récupération des livraisons restantes ...";

		/**
		 * Display message for tu user. {@inheritDoc}
		 */
		@Override
		protected void onPreExecute() {
			Toast.makeText(App.context, TOAST_MSG_GET_ROUND, Toast.LENGTH_LONG).show();

			if (App.currentRoundSet()) {
				// The round is already set.
				Toast.makeText(App.context, TOAST_MSG_ROUND_FIND_CACHE, Toast.LENGTH_LONG).show();
			} else {
				// We have to load the XML file and refresh the DB.
				Toast.makeText(App.context, TOAST_MSG_ROUND_FIND, Toast.LENGTH_LONG).show();
			}
		}

		public RoundXmlTask() {

		}

		/**
		 * Check if a round is already cached or if the round must be get before. {@inheritDoc}
		 */
		@Override
		protected Round doInBackground(Void... params) {
			if (!App.currentRoundSet()) {
				// Get, parse, store on the DB and refresh the App.currentRound.
				Round.tryParseXmlFileRound();
			}

			return App.getCurrendRound();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onPostExecute(final Round round) {
			// Display the round on the map.
			try {
				DeliveryContent.refreshView();
				recreate();
			} catch (Exception e) {
				Log.w("DeliveryListActivity", "Impossible de rafraîchir la vue.", e);
			}
		}

	}
}
