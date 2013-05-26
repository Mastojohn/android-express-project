package com.app.express.activity;

import com.app.express.R;
import com.app.express.R.layout;
import com.app.express.R.menu;
import com.app.express.helper.IntentIntegrator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CustomerPresence extends Activity {

	private RadioButton rb_here;
	private RadioButton rb_away;
	private RadioGroup rg;
	private Button btn;
	private int delivreryId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_presence);
		
		Bundle extras = getIntent().getExtras();
		delivreryId = extras.getInt("deliveryId");
		
		rb_here =(RadioButton) findViewById(R.id.radioButtPresent);
		rb_away =(RadioButton) findViewById(R.id.radioButtonAbsent);
		addListenerOnButton();
		// Récupération des parametres
		
	}

	private void addListenerOnButton() {
		// TODO Auto-generated method stub
		rg = (RadioGroup) findViewById(R.id.radioClient);
		btn = (Button) findViewById(R.id.button_v);
	 
		btn.setOnClickListener(new OnClickListener() {
	 
			@Override
			public void onClick(View v) {
	 
			        // get selected radio button from radioGroup

			        if (rb_away.isChecked())
			        {
			        	Log.i("test", "absent");
				        Intent intent = new Intent(CustomerPresence.this, RecapDelivryAway.class);					
						intent.putExtra("deliveryId", delivreryId);
						startActivity(intent);
						
			        }
			        if (rb_here.isChecked())
			        {
			        	Log.i("test", "present");
				        Intent intent = new Intent(CustomerPresence.this, RecapDelivry.class);					
						intent.putExtra("deliveryId", delivreryId);
						startActivity(intent);
						
			        }
				
	 
			}
	 
		});
	 
	  }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.customer_presence, menu);
		return true;
	}

}
