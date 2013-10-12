package com.tappd.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Order implements Parcelable{
	private int id;
	private User user;
	private double price;
	@SerializedName("restaurant_name")
	private String restarauntName;
	@SerializedName("order_items")
	private List<OrderItem> orderItems;

    protected Order(Parcel in) {
        id = in.readInt();
        user = (User)in.readValue(null);
        price = in.readDouble();
        restarauntName = in.readString();
        orderItems = new ArrayList<OrderItem>();
        in.readList(orderItems, null);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeValue(user);
        dest.writeDouble(price);
        dest.writeString(restarauntName);
        dest.writeList(orderItems);
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRestarauntName() {
		return restarauntName;
	}

	public void setRestarauntName(String restarauntName) {
		this.restarauntName = restarauntName;
	}
}