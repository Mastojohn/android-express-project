package com.app.express.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.app.express.R;
import com.app.express.R.layout;
import com.app.express.R.menu;
import com.app.express.config.Categories;
import com.app.express.db.DatabaseHelper;
import com.app.express.db.persistence.Delivery;
import com.app.express.db.persistence.Packet;
import com.app.express.helper.App;
import com.app.express.helper.IntentIntegrator;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AnalogClock;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

@ContentView(R.layout.activity_recap_delivry)
public class RecapDelivry extends RoboActivity {

	@InjectView(R.id.textView_colis_restant)
	private TextView tvnb_colis;

	@InjectView(R.id.textView_poids_colis)
	private TextView tvpoids;

	@InjectView(R.id.button_valid_cmd)
	private Button bnt_valid_cmd;

	@InjectView(android.R.id.tabhost)
	private TabHost tabs;

	@InjectView(android.R.id.tabcontent)
	private FrameLayout fl;

	/**
	 * Contient les commentaires.
	 */
	private ArrayList<EditText> listCommentDescription = new ArrayList<EditText>();

	private ArrayList<String> listPacketState = new ArrayList<String>();

	private Dao<Delivery, Integer> deliveryDao;

	private Bundle extras;

	private int deliveryId;
	private Delivery delivery;
	private int nb_colis;
	private Double poids_colis = 0.0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize helpers.
		App.context = getApplicationContext();
		App.dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

		// Get extras.
		extras = getIntent().getExtras();

		// Get the delivery id to use.
		deliveryId = extras.getInt("deliveryId", 0);
		if (deliveryId == 0) {
			Log.w("RecapDelivery", "Pas de valeur deliveryId passée par Intent, fermeture de la vue.");
			finish();
		}

		deliveryDao = App.dbHelper.getDeliveryDao();

		try {
			delivery = deliveryDao.queryForId(deliveryId);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		bnt_valid_cmd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Initialisation du scan
				Intent intent = new Intent(RecapDelivry.this, SignatureReceiverActivity.class);
				intent.putExtra("deliveryId", deliveryId);
				startActivity(intent);
			}
		});

		// Récuperation des colis de la livraison
		ForeignCollection<Packet> packetsToGet = delivery.getPackets();
		CloseableIterator<Packet> packetIteratorCloseable = packetsToGet.closeableIterator();
		List<Packet> packetsToDisplay = new ArrayList<Packet>();

		while (packetIteratorCloseable.hasNext()) {
			Packet packet = packetIteratorCloseable.next();

			if (!(packet.getDeliveryAttempted() && packet.getDeliveredState().equals(Categories.Types.type_delivery_state.FORGOTTEN))) {
				packetsToDisplay.add(packet);
				Log.i("colis récupéré de la bdd", packet.toString());
				poids_colis = poids_colis + packet.getWeight();
				nb_colis = nb_colis + 1;
			}
		}

		// Modification des affichage nb colis et poids
		tvnb_colis.setText("Il y a " + nb_colis + " colis");
		tvpoids.setText("Les colis pèse : " + poids_colis + " kg");

		// Boucle sur packet

		// Used like name and define the id of the tab.
		int i = 0;
		tabs.setup();

		Iterator<Packet> packetIterator = packetsToDisplay.iterator();
		
		while (packetIterator.hasNext()) {
			Packet packet = (Packet) packetIterator.next();

			LinearLayout ll = new LinearLayout(this);
			LayoutParams lp = new LayoutParams(500, 500);
			ll.setId(i);
			LinearLayout.LayoutParams relativeParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			// Create the list of choices.
			List<String> list = new ArrayList<String>() {
				{
					add("Colis accepté");
					add("Colis refusé");
				}
			};

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// //Création du texte poids
			// TextView tv = new TextView(this);
			//
			// tv.setLayoutParams(new LayoutParams(100, 100));
			// tv.setText("Poids : "+packet.getWeight());

			// Création du spinner ( liste)
			Spinner Sp = new Spinner(this);
			Sp.setLayoutParams(lp);
			Sp.setId(i);
			Sp.setAdapter(dataAdapter);

			// création textBox (EditText)
			EditText commentDescription = new EditText(this);
			commentDescription.setLayoutParams(new LayoutParams(550, 400));
			commentDescription.setTop(100);
			commentDescription.setWidth(550);
			
			// Placement
			ll.setOrientation(1);
			Sp.setPadding(20, 0, 50, 10);
			Sp.setTop(70);
			// tv.setPadding(10, 10, 10, 10);

			// Ajour de la textbox et du spinner au linearlayout
			ll.addView(Sp, relativeParams);
			// ll.addView(tv, relativeParams);
			ll.addView(commentDescription, relativeParams);

			// Ajout des linealayout au Frame layout
			// btn.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// //v.findViewById(10+i);
			// //test(v);
			// }
			// });
			fl.addView(ll, lp);

			// generation d'onglet
			TabHost.TabSpec spec = tabs.newTabSpec("Colis " + i);
			spec.setContent(i);
			spec.setIndicator("Colis " + (i + 1));
			tabs.addTab(spec);

			listCommentDescription.add(commentDescription);

			i++;
		}

		tabs.setCurrentTab(0);

		// final TabHost tabs=(TabHost)findViewById(R.id.tabhost);
		// tabs.setup();
		// TabHost.TabSpec spec=tabs.newTabSpec("buttontab");
		// spec.setContent(R.id.buttontab);
		// spec.setIndicator("Btn");
		// tabs.addTab(spec);
		// tabs.setCurrentTab(0);
		// Button btn=(Button)tabs.getCurrentView().findViewById(R.id.buttontab);
		// btn.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View view)
		// {
		// TabHost.TabSpec spec=tabs.newTabSpec("tag1");
		// spec.setContent(new TabHost.TabContentFactory() {
		// public View createTabContent(String tag)
		// {
		// return(new AnalogClock(RecapDelivry.this));
		// }
		// });
		// spec.setIndicator("malemi");
		// tabs.addTab(spec);
		// }
		// });
		//
	}

	// private void test(View v){
	// System.out.println("--------------------------------"+tabs.getCurrentTab());
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recap_delivry, menu);
		return true;
	}

}
