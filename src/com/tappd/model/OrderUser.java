package com.tappd.model;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderUser implements Parcelable{
	@SerializedName("first_name")
	private String firstName;
	@SerializedName("last_name")
	private String lastName;
	@SerializedName("image_url")
	private String imageUrl;
	private String email;

	private OrderUser(Parcel in) {
		firstName = in.readString();
		lastName = in.readString();
		imageUrl = in.readString();
		email = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(imageUrl);
		dest.writeString(email);
	}

	public static final Parcelable.Creator<OrderUser> CREATOR = new Parcelable.Creator<OrderUser>() {
        public OrderUser createFromParcel(Parcel in) {
            return new OrderUser(in);
        }

        public OrderUser[] newArray(int size) {
            return new OrderUser[size];
        }
    };

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}