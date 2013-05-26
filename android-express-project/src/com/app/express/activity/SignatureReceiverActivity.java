package com.app.express.activity;

import com.app.express.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignatureReceiverActivity extends Activity {

	public static final int SIGNATURE_ACTIVITY = 1;
	private int deliveryId = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		//récuperation de l'id de la livraison
//		Bundle extras = getIntent().getExtras();
//		deliveryId = extras.getInt("deliveryId");
		
		//end
		setContentView(R.layout.activity_signature_receiver);
		Button getSignature = (Button) findViewById(R.id.signature);
		getSignature.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(SignatureReceiverActivity.this,SignatureReceiver.class);
				intent.putExtra("deliveryId", deliveryId);
				startActivityForResult(intent, SIGNATURE_ACTIVITY);
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SIGNATURE_ACTIVITY:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				String status = bundle.getString("status");
				if (status.equalsIgnoreCase("done")) {
					Toast toast = Toast
							.makeText(this, "Signature capture successful!",
									Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 105, 50);
					toast.show();
				}
			}
			break;
		}
	}
}
