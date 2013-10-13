package com.tappd;

import java.text.NumberFormat;
import java.util.List;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tappd.model.Order;
import com.tappd.model.OrderItem;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class RecieptActivity extends Activity {
	String payKey;
	Order mOrder;
	private static AsyncHttpClient client = new AsyncHttpClient();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reciept);

		mOrder = getIntent().getParcelableExtra("order");
		if(getIntent().hasExtra("pay_key")){
			payKey = getIntent().getStringExtra("pay_key");
			Log.e("RECIEPT", mOrder.getId() + " " + payKey);
			RequestParams params = new RequestParams();
			params.put("state_event", "submit");
			params.put("business_id", "1");
			params.put("user_id", "3");
			params.put("paypal_pay_key", payKey);
			client.put("http://tapdservice.herokuapp.com/orders/" + mOrder.getId(), params, new JsonHttpResponseHandler());
		}
		
		TextView priceView = (TextView) findViewById(R.id.price);
		
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reciept, menu);
		return true;
	}

}
