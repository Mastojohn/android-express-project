package com.app.express.activity;

import roboguice.activity.RoboActivity;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.app.express.R;
import com.app.express.db.DatabaseHelper;
import com.app.express.db.persistence.Delivery;
import com.app.express.helper.App;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.server.erp.Erp;

public class NextDelivery extends OrmLiteBaseActivity<DatabaseHelper> {
	private DatabaseHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		App.context = getApplicationContext();
		
		this.dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		
		setContentView(R.layout.activity_next_delivery);
		
		// Get the round xml file from the ERP. (Simulation)
		StringBuffer rounds = Erp.getRoundsByUser(this);
		if(rounds != null){
			// Rounds are available. Display content.
			Toast.makeText(this, rounds.toString(), Toast.LENGTH_LONG)
					.show();
		}
		
		try {
			Delivery delivery = new Delivery(dbHelper.getDeliveryDao(), 3, 1, 3);
			delivery.create();
			Toast.makeText(this, "ID généré : "+Integer.toString(delivery.getDeliveryId()), Toast.LENGTH_LONG).show();
		} catch(SQLException e){
			Log.e(NextDelivery.class.getName(), "Erreur SQL."+e.getMessage(), e);
			Toast.makeText(this, "Erreur SQL : "+e.getMessage(), Toast.LENGTH_LONG).show();
		} catch(Exception e){
			Log.e(NextDelivery.class.getName(), "Erreur."+e.getMessage(), e);
			Toast.makeText(this, "Erreur : "+e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.next_delivery, menu);
		return true;
	}

}
