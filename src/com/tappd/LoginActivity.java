package com.tappd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;

public class LoginActivity extends Activity implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mPlusClient = new PlusClient.Builder(this, this, this)
		.setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
		.build();

		findViewById(R.id.sign_in_button).setOnClickListener(this);
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");
	}

	@Override
	protected void onStart() {
		super.onStart();
		mPlusClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mPlusClient.disconnect();
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
			if (mConnectionResult == null) {
				mConnectionProgressDialog.show();
			} else {
				try {
					mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					// Try connecting again.
					mConnectionResult = null;
					mPlusClient.connect();
				}
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mConnectionProgressDialog.isShowing()) {
			// The user clicked the sign-in button already. Start to resolve
			// connection errors. Wait until onConnected() to dismiss the
			// connection dialog.
			if (result.hasResolution()) {
				try {
					result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mPlusClient.connect();
				}
			}
		}

		// Save the intent so that we can start an activity when the user clicks
		// the sign-in button.
		mConnectionResult = result;
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// We've resolved any connection errors.
		mConnectionProgressDialog.dismiss();
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
			mConnectionResult = null;
			mPlusClient.connect();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onDisconnected() {
		
	}

}
