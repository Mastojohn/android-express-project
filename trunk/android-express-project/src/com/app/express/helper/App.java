package com.app.express.helper;

import java.sql.SQLException;

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
	private static Round currentRound;
	
	public static Round getCurrendRound(){
		if(currentRound != null){
			try {
				// Refresh before returns.
				dbHelper.getRoundDao().refresh(currentRound);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return currentRound;
		}else{
			//TODO Récupérer la dernière tournée en bdd avec check de la base du jour.
			return null;
		}
	}
	
	public static void setCurrentRound(Round round){
		currentRound = round;
	}
}
