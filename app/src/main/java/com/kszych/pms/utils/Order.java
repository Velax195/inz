package com.kszych.pms.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable{

    private int mId = DatabaseHelper.DEFAULT_INT;
    private boolean mIsExecuted = DatabaseHelper.DEFAULT_BOOLEAN;
    private int mNumber = DatabaseHelper.DEFAULT_INT;
    private String mDate = DatabaseHelper.DEFAULT_STRING;
    private String mAdditional = DatabaseHelper.DEFAULT_STRING;
    private int mClientId = DatabaseHelper.DEFAULT_INT;

    public Order (int id, boolean isExecuted, int clientId, int number, String date, String additionalInfo){
        this.mId = id;
        this.mIsExecuted = isExecuted;
        this.mClientId = clientId;
        this.mNumber = number;
        this.mDate = date;
        this.mAdditional = additionalInfo;
    }

    public Order (boolean isExecuted, int clientId, int number, String date, String additionalInfo){
        this(DatabaseHelper.DEFAULT_INT, isExecuted, clientId, number, date, additionalInfo);
    }

    public Order(Parcel in) {
        String[] data = new String[6];

        in.readStringArray(data);

        this.mId = Integer.parseInt(data[0]);
        this.mIsExecuted = Boolean.parseBoolean(data[1]);
        this.mClientId = Integer.parseInt(data[2]);
        this.mNumber = Integer.parseInt(data[3]);
        this.mDate = data[4];
        this.mAdditional = data[5];
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

    public int getNumber() {
        return mNumber;
    }

    public String getDate() {
        return mDate;
    }

    public String getAdditional() {
        return mAdditional;
    }


    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                Integer.toString(this.mId)
                ,Boolean.toString(this.mIsExecuted)
                ,Integer.toString(this.mClientId)
                ,Integer.toString(this.mNumber)
                ,this.mDate
                ,this.mAdditional
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
