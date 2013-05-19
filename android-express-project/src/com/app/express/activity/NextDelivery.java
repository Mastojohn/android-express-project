package com.app.express.activity;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.app.express.R;
import com.app.express.db.DatabaseHelper;
import com.app.express.db.persistence.*;
import com.app.express.helper.App;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.server.erp.Erp;

@ContentView(R.layout.activity_next_delivery)
public class NextDelivery extends RoboActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Initialize helpers.
		App.context = getApplicationContext();
		App.dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
				
		// Get the round xml file from the ERP. (Simulation)
		StringBuffer rounds = Erp.getRoundsByUser(this);
		if(rounds != null){
			// Rounds are available. Display content.
			Toast.makeText(this, rounds.toString(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.next_delivery, menu);
		return true;
	}

}
