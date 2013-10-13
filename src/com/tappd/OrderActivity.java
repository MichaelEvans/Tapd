package com.tappd;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.tappd.loader.OrderLoader;
import com.tappd.model.Item;
import com.tappd.model.Order;
import com.tappd.model.OrderItem;

public class OrderActivity extends Activity{
	private Order mOrder;
	private TextView priceView;
	private View mDataView;
	private View mEmpty;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_order_layout);
		
		mEmpty = findViewById(R.id.empty);
		mDataView = findViewById(R.id.data);
		priceView = (TextView) findViewById(R.id.price);
		new OrderFetchTask().execute();
		//mOrder = getIntent().getParcelableExtra("order");
		
//		NumberFormat format = NumberFormat.getCurrencyInstance();
//		priceView.setText(format.format(mOrder.getPrice()));
//		
//		TextView orderDetails = (TextView)findViewById(R.id.order_breakdown);
//		List<OrderItem> items = mOrder.getOrderItems();
//		StringBuilder builder = new StringBuilder();
//		for(OrderItem item : items){
//			builder.append("<b>" + item.getItem().getTitle() + " - <i>" + item.getQuantity() + "x</i> - " + format.format(item.getItem().getPrice()) + "<br />");
//		}
//		orderDetails.setText(Html.fromHtml(builder.toString()));
		
		Intent intent = new Intent(this, PayPalService.class);
		
		// live: don't put any environment extra
		// sandbox: use PaymentActivity.ENVIRONMENT_SANDBOX
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);

		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "AUTRVRByoGSJ4gdlisoVe2vKCQExvC3CcQ5M6wTJdpsmA8tx4ZQugkUcFhfg");

		startService(intent);
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
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_SANDBOX);

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
	                Intent i = new Intent(OrderActivity.this, RecieptActivity.class);
	                i.putExtra("pay_key", confirm.getProofOfPayment().getPaymentIdentifier());
	                i.putExtra("order", mOrder);
	                startActivity(i);
	                finish();
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

	public class OrderFetchTask extends AsyncTask<Void, Void, Order>{

		@Override
		protected Order doInBackground(Void... params) {
			HttpRequest request = HttpRequest.get("http://tapdservice.herokuapp.com/orders/current");
	        //HttpRequest request = HttpRequest.get("http://tapdservice.herokuapp.com/orders/" + orderId);
			
			int response = request.code();
	        if(response == 200){
	        	String body = request.body();
	        	JSONObject obj;
				try {
					obj = new JSONObject(body).getJSONObject("order");
				
				Order o = new Order();
				Log.e("KEYS", obj.toString());
				o.setCreatedAt(obj.getLong("created_at"));
				o.setPrice(obj.getDouble("total"));
				o.setId(obj.getInt("id"));
				o.setRestarauntName(obj.getString("restaurant_name"));
				List<OrderItem> orderItems = new ArrayList<OrderItem>();
				JSONArray array = obj.getJSONArray("order_items");
				for(int i=0;i<array.length();i++){
					JSONObject oio = array.getJSONObject(i);
					OrderItem oi = new OrderItem();
					oi.setQuantity(oio.getInt("quantity"));
					
					JSONObject io = oio.getJSONObject("item");
					Item item = new Item();
					item.setPrice(io.getDouble("price"));
					item.setTitle(io.getString("title"));
					oi.setItem(item);
					orderItems.add(oi);
				}
				o.setOrderItems(orderItems);
	        	Log.e("ITEMs", "" + o.getOrderItems());
	        	return o;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        }
			return null;
		}
		
		protected void onPostExecute(Order result) {
			priceView = (TextView) findViewById(R.id.price);
			
			//mOrder = getIntent().getParcelableExtra("order");
			mOrder = result;
			
			NumberFormat format = NumberFormat.getCurrencyInstance();
			priceView.setText(format.format(mOrder.getPrice()));
			
			TextView orderDetails = (TextView)findViewById(R.id.order_breakdown);
			List<OrderItem> items = mOrder.getOrderItems();
			Log.e("TAG", "" + items);
			StringBuilder builder = new StringBuilder();
			for(OrderItem item : items){
				builder.append("<b>" + item.getItem().getTitle() + " - <i>" + item.getQuantity() + "x</i> - " + format.format(item.getItem().getPrice()) + "<br />");
			}
			orderDetails.setText(Html.fromHtml(builder.toString()));
			mEmpty.setVisibility(View.GONE);
			mDataView.setVisibility(View.VISIBLE);
	     }
		
	}
}
