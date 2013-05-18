package com.app.express.activity;

import java.util.List;

import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.app.express.R;
import com.app.express.db.DatabaseHelper;
import com.app.express.db.dao.DelivererDao;
import com.app.express.db.dao.DeliveryDao;
import com.app.express.db.persistence.Deliverer;
import com.app.express.db.persistence.Delivery;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.server.erp.Erp;

public class NextDelivery extends OrmLiteBaseActivity<DatabaseHelper> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next_delivery);
		
		// Get the round xml file from the ERP. (Simulation)
		StringBuffer rounds = Erp.getRoundsByUser(this);
		if(rounds != null){
			// Rounds are available. Display content.
			Toast.makeText(this, rounds.toString(), Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.next_delivery, menu);
		return true;
	}

}
