package com.kszych.pms.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class Part implements Parcelable {

    private int mId = DatabaseHelper.DEFAULT_INT;
    private String mName;
    private String mBuyUrl = DatabaseHelper.DEFAULT_STRING;
    private double mPrice = DatabaseHelper.DEFAULT_REAL;
    private String mProducerName = DatabaseHelper.DEFAULT_STRING;
    private String mAdditionalInfo = DatabaseHelper.DEFAULT_STRING;

    public Part(int id, String name, String buyUrl, double price, String producerName, String additionalInfo) {
        this.mId = id;
        this.mName = name;
        this.mBuyUrl = buyUrl;
        this.mPrice = price;
        this.mProducerName = producerName;
        this.mAdditionalInfo = additionalInfo;
    }

    public Part(String name, String buyUrl, double price, String producerName, String additionalInfo) {
        this(DatabaseHelper.DEFAULT_INT, name, buyUrl, price, producerName, additionalInfo);
    }

    public Part(Parcel in) {
        String[] data = new String[6];

        in.readStringArray(data);

        this.mId = Integer.parseInt(data[0]);
        this.mName = data[1];
        this.mBuyUrl = data[2];
        this.mPrice = Double.parseDouble(data[3]);
        this.mProducerName = data[4];
        this.mAdditionalInfo = data[5];

    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getBuyUrl() {
        return mBuyUrl;
    }

    public double getPrice() {
        return mPrice;
    }

    public String getProducerName() {
        return mProducerName;
    }

    public String getAdditionalInfo() {
        return mAdditionalInfo;
    }

    public void save(Context context) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        helper.save(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                Integer.toString(this.mId)
                , this.mName
                , this.mBuyUrl
                , Double.toString(this.mPrice)
                , this.mProducerName
                , this.mAdditionalInfo
        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Part>() {
        @Override
        public Part createFromParcel(Parcel parcel) {
            return new Part(parcel);
        }

        @Override
        public Part[] newArray(int size) {
            return new Part[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Part && ((Part) obj).getId() == this.getId();
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(this.getId()).hashCode();
    }
}