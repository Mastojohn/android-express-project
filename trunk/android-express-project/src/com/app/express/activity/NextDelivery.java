package com.app.express.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.app.express.R;
import com.server.erp.Erp;

public class NextDelivery extends Activity {

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
