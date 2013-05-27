package com.app.express.activity;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.app.express.R;
import com.app.express.R.layout;
import com.app.express.R.menu;
import com.app.express.db.DatabaseHelper;
import com.app.express.db.persistence.Delivery;
import com.app.express.db.persistence.Packet;
import com.app.express.helper.App;
import com.app.express.helper.IntentIntegrator;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RecapDelivryAway extends Activity {

	private Delivery delivery;
	private int nb_colis;
	private Double poids_colis;
	private TextView tvnb_colis;
	private TextView tvpoids;
	private TextView tvexpediteur;
	private int deliveryId;
	private Button btn_end;
	private List<Packet> packetsToGet;
	private Packet packet;
	private EditText et_com;
	private LocationManager locationManager = null;
	private Location loc;
	private String providerFine = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recap_delivry_away);

		Bundle extras = getIntent().getExtras();
		deliveryId = extras.getInt("deliveryId");
		Log.i("deliveryId", String.valueOf(deliveryId));
		// Initialize helpers.
		App.context = getApplicationContext();
		App.dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		// sur clic du bouton terminer
		btn_end = (Button) this.findViewById(R.id.button_end);
		et_com = (EditText) this.findViewById(R.id.editTextCom);

		/*
		 * évènements "clic" provoqué par l'utilisateur.
		 */
		btn_end.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// le colis est mis en absent
				delivery.setDeliveryOver(true);
				delivery.setReceiverAvailable(false);
				// récuperation de la date
				Date date = new Date();
				delivery.setDateOver(date);
				// Recuperation des coordonée gps
				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Criteria criteria = new Criteria();
				criteria.setAltitudeRequired(false);
				criteria.setBearingRequired(false);
				criteria.setCostAllowed(true);
				criteria.setSpeedRequired(false);
				criteria.setPowerRequirement(Criteria.POWER_LOW);
				criteria.setAccuracy(Criteria.ACCURACY_FINE);
				providerFine = locationManager.getBestProvider(criteria, true);
				loc = locationManager.getLastKnownLocation(providerFine);
				
				try {
					delivery.setLatitude("" + loc.getLatitude());
					delivery.setLongitude("" + loc.getLongitude());
				} catch (Exception e) {
					Log.w("RecapDelivery", "Impossible de récupérer les coordonnées GPS !", e);
				}
				
				try {
					delivery.update();
					Toast.makeText(App.context, "La livraison est terminée. Client absent.", Toast.LENGTH_SHORT).show();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Ajout des commentaires relatif a l'absence
				for (int i = 0; packetsToGet.size() > i; i++) {

					packet = packetsToGet.get(i);
					packet.setDescription(et_com.getText().toString());
					packet.setDeliveryAttempted(true);
					try {
						packet.update();
						Log.i("Packet sauvé en bdd", packet.toString());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
								
				Intent intent = new Intent(RecapDelivryAway.this, DeliveryListActivity.class);
				startActivity(intent);
			}
		});

		// Récuperation de la livraison

		try {
			// Récupération de la livraison
			Dao<Delivery, Integer> deliveryDao = App.dbHelper.getDeliveryDao();
			delivery = deliveryDao.queryForId(deliveryId);
			Log.i("delivery", delivery.toString());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Récuperation des colis de la livraison

		try {
			Dao<Packet, Integer> packetDao = App.dbHelper.getPacketDao();
			// Récuperation des colis de la livraison
			Packet packetTomatch = new Packet();
			packetTomatch.setDelivery(delivery);
			Log.i("delivery récupéré de la bdd", delivery.toString());

			packetsToGet = packetDao.queryForMatchingArgs(packetTomatch);
			poids_colis = (double) 0;
			for (int i = 0; packetsToGet.size() > i; i++) {
				Log.i("colis récupéré de la bdd", packetsToGet.get(i).toString());
				packet = packetsToGet.get(i);
				poids_colis = poids_colis + packet.getWeight();
				nb_colis = nb_colis + 1;

			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Modification des affichage nb colis et poids
		// Récupération du TextView du layout courant
		tvnb_colis = (TextView) this.findViewById(R.id.textView_colis_restant);
		// On modifie l'affichage
		String colis_nb = "Il y a " + nb_colis + " colis";
		// On affecte la valeur
		tvnb_colis.setText(colis_nb);
		// Récupération du TextView du layout courant
		tvpoids = (TextView) this.findViewById(R.id.textView_poids_colis);
		// On modifie l'affichage
		String colis_poids = "Les colis pèsent : " + poids_colis + " kg";
		// On affecte la valeur
		tvpoids.setText(colis_poids);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recap_delivry_away, menu);
		return true;
	}

}
