package com.app.express.activity;

import java.util.Date;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.express.R;
import com.app.express.config.Categories;
import com.app.express.db.DatabaseHelper;
import com.app.express.db.persistence.*;
import com.app.express.helper.App;
import com.app.express.helper.Gmap;
import com.app.express.helper.Session;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.server.erp.Erp;

/**
 * Activity which displays a login screen to the user, offering registration as well.
 */
@ContentView(R.layout.activity_login)
public class Login extends RoboActivity {

	/**
	 * Name of the Shared Preference file.
	 */
	public static final String PREFS_NAME = "login";

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	
	//Test redirection 
			private Button button_Scan_Interface;
			
	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	@InjectView(R.id.email)
	private EditText mEmailView;

	@InjectView(R.id.password)
	private EditText mPasswordView;

	@InjectView(R.id.login_form)
	private View mLoginFormView;

	@InjectView(R.id.login_status)
	private View mLoginStatusView;

	@InjectView(R.id.login_status_message)
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize helpers.
		App.context = getApplicationContext();
		App.dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

		// Get settings for auto fill the input email.
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

		// Set up the login form.
		// Put the value referenced by the key lastEmail and empty string in the
		// key don't exists.
		mEmail = settings.getString("lastEmail", "");
		mEmailView.setText(mEmail);

		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		// Autoselect password input.
		if (mEmailView.getText().length() > 0) {
			// Autoselect the input password.
			mPasswordView.requestFocus();
		}

		findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		// Check if GooglePlayServices are available.		
		if(!Gmap.isGoogleMapsInstalled()){
			Gmap.displayError();
		}
		
		// LEO
		
		try{
			 // Create deliverer.
			 Dao<Deliverer, Integer> delivererDao = App.dbHelper.getDelivererDao();
			 Deliverer deliverer = new Deliverer(delivererDao, "léo", "leo@test.fr");
			 deliverer.create();
			 deliverer.refresh();
			 Log.i(NextDelivery.class.getName(), "Livreur ajouté: " + deliverer.toString());
			
			 // Create round.
			 Dao<Round, Integer> roundDao = App.dbHelper.getRoundDao();
			 Round round = new Round(roundDao, deliverer, new Date());
			 round.create();
			 round.refresh();
			 Log.i(NextDelivery.class.getName(), "Tournée ajoutée: " + round.toString());
			
			 // Create delivery.
			 Dao<Delivery, Integer> deliveryDao = App.dbHelper.getDeliveryDao();
			 Delivery delivery = new Delivery(deliveryDao, round, 1);
			 delivery.create();
			 delivery.refresh();
			 Log.i(NextDelivery.class.getName(), "Livraison ajoutée: " + delivery.toString());
			
			 // Create receiver.
			 Dao<User, Integer> receiverDao = App.dbHelper.getUserDao();
			 User receiver = new User(receiverDao, delivery, "Destinataire mathieu", Categories.Types.type_user.RECEIVER, "240 b xxx", "", "13100", "Aix", "");
			 receiver.create();
			 receiver.refresh();
			 Log.i(NextDelivery.class.getName(), "Utilisateur ajouté: " + receiver.toString());
			
			 // Create sender.
			 Dao<User, Integer> senderDao = App.dbHelper.getUserDao();
			 User sender = new User(senderDao, delivery, "Expéditeur paul", Categories.Types.type_user.SENDER, "", "", "", "", "");
			 sender.create();
			 sender.refresh();
			 Log.i(NextDelivery.class.getName(), "Utilisateur ajouté: " + sender.toString());
	
			 // Create packet.
			 Dao<Packet, Integer> packetDao = App.dbHelper.getPacketDao();
			 Packet packet = new Packet(packetDao, delivery, "3057068015986", "150x12x54", 5.1, "En cours");
			 packet.create();
			 packet.refresh();
			 Log.i(NextDelivery.class.getName(), "Colis ajouté: " + packet.toString());
			 
			 // Create packet.
			 Packet packet2 = new Packet(packetDao, delivery, "30004832", "54x12x54", 5.1, "En cours");
			 packet2.create();
			 packet2.refresh();
			 Log.i(NextDelivery.class.getName(), "Colis ajouté: " + packet2.toString());
		 
		}catch(Exception e){
			Log.e("ERROR", "Erreur durant ajout données fake.", e);
		}
		
		button_Scan_Interface = (Button) findViewById(R.id.button_Scan_Interface);
		button_Scan_Interface.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Login.this, Scan.class);
						
		
						// On lance l'Activity
						startActivity(intent);
						
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form. If there are form errors (invalid email, missing fields, etc.), the errors are
	 * presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 3) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				// Simulate network access.
				Thread.sleep(1000);// TODO Comment this line for instant check.
			} catch (InterruptedException e) {
				return false;
			}

			// Simulate ERP call.
			return Erp.checkLogin(mEmail, mPassword);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				// Connect the user.
				Session.doConnect(mEmail);

				// Save the mail into preferences.
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("lastEmail", mEmail);
				editor.commit();

				// Call next activity.
				Intent intent = new Intent(App.context, NextDelivery.class);
				startActivity(intent);

				finish();
			} else {
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
