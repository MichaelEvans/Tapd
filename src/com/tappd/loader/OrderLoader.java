package com.tappd.loader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tappd.model.Item;
import com.tappd.model.Order;
import com.tappd.model.OrderItem;

public class OrderLoader extends AsyncTaskLoader<Order> {

    Order mOrder;
    Gson mGson = new Gson();
    String orderId;
    public OrderLoader(Context context) {
        super(context);

        // Retrieve the package manager for later use; note we don't
        // use 'context' directly but instead the save global application
        // context returned by getContext().
        
    }

    public OrderLoader(Context context, String string) {
		super(context);
		orderId = string;
	}

	/**
     * This is where the bulk of our work is done.  This function is
     * called in a background thread and should generate a new set of
     * data to be published by the loader.
     */
    @Override public Order loadInBackground() {
        // Retrieve all known applications.
        

        final Context context = getContext();
        Order order = null;
        // Create corresponding array of entries and load their labels.
        if(orderId == null)
        	orderId = "1";
        Log.e("FETCH", "http://tapdservice.herokuapp.com/orders/current");
        HttpRequest request = HttpRequest.get("http://tapdservice.herokuapp.com/orders/current");
        //HttpRequest request = HttpRequest.get("http://tapdservice.herokuapp.com/orders/" + orderId);
		int response = request.code();
        if(response == 200){
        	String body = request.body();
        	try {
				JSONObject obj = new JSONObject(body);
				Order o = new Order();
				o.setCreatedAt(obj.getLong("created_at"));
				o.setPrice(obj.getDouble("total"));
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
				Log.e("TAGO", "" + o.getPrice());
				return o;
			} catch (JSONException e) {
				
			}
        	
        }
        Log.e("fetch", "" + (order == null));
        // Done!
        return order;
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override public void deliverResult(Order order) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (order != null) {
                onReleaseResources(order);
            }
        }
        Order oldOrder = order;
        mOrder = order;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(order);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldOrder != null) {
            onReleaseResources(oldOrder);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override protected void onStartLoading() {
        if (mOrder != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mOrder);
        }

        if (takeContentChanged() || mOrder == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override public void onCanceled(Order order) {
        super.onCanceled(order);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(order);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (mOrder != null) {
            onReleaseResources(mOrder);
            mOrder = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(Order order) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }
}