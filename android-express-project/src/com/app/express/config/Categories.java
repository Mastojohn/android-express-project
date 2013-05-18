package com.app.express.config;

/**
 * Contains all STATICS type who can be changed.
 * Already exists in the database.
 * 
 * @author Ambroise
 */
public abstract class Categories {
	public abstract class Types {
		public abstract class TypeDelivery {
			/**
			 * Livraison
			 */
			public static final String DELIVERY = "Livraison";
			
			/**
			 * Récupération
			 */
			public static final String RECOVERY = "Récupération";
		}
	}
}
