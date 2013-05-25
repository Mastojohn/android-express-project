package com.app.express.activity;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.express.R;
import com.app.express.R.id;
import com.app.express.R.layout;
import com.app.express.db.persistence.Delivery;
import com.app.express.dummy.DeliveryContent;
import com.google.android.gms.internal.de;

/**
 * A fragment representing a single Delivery detail screen. This fragment is either contained in a {@link DeliveryListActivity} in two-pane mode (on tablets) or
 * a {@link DeliveryDetailActivity} on handsets.
 */
public class DeliveryDetailFragment extends Fragment {
	
	/**
	 * The fragment argument representing the item ID that this fragment represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DeliveryContent.DeliveryItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
	 */
	public DeliveryDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DeliveryContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_delivery_detail, container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			// Convert the view who inherit to Delivery for generate the view with data of the delivery.
			Delivery delivery = (Delivery) mItem;
			// TODO Problème, la vue est recréée, c'est parce que je me suis planté en la créant pas dans le bon xml mais j'arrive pas à inverser...
			((TextView) rootView.findViewById(R.id.textView_receiverName)).setText(Html.fromHtml(mItem.displayReceiver()));
			((TextView) rootView.findViewById(R.id.textView_receiverAddress)).setText(Html.fromHtml(mItem.displayAddress()));
			((TextView) rootView.findViewById(R.id.textView_ReceiverStatsDelivery)).setText(Html.fromHtml(mItem.displayStatsDelivery()));
			
		}

		return rootView;
	}
}
