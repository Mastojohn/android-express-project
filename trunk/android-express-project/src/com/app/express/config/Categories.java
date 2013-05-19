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
			 * Récupération
			 */
			public static final String RECOVERY = "Récupération";
		}

		public abstract class type_delivery_state {
			/**
			 * En cours
			 */
			public static final String PENDING = "En cours";

			/**
			 * Refusé
			 */
			public static final String REFUSED = "Refusé";

			/**
			 * Livré
			 */
			public static final String DELIVERED = "Livré";

			/**
			 * Oublié
			 */
			public static final String FORGOTTEN = "Oublié";
		}

		public abstract class type_user {
			/**
			 * Expéditeur
			 */
			public static final String SENDER = "Expéditeur";

			/**
			 * Destinataire
			 */
			public static final String RECEIVER = "Destinataire";
		}
	}
}
