package com.tappd.fragments;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tappd.loader.OrderLoader;
import com.tappd.model.*;

import com.tappd.R;

public class OrderFragment extends Fragment implements LoaderCallbacks<Order>{
	TextView textView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_order_layout, container, false);
		textView = (TextView) view.findViewById(R.id.price);
		return view;
	}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
	}

	@Override
	public Loader<Order> onCreateLoader(int id, Bundle args) {
		return new OrderLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Order> loader, Order order) {
		textView.setText(String.valueOf(order.getPrice()));
	}

	@Override
	public void onLoaderReset(Loader<Order> arg0) {
		
	}

	public static OrderFragment newInstance() {
		return new OrderFragment();
	}

}
