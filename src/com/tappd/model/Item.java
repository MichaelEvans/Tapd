package com.tappd.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable{
	private int id;
	private double price;
	private String title;

    protected Item(Parcel in) {
        id = in.readInt();
        price = in.readDouble();
        title = in.readString();
    }

    public Item() {
		// TODO Auto-generated constructor stub
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(price);
        dest.writeString(title);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}