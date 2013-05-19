package com.app.express.config;

/**
 * Contains all STATICS type who can be changed. Already exists in the database.
 * 
 * @author Ambroise
 */
public abstract class Categories {
	public abstract class Types {
		public abstract class type_delivery {
			/**
			 * Livraison
			 */
			public static final String DELIVERY = "Livraison";

			/**
			 * R�cup�ration
			 */
			public static final String RECOVERY = "R�cup�ration";
		}

		public abstract class type_delivery_state {
			/**
			 * En cours
			 */
			public static final String PENDING = "En cours";

			/**
			 * Refus�
			 */
			public static final String REFUSED = "Refus�";

			/**
			 * Livr�
			 */
			public static final String DELIVERED = "Livr�";

			/**
			 * Oubli�
			 */
			public static final String FORGOTTEN = "Oubli�";
		}

		public abstract class type_user {
			/**
			 * Exp�diteur
			 */
			public static final String SENDER = "Exp�diteur";

			/**
			 * Destinataire
			 */
			public static final String RECEIVER = "Destinataire";
		}
	}
}
