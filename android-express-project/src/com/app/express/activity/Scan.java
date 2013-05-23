package com.app.express.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.app.express.R;
import com.app.express.R.layout;
import com.app.express.R.menu;
import com.app.express.config.Categories;
import com.app.express.db.persistence.*;
import com.app.express.helper.App;
import com.app.express.helper.IntentIntegrator;
import com.app.express.helper.IntentResult;
import com.j256.ormlite.dao.Dao;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Scan extends Activity {

	private Button button_Scan;
	private Button button_UnScanable;
	private Button button_Valid;
	private Button button_packet_away;
	
	private List<Packet> packetsScanned = new ArrayList<Packet>();
	
	
	//en dur pour le moment
	private Delivery delivery;
	private int deliveryId = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Définition de la vue, on lui affecte R.layout.activity_scan ce qui
		// représente notre vue
		// déclarer dans le dossier layout
		setContentView(R.layout.activity_scan);
		/*
		 * Récupère une référence sur les bouton en utilisant leurs identifiant
		 */
		button_Valid = (Button) findViewById(R.id.button_Valider);
		button_Scan = (Button) findViewById(R.id.button_Scan);
		button_UnScanable = (Button) findViewById(R.id.button_UnScanable);
		button_packet_away = (Button) findViewById(R.id.button_Colis_Absent);
		
		final TextView tvResult_Unscanned = (TextView) this.findViewById(R.id.editText_Code_Barre);
		/*
		 * évènements "clic" provoqué par l'utilisateur.
		 */
		button_Scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Initialisation du scan
				IntentIntegrator.initiateScan(Scan.this);
			}
		});
		button_UnScanable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Test de la validation du code barre
				

				String out = tvResult_Unscanned.getText().toString();
				Intent intent = new Intent(Scan.this, Scan.class);
				intent.putExtra("bareCode", out);
				startActivity(intent);
				
			}
		});
		button_packet_away.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
				
				Dao<Packet, Integer> packetDao = App.dbHelper.getPacketDao();
				//Récuperation des colis de la livraison
				Packet packetToMatch = new Packet();
				packetToMatch.setDelivery(delivery);
				packetToMatch.setPacketScanned(false);
				packetToMatch.setDeliveredState("En cours");
				
				List<Packet> packetsTogetAway;
				packetsTogetAway = packetDao.queryForMatchingArgs(packetToMatch);
				// Mise a  l'etat oublier tout les colis restant
				Packet packet;
				
				for(int i=0; i < packetsTogetAway.size();i++)
				{
					packet = packetsTogetAway.get(i);
					Log.i("packet récupéré de la bdd", packet.toString());
					
					packet.setDeliveredState("Oublié");
					
					packet.update();
					packetsScanned.add(packet);
					
					Log.i("Packet sauvé en bdd en mode oublier", packet.toString());
				}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		try {
			//Récupération de la livraison !en dur pour le moment !
			Dao<Delivery, Integer> deliveryDao = App.dbHelper.getDeliveryDao();
			delivery = deliveryDao.queryForId(deliveryId);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		try {
			Dao<Packet, Integer> packetDao = App.dbHelper.getPacketDao();
			//Récuperation des colis de la livraison
			Packet packetToMatch = new Packet();
			packetToMatch.setDelivery(delivery);
			packetToMatch.setPacketScanned(false);
			packetToMatch.setDeliveredState("En cours");
			
			List<Packet> packetsToScan;
			packetsToScan = packetDao.queryForMatchingArgs(packetToMatch);
			
			if(packetsToScan.size() > 0)
			{
				//Modification du champs colis restant
				String exp_nbrcolis = "Il reste "+packetsToScan.size()+" coli(s) à scanner";
				TextView tvcolis_restant = (TextView) this.findViewById(R.id.textView_Nb_Colis);
				tvcolis_restant.setText(exp_nbrcolis);
				Log.i("Scan", "Il y a "+packetsToScan.size()+" paquets à scanner pour ette livraison.");
				
				button_Valid.setClickable(false);
				button_Scan.setClickable(true);
				button_UnScanable.setClickable(true);
			}else{
				Log.i("Scan", "Il n'y a plus de paquet à scanner pour ette livraison.");
				button_Valid.setClickable(true);
				button_Scan.setClickable(false);
				button_UnScanable.setClickable(false);
				
				
			}
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Récupération des parametres
		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			// On récupere notre parametre String
			String bareCode = (String) extras.get("bareCode");
			
			try {
				
				Dao<Packet, Integer> packetDao = App.dbHelper.getPacketDao();
				//Récuperation des colis non scanner
				Packet packetToMatch = new Packet();
				packetToMatch.setBarcode(bareCode);
				packetToMatch.setPacketScanned(false);
				packetToMatch.setDelivery(delivery);
				packetToMatch.setDeliveredState("En cours");
				
				// Ajouter filtre avec delivery pour ne get que les packets de la livraison actuelle.
				List<Packet> packets = packetDao.queryForMatchingArgs(packetToMatch);
				Packet packet = null;
				
				// Get only the first result, if exists.
				if(packets.size() > 0){
					packet = packets.get(0);
					Log.i("packet récupéré de la bdd", packet.toString());
					
					packet.setPacketScanned(true);
					
					packet.update();
					packetsScanned.add(packet);
					
					Log.i("Packet sauvé en bdd", packet.toString());
					
					// Récupération du TextView du layout courant
					TextView tvResult = (TextView) this
							.findViewById(R.id.editText_Code_Barre);

					// On affecte la valeur
					tvResult.setText(bareCode);
					
					//Information de livraison
					
					//Recup Information colis
					String poids = String.valueOf(packet.getWeight());
					String taille = packet.getSize();
					//Traitement des information : 
					TextView tvcolis_restant = (TextView) this
							.findViewById(R.id.textView_Nb_Colis);
					// On décrémente le nb de colis à scanner.
					tvcolis_restant.setText("Il reste "+(packet.getDelivery().countPacketToScan() -1)+" coli(s) à scanner");
					String exp_poids = "Le colis pèse : "+poids+"kg";
					String exp_taille = "Le colis mesure : "+taille+"cm";
						//Modification du champs poids 
							TextView tvpoids = (TextView) this
									.findViewById(R.id.TextView_poids);
							tvpoids.setText(exp_poids);
						// et taille
							TextView tvtaille = (TextView) this
									.findViewById(R.id.TextView_taille);
							tvtaille.setText(exp_taille);
							
					if((packet.getDelivery().countPacketToScan() -1) == 0)
					{
						button_Valid.setClickable(true);
						button_Scan.setClickable(false);
						button_UnScanable.setClickable(false);
					}
							
				}else{
					// Prévenir user que le code n'existe pas
					new AlertDialog.Builder(this)
				    .setTitle("Code barre non présent")
				    .setMessage("Le colis n'est pas présent ou à dejas était scanner")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            // continue with delete
					        }
					     })
				     .show();
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scan, menu);
		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				IntentResult scanResult = IntentIntegrator.parseActivityResult(
						requestCode, resultCode, data);

				if (scanResult != null) {
					String out = scanResult.getContents();

					if (out != null) {

						// Envoi du resultat et affichage de la page details
						Intent intent = new Intent(Scan.this, Scan.class);
						intent.putExtra("bareCode", out);
						startActivity(intent);
					}
				}
			}
		}
	}

}
