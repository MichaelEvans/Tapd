package com.tappd.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Order implements Parcelable{
	private int id;
	private OrderUser user;
	@SerializedName("total")
	private double price;
	@SerializedName("restaurant_name")
	private String restarauntName;
	@SerializedName("order_items")
	private List<OrderItem> orderItems;
	@SerializedName("created_at")
	private long createdAt;
	@SerializedName("restaurant_url")
	private String restaurantUrl;

    protected Order(Parcel in) {
        id = in.readInt();
        user = in.readParcelable(OrderUser.class.getClassLoader());
        price = in.readDouble();
        restarauntName = in.readString();
        orderItems = new ArrayList<OrderItem>();
        in.readTypedList(orderItems, OrderItem.CREATOR);
        createdAt = in.readLong();
        restaurantUrl = in.readString();
    }

    public Order() {
		// TODO Auto-generated constructor stub
	}

	public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(user, flags);
        dest.writeDouble(price);
        dest.writeString(restarauntName);
        dest.writeTypedList(orderItems);
        dest.writeLong(createdAt);
        dest.writeString(restaurantUrl);
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public OrderUser getUser() {
		return user;
	}

	public void setUser(OrderUser user) {
		this.user = user;
	}

	public String getRestarauntName() {
		return restarauntName;
	}

	public void setRestarauntName(String restarauntName) {
		this.restarauntName = restarauntName;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public String getRestaurantUrl() {
		return restaurantUrl;
	}

	public void setRestaurantUrl(String restaurantUrl) {
		this.restaurantUrl = restaurantUrl;
	}
}