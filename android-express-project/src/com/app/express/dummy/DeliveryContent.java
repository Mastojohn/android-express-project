package com.app.express.dummy;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.Html;
import android.util.Log;

import com.app.express.config.Categories;
import com.app.express.db.DatabaseHelper;
import com.app.express.db.persistence.Delivery;
import com.app.express.db.persistence.Round;
import com.app.express.db.persistence.Type;
import com.app.express.helper.App;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;

/**
 * Helper class for providing sample content for user interfaces created by Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DeliveryContent {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<DeliveryItem> ITEMS = new ArrayList<DeliveryItem>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, DeliveryItem> ITEM_MAP = new HashMap<String, DeliveryItem>();

	public static void refreshView() {
		if (App.currentRoundSet()) {
			Round round = App.getCurrendRound();
			ForeignCollection<Delivery> deliveries = round.getDeliveries();

			CloseableIterator<Delivery> deliveryIterator = deliveries.closeableIterator();

			try {
				// Foreach deliveries in the round.
				while (deliveryIterator.hasNext()) {
					Delivery delivery = deliveryIterator.next();
					if(!delivery.getDeliveryOver()){
						addItem(new DeliveryItem(delivery));
					}
					
				}
			} finally {
				// Always close the iterator, else the connection from the database
				// isn't destroyed.
				try {
					deliveryIterator.close();
				} catch (SQLException e) {
					Log.e(DeliveryContent.class.getName(), e.getMessage(), e);
				}
			}
		}
	}

	private static void addItem(DeliveryItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.getDeliveryId().toString(), item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class DeliveryItem extends Delivery {
		public String id;

		public DeliveryItem(Delivery delivery) {
			id = delivery.getId();
			deliveryId = delivery.getDeliveryId();
			round = delivery.getRound();
			typeDelivery = delivery.getTypeDelivery();
			priority = delivery.getPriority();
			deliveryOver = delivery.getDeliveryOver();
			receiverAvailable = delivery.getReceiverAvailable();
			signature = delivery.getSignature();
			dateOver = delivery.getDateOver();
			latitude = delivery.getLatitude();
			longitude = delivery.getLongitude();
			users = delivery.getUsers();
			packets = delivery.getPackets();
			sender = delivery.getSender();
			receiver = delivery.getReceiver();
		}

		@Override
		public String toString() {
			String content = "";
			if (getTypeDelivery().equals(Categories.Types.type_delivery.DELIVERY)) {
				content += "Livraison pour " + receiver.getName() + " :\n";
			} else if (getTypeDelivery().equals(Categories.Types.type_delivery.RECOVERY)) {
				content += "Récupération de colis :";
			} else {
				content += "Élément non initialisé, impossible de récupérer ses propriétés.";
			}

			content += "  => " + packets.size() + " colis. ";
			content += receiver.getZipCode() + " " + receiver.getCity() + "\n";

			return content;
		}

		/**
		 * Method called for display content as list into DeliveryListFragment. Internal call via DeliverListArrayAdapter
		 * 
		 * @return String
		 */
		public String displayAsList() {
			String content = "";
			if (getTypeDelivery().equals(Categories.Types.type_delivery.DELIVERY)) {
				content += "<u>Livraison pour " + receiver.getName() + "</u>:<br />";
			} else if (getTypeDelivery().equals(Categories.Types.type_delivery.RECOVERY)) {
				content += "<u>Récupération de colis</u>:<br />";
			} else {
				content += "<b>Élément non initialisé</b>, impossible de récupérer ses propriétés.";
			}
			return content;
		}

		/**
		 * Display the receiver informations as html.
		 * 
		 * @return String
		 */
		public String displayReceiver() {
			String content = "";
			if (getTypeDelivery().equals(Categories.Types.type_delivery.DELIVERY)) {
				content += "<b>Destinataire</b> :<br />";

			} else if (getTypeDelivery().equals(Categories.Types.type_delivery.RECOVERY)) {
				content += "<b>Fournisseur</b> :<br />";
			}
			
			content += receiver.getName() + " (" + (receiver.getPhone().length() > 0 ? receiver.getPhone() : "Pas de téléphone") + ")<br />";

			return content;
		}

		/**
		 * Display the address of the delivery receiver to deliverer as html.
		 * 
		 * @return String
		 */
		public String displayAddress() {
			String content = "";
			if (getTypeDelivery().equals(Categories.Types.type_delivery.DELIVERY)) {
				content += "<b>Adresse de livraison</b> :<br />";
				
			} else if (getTypeDelivery().equals(Categories.Types.type_delivery.RECOVERY)) {
				content += "<b>Adresse du fournisseur</b> :<br />";
			}
			
			content += receiver.getAddress() + "<br />" + (receiver.getAddressDescription() != null && receiver.getAddressDescription().length() > 0 ? receiver.getAddressDescription() : "") + "<br />" + receiver.getZipCode() + " " + receiver.getCity() + "<br />";

			return content;
		}

		/**
		 * Display statistics of the delivery like number of packets, total weight, etc. As html.
		 * 
		 * @return String
		 */
		public String displayStatsDelivery() {
			String content = "<b>Informations supplémentaires</b> :<br />";
			
			if (getTypeDelivery().equals(Categories.Types.type_delivery.DELIVERY)) {
				content += "Il y a <b>"+packets.size()+"</b> colis <b>à livrer</b> au total. <u>Poids total</u>: " + getPacketsWeight() + " Kg.<br />";
				if(packets.size() != countPacketToScan()){
					// The delivery is started, display the number of packets to deliver.
					content += "(Il reste encore <b>"+(packets.size() - countPacketToScan())+"</b> colis <b>à scanner</b>.";
				}
				
				
			} else if (getTypeDelivery().equals(Categories.Types.type_delivery.RECOVERY)) {
				content += "Il y a <b>"+countPacketToScan()+"</b> colis <b>à récupérer</b>. <u>Poids total</u>: " + getPacketsWeight() + " Kg.<br />"; 
			}
			
			return content;
		}

	}
}
