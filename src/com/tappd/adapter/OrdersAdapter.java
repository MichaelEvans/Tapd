package com.tappd.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haarman.listviewanimations.ArrayAdapter;
import com.squareup.picasso.Picasso;
import com.tappd.R;
import com.tappd.model.Order;

public class OrdersAdapter extends ArrayAdapter<Order> {
	private Context mContext;

	public OrdersAdapter(Context context, List<Order> orders) {
		super(orders);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_order, parent, false);
		}

		Order order = getItem(position);
		
		
		ImageView profilePic = (ImageView) convertView.findViewById(R.id.profile_pic);
		TextView ordererName = (TextView) convertView.findViewById(R.id.user_name);
		TextView restaurantName = (TextView) convertView.findViewById(R.id.restaurant);
		
		Picasso.with(mContext).load(order.getUser().getImageUrl()).into(profilePic);
		ordererName.setText(order.getUser().getFirstName() + " " + order.getUser().getLastName());
		restaurantName.setText(order.getRestarauntName());
		
		return convertView;
	}
}
