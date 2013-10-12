package com.tappd.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.internal.ar;
import com.google.android.gms.plus.model.people.Person;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.squareup.picasso.Picasso;
import com.tappd.R;
import com.tappd.adapter.OrdersAdapter;
import com.tappd.loader.PurchasesLoader;
import com.tappd.model.Order;
import com.tappd.widget.RoundedImageView;

public class PurchasesFragment extends ListFragment implements LoaderCallbacks<List<Order>>{
	ImageView profileImage;
	TextView userNameView;
	TextView emailView;
	String userName;
	String profilePicUrl;
	String email;
	
	private OrdersAdapter mAdapter;
	@Override 
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		userName = args.getString("username");
		profilePicUrl = args.getString("profile_image");
		email = args.getString("email");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_purchase_history,
				container, false);
		profileImage = (ImageView) rootView.findViewById(R.id.profile_pic);
		userNameView = (TextView) rootView.findViewById(R.id.user_name);
		emailView = (TextView) rootView.findViewById(R.id.email);
		
		userNameView.setText(userName);
		emailView.setText(email);
		Picasso.with(getActivity()).load(profilePicUrl).into(profileImage);
		
		
		getLoaderManager().initLoader(0, null, this);
		return rootView;
	}

	@Override
	public Loader<List<Order>> onCreateLoader(int id, Bundle args) {
		return new PurchasesLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Order>> loader, List<Order> orders) {
		mAdapter = new OrdersAdapter(getActivity(), orders);
		//SwingBottomInAnimationAdapter swingInAdapter = new SwingBottomInAnimationAdapter(mAdapter);
		getListView().setAdapter(mAdapter);
	}

	@Override
	public void onLoaderReset(Loader<List<Order>> arg0) {
	}

	public static PurchasesFragment newInstance(String username, String profileUrl) {
		PurchasesFragment fragment = new PurchasesFragment();
		Bundle args = new Bundle();
		args.putString("username", username);
		args.putString("profile_image", profileUrl);
		fragment.setArguments(args);
		return fragment;
	}

	public static PurchasesFragment newInstance(Person currentPerson, String name) {
		PurchasesFragment fragment = new PurchasesFragment();
		Bundle args = new Bundle();
		args.putString("username", currentPerson.getName().getGivenName() + " " +currentPerson.getName().getFamilyName());
		String url = currentPerson.getImage().getUrl();
		args.putString("email", name);
		url = url.substring(0,  url.indexOf("?"));
		args.putString("profile_image", url);
		fragment.setArguments(args);
		return fragment;
	}
}
