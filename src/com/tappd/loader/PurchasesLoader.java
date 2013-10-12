package com.tappd.loader;

import java.lang.reflect.Type;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.tappd.model.Order;

public class PurchasesLoader extends AsyncTaskLoader<List<Order>> {
	List<Order> mOrders;
	Gson mGson = new Gson();
	public PurchasesLoader(Context context) {
		super(context);

		// Retrieve the package manager for later use; note we don't
		// use 'context' directly but instead the save global application
		// context returned by getContext().

	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override 
	public List<Order> loadInBackground() {
		// Retrieve all known applications.


		final Context context = getContext();
		List<Order> order = null;
		// Create corresponding array of entries and load their labels.
		HttpRequest request = HttpRequest.get("http://tapdservice.herokuapp.com/users/1/orders?count=20");
		int response = request.code();
		if(response == 200){
			String body = request.body();
			JsonObject obj = new JsonParser().parse(body).getAsJsonObject();
			
			Type listType = new TypeToken<List<Order>>(){}.getType();
			order = mGson.fromJson(obj.get("orders").getAsJsonArray(), listType);
		}
		// Done!
		return order;
	}

	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override 
	public void deliverResult(List<Order> order) {
		if (isReset()) {
			// An async query came in while the loader is stopped.  We
			// don't need the result.
			if (order != null) {
				onReleaseResources(order);
			}
		}
		List<Order> oldOrder = order;
		mOrders = order;

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
	@Override 
	protected void onStartLoading() {
		if (mOrders != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(mOrders);
		}

		if (takeContentChanged() || mOrders == null) {
			// If the data has changed since the last time it was loaded
			// or is not currently available, start a load.
			forceLoad();
		}
	}

	/**
	 * Handles a request to stop the Loader.
	 */
	@Override 
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	/**
	 * Handles a request to cancel a load.
	 */
	@Override public void onCanceled(List<Order> order) {
		super.onCanceled(order);

		// At this point we can release the resources associated with 'apps'
		// if needed.
		onReleaseResources(order);
	}

	/**
	 * Handles a request to completely reset the Loader.
	 */
	@Override 
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		// At this point we can release the resources associated with 'apps'
		// if needed.
		if (mOrders != null) {
			onReleaseResources(mOrders);
			mOrders = null;
		}
	}

	/**
	 * Helper function to take care of releasing resources associated
	 * with an actively loaded data set.
	 */
	protected void onReleaseResources(List<Order> order) {
		// For a simple List<> there is nothing to do.  For something
		// like a Cursor, we would close it here.
	}

}
