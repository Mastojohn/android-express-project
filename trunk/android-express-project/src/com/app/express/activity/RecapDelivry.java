package com.app.express.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

public class RecapDelivry extends Activity {

	private Delivery delivery;
	private int	deliveryId = 2;
	private int nb_colis;
	private Double poids_colis;
	private TextView tvnb_colis;
	private TextView tvpoids;
	private TextView tvexpediteur;
	private Button bnt_valid_cmd;
//	private final TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recap_delivry);
				// Initialize helpers.
				App.context = getApplicationContext();
				App.dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
				//validation finale
				bnt_valid_cmd = (Button) findViewById(R.id.button_valid_cmd);
				bnt_valid_cmd.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Initialisation du scan
						 	Intent intent = new Intent(RecapDelivry.this, SignatureReceiverActivity.class);					
							intent.putExtra("deliveryId", deliveryId);
							startActivity(intent);
							
					}
				});
				
				//Récuperation de la livraison	
				
				try {
					//Récupération de la livraison !en dur pour le moment !
					Dao<Delivery, Integer> deliveryDao = App.dbHelper.getDeliveryDao();
					delivery = deliveryDao.queryForId(deliveryId);
					Log.i("delivery", delivery.toString());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//Récuperation des colis de la livraison
				List<Packet> packetsToGet = null;
				try {
						Dao<Packet, Integer> packetDao = App.dbHelper.getPacketDao();
						//Récuperation des colis de la livraison
						Packet packetTomatch = new Packet();
						packetTomatch.setDelivery(delivery);
						
						
						packetsToGet = packetDao.queryForMatchingArgs(packetTomatch);		
						Packet packet;
						
						poids_colis = (double) 0;
						for(int i=0;packetsToGet.size() > i;i++)
						{
							Log.i("colis récupéré de la bdd", packetsToGet.get(i).toString());
							packet = packetsToGet.get(i);
							poids_colis = poids_colis + packet.getWeight();
							nb_colis = nb_colis +1;
						
						}
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
						//Modification des affichage nb colis et poids 
						// Récupération du TextView du layout courant
						tvnb_colis = (TextView) this.findViewById(R.id.textView_colis_restant);
						//On modifie l'affichage
						String colis_nb = "Il y a "+nb_colis+" colis";
						// On affecte la valeur
						tvnb_colis.setText(colis_nb);
						// Récupération du TextView du layout courant
						tvpoids = (TextView) this.findViewById(R.id.textView_poids_colis);
						//On modifie l'affichage
						String colis_poids = "Les colis pése : "+poids_colis+" kg";
						// On affecte la valeur
						tvpoids.setText(colis_poids);
				
			//Boucle sur packet
			Packet packet;
			int i=1;
			Iterator j = packetsToGet.iterator();
			TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
			tabs.setup(); 
			FrameLayout fl = (FrameLayout)findViewById(android.R.id.tabcontent);
			while(j.hasNext())
			{

				 packet = (Packet)j.next();
				 	//Generation des texte
						
					LinearLayout ll = new LinearLayout(this);
					LayoutParams lp = new LayoutParams(500, 500);
					ll.setId(i);
					LinearLayout.LayoutParams relativeParams = new LinearLayout.LayoutParams(
					        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					
//					//Création du texte poids
//						TextView tv = new TextView(this);
//						
//						tv.setLayoutParams(new LayoutParams(100, 100));
//						tv.setText("Poids : "+packet.getWeight());
					//Création du spinner ( liste)
						Spinner Sp = new Spinner(this);
						Sp.setLayoutParams(lp);
						Sp.setId(i);
						List<String> list = new ArrayList<String>();
						list.add("Colis Accepté");
						list.add("Colis Refusé");
						ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
							android.R.layout.simple_spinner_item, list);
						dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						Sp.setAdapter(dataAdapter);
					//création textBox (EditText)
						EditText et = new EditText(this);
						et.setLayoutParams(new LayoutParams(400, 400));	
					//Création du bouton de sauvegarde
						Button btn = new Button(this);
						btn.setText("Valider");
						btn.setLayoutParams(new LayoutParams(200, 200));	
					//Placement
						ll.setOrientation(1);
						Sp.setPadding(20, 0, 50, 10);
//						tv.setPadding(10, 10, 10, 10);
						et.setWidth(550);
						btn.setWidth(250);
					//Ajour de la textbox et du spinner au linearlayout
						ll.addView(Sp,relativeParams);
//						ll.addView(tv, relativeParams);	
						ll.addView(et,relativeParams);
						ll.addView(btn,relativeParams);

					//Ajout des linealayout au Frame layout
//						btn.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								//v.findViewById(10+i);
//								//test(v);
//							}
//						});
						fl.addView(ll, lp);
					
					
					//generation d'onglet 
					 TabHost.TabSpec spec=tabs.newTabSpec("Colis "+i); 
					 spec.setContent(i); 
					 spec.setIndicator("Colis "+i); 
					 tabs.addTab(spec); 
					 tabs.setCurrentTab(0);
					 
					 
					
					 i++;
				
			}


			
			
			
			
			
//			 final TabHost tabs=(TabHost)findViewById(R.id.tabhost); 
//			 tabs.setup(); 
//			 TabHost.TabSpec spec=tabs.newTabSpec("buttontab"); 
//			 spec.setContent(R.id.buttontab); 
//			 spec.setIndicator("Btn"); 
//			 tabs.addTab(spec); 
//			 tabs.setCurrentTab(0); 
//			 Button btn=(Button)tabs.getCurrentView().findViewById(R.id.buttontab); 
//			 btn.setOnClickListener(new View.OnClickListener() { 
//			 public void onClick(View view) 
//			 { 
//				 TabHost.TabSpec spec=tabs.newTabSpec("tag1"); 
//				 spec.setContent(new TabHost.TabContentFactory() { 
//				 public View createTabContent(String tag) 
//				 { 
//					 return(new AnalogClock(RecapDelivry.this)); 
//				 } 
//				 }); 
//				 spec.setIndicator("malemi"); 
//				 tabs.addTab(spec); 
//			 } 
//			 }); 
//			 
	}
//	private void test(View v){
//		System.out.println("--------------------------------"+tabs.getCurrentTab());
//	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recap_delivry, menu);
		return true;
	}

}
