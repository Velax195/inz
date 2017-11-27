package com.kszych.pms.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable{

    private int mId = DatabaseHelper.DEFAULT_INT;
    private boolean mIsExecuted = DatabaseHelper.DEFAULT_BOOLEAN;
    private int mClientId = DatabaseHelper.DEFAULT_INT;

    public Order (int id, boolean isExecuted, int clientId){
        this.mId = id;
        this.mIsExecuted = isExecuted;
        this.mClientId = clientId;
    }

    public Order (boolean isExecuted, int clientId){
        this(DatabaseHelper.DEFAULT_INT, isExecuted, clientId);
    }

    public Order(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);

        this.mId = Integer.parseInt(data[0]);
        this.mIsExecuted = Boolean.parseBoolean(data[1]);
        this.mClientId = Integer.parseInt(data[2]);
    }

    public int getId() {
        return mId;
    }

    public boolean getIsExecuted() {
        return mIsExecuted;
    }

    public int getClientId() {
        return mClientId;
    }

    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                Integer.toString(this.mId)
                ,Boolean.toString(this.mIsExecuted)
                ,Integer.toString(this.mClientId)
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel parcel) {
            return new Order(parcel);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
