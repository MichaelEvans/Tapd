package com.tappd.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem implements Parcelable{
	private int id;
	private int quantity;
	private Item item;

    protected OrderItem(Parcel in) {
        id = in.readInt();
        quantity = in.readInt();
        item = in.readParcelable(Item.class.getClassLoader());
    }

    public OrderItem() {
		// TODO Auto-generated constructor stub
	}

	public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(quantity);
        dest.writeParcelable(item, flags);
    }

    public static final Parcelable.Creator<OrderItem> CREATOR = new Parcelable.Creator<OrderItem>() {
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}