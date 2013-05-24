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
	
	/**
	 * Check if the round stored is set. If it's return it after refresh from the db.
	 * If is not, try to get it from the db, get the round for today if exists. If don't, return null.
	 * 
	 * @return Round|null
	 */
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
	
	/**
	 * Set the current round.
	 * 
	 * @param round
	 */
	public static void setCurrentRound(Round round){
		currentRound = round;
	}
	
	/**
	 * Check if the round is set.
	 * 
	 * @return boolean
	 */
	public static boolean currentRoundSet(){
		return !(currentRound == null);
	}
}
