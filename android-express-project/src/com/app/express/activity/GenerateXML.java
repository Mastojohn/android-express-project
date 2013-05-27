package com.app.express.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.app.express.R;
import com.app.express.R.layout;
import com.app.express.R.menu;
import com.app.express.config.Categories;
import com.app.express.db.DatabaseHelper;
import com.app.express.db.persistence.Deliverer;
import com.app.express.db.persistence.Delivery;
import com.app.express.db.persistence.Packet;
import com.app.express.db.persistence.Round;
import com.app.express.db.persistence.User;
import com.app.express.helper.App;
import com.app.express.helper.Session;
import com.app.express.helper.XmlParser.Entry;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

public class GenerateXML extends Activity {
	private Deliverer deliverer;
	public Round round = null;
	public ForeignCollection<User> users;
	public ForeignCollection<Packet> packets;
	private Bundle extras;
	private static int deliveryId;
    private Delivery delivery;
    private User user;
    private Session session;
	Button bGenerate;
	private static final String ns = null;
	private int NbParser = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Initialize helpers.
		App.context = getApplicationContext();
		App.dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		setContentView(R.layout.activity_generate_xml);
        bGenerate = (Button)findViewById(R.id.Generatexml);
        bGenerate.setOnClickListener(new OnClickListener()
        {       
            public void onClick(View v)
            {
                Log.v("log_tag", "Panel Cleared");

           		Bundle extras = getIntent().getExtras();
    			deliveryId = extras.getInt("deliveryId");
                WriteXML(App.context);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.generate_xml, menu);
		return true;
	}



	public Round parse(InputStream in) throws XmlPullParserException, IOException, SQLException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			List feed = readFeed(parser);
			return this.round;
		} finally {
			in.close();
		}
	}

	private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException, SQLException {
		List entries = new ArrayList();

		parser.require(XmlPullParser.START_TAG, getNs(), "tournee");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("livreur")) {
				NbParser = 1;
				entries.add(readEntry(parser));
			}
			if (name.equals("livraison")) {
				NbParser = 2;
				entries.add(readEntry(parser));
			}
			if (name.equals("date_tournee")) {
				NbParser = 3;
				entries.add(readEntry(parser));
			}

		}

		return entries;
	}

	public static String getNs() {
		return ns;
	}

	public static class Entry {
		public final String id;
		public final String nom;
		public final String date_tournee;
		public final String id_livraison;
		public final String nom_expediteur;
		public final String rue;
		public final String cp;
		public final String ville;
		public final String telephone;
		public final String nombre;
		public final String code_barre;
		public final String taille;
		public final String poids;
		public final String nom_destinataire;
		public final String cp_destinataire;
		public final String rue_destinataire;
		public final String ville_destinataire;
		public final String complement_adresse;
		public final String telephone_destinataire;
		public final String portable;

		private Entry(String id, String nom, String date_tournee, String id_livraison, String nom_expediteur, String rue, String cp, String ville, String telephone, String nombre, String code_barre, String taille, String poids, String nom_destinataire, String rue_destinataire, String cp_destinataire, String ville_destinataire, String complement_adresse, String telephone_destinataire, String portable) {
			this.id = id;
			this.nom = nom;
			this.date_tournee = date_tournee;
			this.id_livraison = id_livraison;
			this.nom_expediteur = nom_expediteur;
			this.rue = rue;
			this.cp = cp;
			this.ville = ville;
			this.telephone = telephone;
			this.nombre = nombre;
			this.code_barre = code_barre;
			this.taille = taille;
			this.poids = poids;
			this.nom_destinataire = nom_destinataire;
			this.cp_destinataire = cp_destinataire;
			this.rue_destinataire = rue_destinataire;
			this.ville_destinataire = ville_destinataire;
			this.complement_adresse = complement_adresse;
			this.telephone_destinataire = telephone_destinataire;
			this.portable = portable;

		}
	}

	// Parses the contents of an entry. If it encounters a title, summary, or
	// link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the
	// tag.
	private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException, SQLException {
		// VARIABLE
		String id = null;
		String nom = null;
		String date_tournee = null;
		String id_livraison = null;
		String nom_expediteur = null;
		String rue = null;
		String cp = null;
		String ville = null;
		String telephone = null;
		String nombre = null;
		String code_barre = null;
		String taille = null;
		String poids = null;
		String nom_destinataire = null;
		String cp_destinataire = null;
		String rue_destinataire = null;
		String ville_destinataire = null;
		String complement_adresse = null;
		String telephone_destinataire = null;
		String portable = null;

		try {
			users = App.dbHelper.getDeliveryDao().getEmptyForeignCollection("users");
			packets = App.dbHelper.getDeliveryDao().getEmptyForeignCollection("packets");
		} catch (SQLException e) {
			Log.e("XmlParser", "Une erreur est survenue lors de la génération des collections des objets de persistance !", e);
			e.printStackTrace();
		}

		// 1 = TEST LIVREUR / 2 = LIVRAISON / 3 = DATE TOURNEE
		if (NbParser == 1) {
			parser.require(XmlPullParser.START_TAG, ns, "livreur");

			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String infosLivreur = parser.getName();
				if (infosLivreur.equals("id")) {
					id = Read(parser, infosLivreur);
				}
				else if (infosLivreur.equals("nom")) {
					nom = Read(parser, infosLivreur);
				}
			}
			this.deliverer = new Deliverer(App.dbHelper.getDelivererDao(), Integer.parseInt(id), nom);
			this.deliverer.create();
		}

		// Faire boucle sur toutes les livraisons
		int priority_delivery = 1;

		 if (NbParser == 2) {
			parser.require(XmlPullParser.START_TAG, ns, "livraison");

			// On initialise une livraison qu'on crera lorsque l'id de la livraison aura été récupéré.
			Delivery delivery = null;

			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String infosLivraison = parser.getName();
				if (infosLivraison.equals("id")) {
					id_livraison = Read(parser, infosLivraison);
				}

				// On créer une variable locale temporaire utilisée dans la boucle puis mise dans le liste à la fin.
				if (delivery == null && id_livraison != null) {
					delivery = new Delivery(App.dbHelper.getDeliveryDao(), this.round, id_livraison, priority_delivery);
					delivery.create();
				}

				if (infosLivraison.equals("expediteur")) {
					parser.require(XmlPullParser.START_TAG, ns, "expediteur");
					while (parser.next() != XmlPullParser.END_TAG) {
						if (parser.getEventType() != XmlPullParser.START_TAG) {
							continue;
						}
						String infosExpediteur = parser.getName();
						if (infosExpediteur.equals("nom")) {
							nom_expediteur = Read(parser, infosExpediteur);
						}
						else if (infosExpediteur.equals("rue")) {
							rue = Read(parser, infosExpediteur);
						}
						else if (infosExpediteur.equals("cp")) {
							cp = Read(parser, infosExpediteur);
						}
						else if (infosExpediteur.equals("ville")) {
							ville = Read(parser, infosExpediteur);
						}
						else if (infosExpediteur.equals("telephone")) {
							telephone = Read(parser, infosExpediteur);
						}
					}

					// On créé l'expéditeur et on l'ajoute la liste des utilisateurs de la livraison (expéditeur, destinataire)
					User sender = new User(App.dbHelper.getUserDao(), delivery, nom_expediteur, Categories.Types.type_user.SENDER, rue, "", cp, ville, telephone);
					sender.create();
					this.users.add(sender);
				}
				else if (infosLivraison.equals("colis")) {
					parser.require(XmlPullParser.START_TAG, ns, "colis");
					while (parser.next() != XmlPullParser.END_TAG) {
						if (parser.getEventType() != XmlPullParser.START_TAG) {
							continue;
						}
						String infosColis = parser.getName();
						if (infosColis.equals("nombre")) {
							nombre = Read(parser, infosColis);
						}
						if (infosColis.equals("paquet")) {
							parser.require(XmlPullParser.START_TAG, ns, "paquet");
							while (parser.next() != XmlPullParser.END_TAG) {
								if (parser.getEventType() != XmlPullParser.START_TAG) {
									continue;
								}
								String infosPaquet = parser.getName();
								if (infosPaquet.equals("code_barre")) {
									code_barre = Read(parser, infosPaquet);
								} else if (infosPaquet.equals("taille")) {
									taille = Read(parser, infosPaquet);
								} else if (infosPaquet.equals("poids")) {
									poids = Read(parser, infosPaquet);
								}
							}

							// On créé le colis et on l'ajoute à la liste des colis de la livraison.
							Packet packet = new Packet(App.dbHelper.getPacketDao(), delivery, code_barre, taille, Double.parseDouble(poids.replace(",", ".")));
							packet.create();
							this.packets.add(packet);
						}
					}
					// On lie nos packets à notre livraison.
					delivery.setPackets(this.packets);
				}
				else if (infosLivraison.equals("destinataire")) {

					while (parser.next() != XmlPullParser.END_TAG) {
						if (parser.getEventType() != XmlPullParser.START_TAG) {
							continue;
						}
						String infosDestinataire = parser.getName();
						if (infosDestinataire.equals("nom")) {
							nom_destinataire = Read(parser, infosDestinataire);
						} else if (infosDestinataire.equals("rue")) {
							rue_destinataire = Read(parser, infosDestinataire);
						} else if (infosDestinataire.equals("cp")) {
							cp_destinataire = Read(parser, infosDestinataire);
						} else if (infosDestinataire.equals("ville")) {
							ville_destinataire = Read(parser, infosDestinataire);
						} else if (infosDestinataire.equals("complement_adresse")) {
							complement_adresse = Read(parser, infosDestinataire);
						} else if (infosDestinataire.equals("telephone")) {
							telephone_destinataire = Read(parser, infosDestinataire);
						} else if (infosDestinataire.equals("portable")) {
							portable = Read(parser, infosDestinataire);// TODO Stocker le portable également.
						}
					}

					// On créé le destinataire et on l'ajoute à la liste des utilisateurs de la livraison (expéditeur, destinataire)
					User receiver = new User(App.dbHelper.getUserDao(), delivery, nom_destinataire, Categories.Types.type_user.RECEIVER, rue_destinataire, complement_adresse, cp_destinataire, ville_destinataire, telephone_destinataire);
					receiver.create();
					this.users.add(receiver);
				}
			}

			priority_delivery++;
		}

		// Fin de boucle livraison

		else if (NbParser == 3) {
			String dateTournee = parser.getName();
			if (dateTournee.equals("date_tournee")) {
				date_tournee = Read(parser, dateTournee);
			}

			try {
				// On crée le parours/tournée s'il n'existe pas déjà.
				if (this.round == null) {
					this.round = new Round(App.dbHelper.getRoundDao(), this.deliverer, new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH).parse(date_tournee));
					this.round.create();
				}
			} catch (Exception e) {
				try {
					this.round = new Round(App.dbHelper.getRoundDao(), this.deliverer, new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH).parse(new Date().getDate() + "-" + new Date().getMonth() + "-" +new Date().getYear()));
					this.round.create();
					Log.w("XmlParser", "Impossible de récupérer la date ce la tournée, mise par défaut à la date du jour.");
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		Entry entry = new Entry(id, nom, date_tournee, id_livraison, nom_expediteur, rue, cp, ville, telephone, nombre, code_barre, taille, poids, nom_destinataire, rue_destinataire, cp_destinataire, ville_destinataire, complement_adresse, telephone_destinataire, portable);

		return entry;

	}
	

	@SuppressWarnings("null")
	public void WriteXML(Context context) {
		FileOutputStream fOut = null;
		OutputStreamWriter osw = null;
		File mFile = null;
		int livreurId = 0;
		String livreurNom = null;
		String livreurEmail = null;
		String packetCB = null;
		String packetNombre = null;
		String packetTaille = null;
		String packetId = null;
		String packetPoid = null;
		String deliveryID = null;
		Date deliveryDate = null;
		String deliveryLongitude = null;
		String deliveryLatitude = null;

		Dao<Delivery, Integer> deliveryDao = App.dbHelper.getDeliveryDao();
		Dao<Deliverer, Integer> delivererDao = App.dbHelper.getDelivererDao();
		Dao<Packet, Integer> packetDao = App.dbHelper.getPacketDao();
		Dao<User, Integer> userDao = App.dbHelper.getUserDao();
		try {
			delivery = deliveryDao.queryForId(deliveryId);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Récuperation des colis de la livraison
		Packet packetToMatch = new Packet();
		packetToMatch.setDelivery(delivery);
		
		List<Packet> packetsToScan = null;
		try {
			packetsToScan = packetDao.queryForMatchingArgs(packetToMatch);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		//Récuperation du livreur
		Delivery deliveryToMatch = new Delivery();
		deliveryToMatch.setDeliveryId(delivery.getDeliveryId());
		List<Delivery> deliveryTogetAway = null;
		try {
			deliveryTogetAway = deliveryDao.queryForMatchingArgs(deliveryToMatch);
					
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Récuperation du destinataire
		User userReceiveirToMatch = new User();
		userReceiveirToMatch.setUserId(delivery.getReceiver().getUserId());
		
		List<User> userReceveirTogetAway = null;
		try {
			userReceveirTogetAway = userDao.queryForMatchingArgs(userReceiveirToMatch);
					
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Récuperation du l'expediteur
//		User userToMatch = new User();
//		userToMatch.setUserId(delivery.getSender().getUserId());
//		//userToMatch.setTypeUser("sender");
//		List<User> usersTogetAway = null;
//
//		try {
//			usersTogetAway = userDao.queryForMatchingArgs(userToMatch);
//					
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
//		if (usersTogetAway.size()>0){
//			User user = null;
//			int k=1;
//			Iterator j = usersTogetAway.iterator();
//			while(j.hasNext())
//				{
//
//				livreurNom = user.getName();
//				livreurId = user.getUserId();
//				}
//			
//		}
		
		try {
			File newxmlfile = new File(
					Environment.getExternalStorageDirectory() + "/fichier.xml");
			try {
				newxmlfile.createNewFile();
				} 
			catch (IOException e) {
				Log.e("IOException", "exception in createNewFile() method");
				}
			FileOutputStream fileos = null;
			try{
				fileos = new FileOutputStream(newxmlfile);
				}catch(FileNotFoundException e){
				Log.e("FileNotFoundException", "can't create File OutputStream");
				}
				XmlSerializer serializer = Xml.newSerializer();
				try {
					serializer.setOutput(fileos, "UTF8");
				    serializer.startDocument("UTF-8", true); 
				    serializer.startTag("", "tournee");  
						    serializer.startTag("", "livreur");
						    serializer.startTag("", "id");
		
						    serializer.endTag("", "id");
						    serializer.startTag("", "nom");
		
						    serializer.endTag("", "nom");
					    serializer.endTag("", "livreur");
					    serializer.startTag("", "date_tournee");
					    serializer.text(String.valueOf(delivery.getDateOver()));
					    serializer.endTag("", "date_tournee");
				    
				    //FOR SUR TOUTES LES TOURNEES
					for (int e = 0; e<deliveryTogetAway.size(); e++)
					{     
					   	if (deliveryTogetAway.size()>0){
							Delivery delivery = null;
							int i=1;
							Iterator j = deliveryTogetAway.iterator();
							while(j.hasNext())
							{
								delivery = (Delivery)j.next();
					    serializer.startTag("", "livraison");
						    serializer.startTag("", "id");
						    serializer.text(String.valueOf(delivery.getDeliveryId()));
						    serializer.endTag("", "id");
						    serializer.startTag("", "Longitude");
						    serializer.text(String.valueOf(delivery.getLongitude()));						    
						    serializer.endTag("", "Longitude");
						    serializer.startTag("", "Latitude");
						    serializer.text(String.valueOf(delivery.getLatitude()));
						    serializer.endTag("", "Latitude");
						    serializer.startTag("", "Image");
						    serializer.text(String.valueOf(delivery.getSignature()));
						    serializer.endTag("", "Image");
						    
						    
//						    if (usersTogetAway.size()>0){
//						    	User user = null;
//						    	Iterator k = usersTogetAway.iterator();
//						    	while(k.hasNext())
//								{
//						    		user = (User)k.next();
//								    if (user.getTypeUser().equals(Categories.Types.type_user.SENDER)){
//								    serializer.startTag("", "expediteur");
//									    serializer.startTag("", "nom");
//									    	//serializer.text(livreurNom);
//									    serializer.text(String.valueOf(user.getName()));
//									    serializer.endTag("", "nom");
//									    serializer.startTag("", "rue");
//									    serializer.text(String.valueOf(user.getAddress()));
//									    serializer.endTag("", "rue");
//									    serializer.startTag("", "cp");
//									    serializer.text(String.valueOf(user.getZipCode()));
//									    serializer.endTag("", "cp");
//									    serializer.startTag("", "ville");
//									    serializer.text(String.valueOf(user.getCity()));
//									    serializer.endTag("", "ville");
//									    serializer.startTag("", "telephone");
//									    serializer.text(String.valueOf(user.getPhone()));
//									    serializer.endTag("", "telephone");
//								    serializer.endTag("", "expediteur");
//								    }
//								}
//						    }
							if(packetsToScan.size() > 0)
							{
								Packet packet = null;
								int a=1;
								Iterator h = packetsToScan.iterator();
						    serializer.startTag("", "colis");
						    serializer.startTag("", "nombre");
							//packetNombre = packet.getSize().toString();
						    serializer.text(String.valueOf(packetsToScan.size()));
						    serializer.endTag("", "nombre");
						    while(h.hasNext())
							{

							packet = (Packet)h.next();
							    //GESTION DE PLUSIEURS PAQUETS PAR COLIS
							    serializer.startTag("", "paquet");
								    serializer.startTag("", "code_barre");
								    serializer.text(String.valueOf(packet.getBarcode()));								    
								    serializer.endTag("", "code_barre");
								    serializer.startTag("", "taille");
								    serializer.text(String.valueOf(packet.getSize()));
								    serializer.endTag("", "taille");
								    serializer.startTag("", "poids");
								    serializer.text(String.valueOf(packet.getWeight()));
								    serializer.endTag("", "poids");
							    serializer.endTag("", "paquet");
							}
						   serializer.endTag("", "colis");
							}
						    // FIN DE GESTION DE PLUSIEURS PAQUETS PAR COLIS
							if (userReceveirTogetAway.size()>0){
								User user = null;
						    	Iterator k = userReceveirTogetAway.iterator();
						    	while(k.hasNext())
								{
						    		user = (User)k.next();
						    		 if (user.getTypeUser().equals(Categories.Types.type_user.RECEIVER)){
								    serializer.startTag("", "destinataire");
									    serializer.startTag("", "nom");
									    serializer.text(String.valueOf(user.getName()));
									    serializer.endTag("", "nom");
									    serializer.startTag("", "rue");
									    serializer.text(String.valueOf(user.getAddress()));
									    serializer.endTag("", "rue");
									    serializer.startTag("", "cp");
									    serializer.text(String.valueOf(user.getZipCode()));
									    serializer.endTag("", "cp");
									    serializer.startTag("", "ville");
									    serializer.text(String.valueOf(user.getCity()));
									    serializer.endTag("", "ville");
									    serializer.startTag("", "telephone");
									    serializer.text(String.valueOf(user.getPhone()));
									    serializer.endTag("", "telephone");
								    serializer.endTag("", "destinataire");
							    serializer.endTag("", "livraison");	
									}
								}
					    	}
						}
					}
				}
				    serializer.endTag("", "tournee");
				    serializer.endDocument();
				    serializer.flush();
				    fileos.close();
					// popup surgissant pour le résultat
					Toast.makeText(context, "Sauvegarde des données - OK", Toast.LENGTH_SHORT).show();
           		} catch (Exception e) {
						Toast.makeText(context, "Sauvegarde des données - ECHEC", Toast.LENGTH_SHORT)
						.show();
						}
			ReadSettings(context);
		}
		catch (Exception e) {
			Toast.makeText(context, "Sauvegarde des données - ECHEC", Toast.LENGTH_SHORT)
			.show();
			}
	}
	
	
	// TESTER la lecture de la génération du fichier 
	public void ReadSettings(Context context){ 
        FileInputStream fIn = null; 
        InputStreamReader isr = null; 
 
        char[] inputBuffer = new char[255]; 
        String data = null; 
 
        try{ 
         fIn = context.openFileInput("NewXML.xml");       
            isr = new InputStreamReader(fIn); 
            isr.read(inputBuffer); 
            data = new String(inputBuffer); 
           //affiche le contenu de mon fichier dans un popup surgissant
            Toast.makeText(context, " "+data,Toast.LENGTH_SHORT).show(); 
            } 
            catch (Exception e) {       
                      Toast.makeText(context, "Lecture des données - ECHEC",Toast.LENGTH_SHORT).show(); 
            } 
            /*finally { 
               try { 
                      isr.close(); 
                      fIn.close(); 
                      } catch (IOException e) { 
                        Toast.makeText(context, "Settings not read",Toast.LENGTH_SHORT).show(); 
                      } 
            } */
       }

	// Methode qui va permettre de d'appeler la méthode lire le texte , pour le texte entre ces deux balise
	private String Read(XmlPullParser parser, String balise) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, balise);
		String texte = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, balise);
		return texte;
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
				case XmlPullParser.END_TAG:
					depth--;
					break;
				case XmlPullParser.START_TAG:
					depth++;
					break;
			}
		}
	}
	}


