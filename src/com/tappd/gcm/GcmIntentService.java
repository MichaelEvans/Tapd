package com.tappd.gcm;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tappd.MainActivity;
import com.tappd.OrderActivity;
import com.tappd.R;
import com.tappd.model.Order;

public class GcmIntentService extends IntentService {
	String TAG = "GCMINTENTSERVICE";
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that GCM
			 * will be extended in the future with new message types, just ignore
			 * any message types you're not interested in, or that you don't
			 * recognize.
			 */
			if (GoogleCloudMessaging.
					MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " +
						extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				// This loop represents the service doing some work.
				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				// Post notification of received message.
				Log.i(TAG, "Received: " + extras.getString("order"));
				sendNotification(extras.getString("order"));
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager)
				this.getSystemService(Context.NOTIFICATION_SERVICE);

		Gson gson = new Gson();
//		JSONObject obj = new JSONObject(msg);
//		Log.e("obj", "" + obj.get("id"));
//		JSONObject obj;
		try {
			JSONObject obj = new JSONObject(msg);
			Log.e("obj", "" + obj.getJSONObject("order"));
			Log.e("obj", "" + obj.getJSONObject("order").getDouble("total"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("exeception", e.getMessage());
		}
		JsonObject obj = new JsonParser().parse(msg).getAsJsonObject();
		Order order = gson.fromJson(obj.get("order").getAsJsonObject(), Order.class);
		Intent intent = new Intent(this, OrderActivity.class);
		Log.e("PRICE", "TEST: " + order.getPrice());
//		SharedPreferences sp = getSharedPreferences(MainActivity.class.getSimpleName(),
//				Context.MODE_PRIVATE);
//		sp.edit().putString("order", msg).commit();
		intent.putExtra("order", order);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.common_signin_btn_icon_dark)
		.setContentTitle("GCM Notification")
		.setStyle(new NotificationCompat.BigTextStyle()
		.bigText(msg))
		.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

}