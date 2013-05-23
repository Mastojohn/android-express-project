package com.app.express.helper;

import com.app.express.db.DatabaseHelper;
import com.app.express.db.persistence.Round;

import android.content.Context;

/**
 * Helper for the application. Contains some instance of static objects.
 * 
 * @author Ambroise
 */
public class App {
	public static Context context;
	public static DatabaseHelper dbHelper;
	public static Round currentRound;
	
	public static Round getCurrendRound(){
		if(currentRound != null){
			return currentRound;
		}else{
			//TODO Récupérer la dernière tournée en bdd avec check de la base du jour.
			return null;
		}
	}
}
