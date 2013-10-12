package com.tappd;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.tappd.model.Order;

public class OrderActivity extends Activity{
	private Order mOrder;
	private TextView priceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_order_layout);
		
		priceView = (TextView) findViewById(R.id.price);
		
		mOrder = getIntent().getParcelableExtra("order");
		
		NumberFormat format = NumberFormat.getCurrencyInstance();
		priceView.setText(format.format(mOrder.getPrice()));
		
		Intent intent = new Intent(this, PayPalService.class);
		
		// live: don't put any environment extra
		// sandbox: use PaymentActivity.ENVIRONMENT_SANDBOX
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);

		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "AUTRVRByoGSJ4gdlisoVe2vKCQExvC3CcQ5M6wTJdpsmA8tx4ZQugkUcFhfg");

		startService(intent);
		//getLoaderManager().initLoader(0, null, this);
		findViewById(R.id.pay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBuyPressed(v);
			}
		});
	}

	@Override
	public void onDestroy() {
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}

	public void onBuyPressed(View pressed) {
		PayPalPayment payment = new PayPalPayment(new BigDecimal(mOrder.getPrice()), "USD", mOrder.getRestarauntName());

		Intent intent = new Intent(this, PaymentActivity.class);

		// comment this line out for live or set to PaymentActivity.ENVIRONMENT_SANDBOX for sandbox
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);

		// it's important to repeat the clientId here so that the SDK has it if Android restarts your
		// app midway through the payment UI flow.
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "AUTRVRByoGSJ4gdlisoVe2vKCQExvC3CcQ5M6wTJdpsmA8tx4ZQugkUcFhfg");

		// Provide a payerId that uniquely identifies a user within the scope of your system,
		// such as an email address or user ID.
		intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "michaelcevans10@gmail.com");

		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, "jphillips-facilitator@opthumb.com");
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

		startActivityForResult(intent, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
	    if (resultCode == Activity.RESULT_OK) {
	        PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
	        if (confirm != null) {
	            try {
	                Log.i("paymentExample", confirm.toJSONObject().toString(4));

	                // TODO: send 'confirm' to your server for verification.
	                // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
	                // for more details.

	            } catch (JSONException e) {
	                Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
	            }
	        }
	    }
	    else if (resultCode == Activity.RESULT_CANCELED) {
	        Log.i("paymentExample", "The user canceled.");
	    }
	    else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
	        Log.i("paymentExample", "An invalid payment was submitted. Please see the docs.");
	    }
	}

	@Override 
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);
	    Log.e("new intent", "" + getIntent().getParcelableExtra("order"));
	} 
}
