package com.kszych.getdata.utils;

import android.content.Context;

public class Part {

    private int mId = DatabaseHelper.DEFAULT_INT;
    private String mName;
    private int mBuyUrl = DatabaseHelper.DEFAULT_INT;
    private double mPrice = DatabaseHelper.DEFAULT_REAL;

    private String mProducerName = DatabaseHelper.DEFAULT_STRING;
    private String mAdditionalInfo = DatabaseHelper.DEFAULT_STRING;

    public Part(int id, String name, int buyUrl, double price, String producerName, String additionalInfo){
        this.mId = id;
        this.mName = name;
        this.mBuyUrl = buyUrl;
        this.mPrice = price;
        this.mProducerName = producerName;
        this.mAdditionalInfo = additionalInfo;
    }

    public Part (String name, int buyUrl, double price, String producerName, String additionalInfo){
        this(DatabaseHelper.DEFAULT_INT, name, buyUrl, price, producerName, additionalInfo);
    }
    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getBuyUrl() {
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
}
