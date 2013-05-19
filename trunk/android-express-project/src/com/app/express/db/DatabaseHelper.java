package com.app.express.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.app.express.R;
import com.app.express.activity.NextDelivery;
import com.app.express.db.dao.DelivererDao;
import com.app.express.db.persistence.Deliverer;
import com.app.express.db.persistence.Delivery;
import com.app.express.helper.App;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 * 
 * @author Ambroise
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	/************************************************
	 * Suggested Copy/Paste code. Everything from here to the done block.
	 ************************************************/

	private static final String DATABASE_NAME = "express.db";
	private static final int DATABASE_VERSION = 8;

	private Dao<Deliverer, Integer> delivererDao;
	private Dao<Delivery, Integer> clickDao;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/************************************************
	 * Suggested Copy/Paste Done
	 ************************************************/

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {		
		try {
			TableUtils.createTable(connectionSource, Deliverer.class);
			TableUtils.createTable(connectionSource, Delivery.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Impossible de créer les tables de la BDD.", e);
		}
		
		// ---------------------------- SAMPLES SOURCE CODE ---------------------------
		
		// ADD DELIVERER with intern DAO.
//		try {
//			Delivery delivery = new Delivery(getHelper().getDeliveryDao(), 2, 1, 3);
//			delivery.create();
//			Toast.makeText(this, "ID généré : "+Integer.toString(delivery.getDeliveryId()), Toast.LENGTH_LONG).show();
//		} catch(SQLException e){
//			Log.e(NextDelivery.class.getName(), "Erreur SQL."+e.getMessage(), e);
//			Toast.makeText(this, "Erreur SQL : "+e.getMessage(), Toast.LENGTH_LONG).show();
//		} catch(Exception e){
//			Log.e(NextDelivery.class.getName(), "Erreur."+e.getMessage(), e);
//			Toast.makeText(this, "Erreur : "+e.getMessage(), Toast.LENGTH_LONG).show();
//		}
		
		// Add DELIVERER with extern DAO.
//		Delivery deliverer = new Delivery(1, 1, 2);
//		Dao<Delivery, Integer> dao;
//		try {
//			dao = getHelper().getDeliveryDao();
//			dao.createIfNotExists(deliverer);
//			
//			List<Delivery> deliverer2 = dao.queryForAll();
//			Toast.makeText(this, "Taille : "+Integer.toString(deliverer2.size()), Toast.LENGTH_LONG).show();
//			
//		} catch(SQLException e){
//			Log.e(NextDelivery.class.getName(), "Erreur SQL."+e.getMessage(), e);
//			Toast.makeText(this, "Erreur SQL : "+e.getMessage(), Toast.LENGTH_LONG).show();
//		} catch(Exception e){
//			Log.e(NextDelivery.class.getName(), "Erreur."+e.getMessage(), e);
//			Toast.makeText(this, "Erreur : "+e.getMessage(), Toast.LENGTH_LONG).show();
//		}
		
		// GET DELIVERER BY ID
//		try {
//			Dao<Deliverer, Integer> dao = getHelper().getDelivererDao();
//			Deliverer deliverer2 = dao.queryForId(789);
//			Toast.makeText(this, "ID: "+Integer.toString(deliverer2.getDelivererId()), Toast.LENGTH_LONG).show();
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}catch (Exception e) {
//			Toast.makeText(this, "Impossible de récupérer l'utilisateur 789.", Toast.LENGTH_LONG)
//			.show();
//		}
		

		// Create and get all deliveries.
//		Delivery delivery = new Delivery(1, 1, 1);
//		Dao<Delivery, Integer> dao;
//		try {
//			dao = getHelper().getDeliveryDao();
//			dao.createIfNotExists(delivery);
//			List<Delivery> deliverer2 = dao.queryForAll();
//			Toast.makeText(this, "Taille : "+Integer.toString(deliverer2.size()), Toast.LENGTH_LONG).show();
//			
//		} catch (Exception e) {
//			
//		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, Deliverer.class, true);
			TableUtils.dropTable(connectionSource, Delivery.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Impossible de passer de la version " + oldVer + " à "
					+ newVer +".", e);
		}
	}
	
	public Dao<Deliverer, Integer> getDelivererDao() throws SQLException {
		if (delivererDao == null) {
			delivererDao = getDao(Deliverer.class);
		}
		return delivererDao;
	}

	public Dao<Delivery, Integer> getDeliveryDao() throws SQLException {
		if (clickDao == null) {
			clickDao = getDao(Delivery.class);
		}
		return clickDao;
	}
}
