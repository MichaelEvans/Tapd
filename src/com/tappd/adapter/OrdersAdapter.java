package com.tappd.adapter;

import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tappd.R;
import com.tappd.fancy.ExpandableListItem;
import com.tappd.fancy.ExpandingLayout;
import com.tappd.model.Order;
import com.tappd.model.OrderItem;

public class OrdersAdapter extends ArrayAdapter<ExpandableListItem> {
	private Context mContext;

	public OrdersAdapter(Context context, int layoutViewResourceId, List<ExpandableListItem> orders) {
		super(context, layoutViewResourceId, orders);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ExpandableListItem object = getItem(position);
		
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_order, parent, false);
		}

		Order order = object.getOrder();
		
		TextView priceView = (TextView) convertView.findViewById(R.id.price);
		TextView createdAtView = (TextView) convertView.findViewById(R.id.created_at);
		TextView restaurantName = (TextView) convertView.findViewById(R.id.restaurant);
		restaurantName.setText(order.getRestarauntName());
		NumberFormat format = NumberFormat.getCurrencyInstance();
		priceView.setText(format.format(order.getPrice()));
		CharSequence ago = DateUtils.getRelativeTimeSpanString(order.getCreatedAt()*1000, System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS);
		createdAtView.setText(ago);
		
		TextView orderDetails = (TextView) convertView.findViewById(R.id.order_details);
		List<OrderItem> items = order.getOrderItems();
		StringBuilder builder = new StringBuilder();
		for(OrderItem item : items){
			builder.append("<b>" + item.getItem().getTitle() + " - <i>" + item.getQuantity() + "x</i> " + format.format(item.getItem().getPrice()) + "<br />");
		}
		orderDetails.setText(Html.fromHtml(builder.toString()));
		
		ExpandingLayout expandingLayout = (ExpandingLayout)convertView.findViewById(R.id
                .expanding_layout);
        expandingLayout.setExpandedHeight(object.getExpandedHeight());
        expandingLayout.setSizeChangedListener(object);

        if (!object.isExpanded()) {
            expandingLayout.setVisibility(View.GONE);
        } else {
            expandingLayout.setVisibility(View.VISIBLE);
        }
        
		return convertView;
	}
}
