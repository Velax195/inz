package com.kszych.getdata.utils;

import android.content.Context;

/**
 * Created by kszyc on 30.10.2017.
 */

public class Part {

    public static final int DEFAULT_INT = -1;
    public static final String DEFAULT_STRING = null;

    private int mId = DEFAULT_INT;
    private String mName;
    private int mBuyUrl = DEFAULT_INT;
    private double mPrice = (double) DEFAULT_INT;



    private String mProducerName = DEFAULT_STRING;
    private String mAdditionalInfo = DEFAULT_STRING;

    public Part(int id, String name, int buyUrl, double price, String producerName, String additionalInfo){
        this.mId = id;
        this.mName = name;
        this.mBuyUrl = buyUrl;
        this.mPrice = price;
        this.mProducerName = producerName;
        this.mAdditionalInfo = additionalInfo;
    }

    public Part (String name, int buyUrl, double price, String producerName, String additionalInfo){
        this(DEFAULT_INT, name, buyUrl, price, producerName, additionalInfo);
    }
    public int getId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public int getmBuyUrl() {
        return mBuyUrl;
    }

    public double getmPrice() {
        return mPrice;
    }

    public String getmProducerName() {
        return mProducerName;
    }

    public String getmAdditionalInfo() {
        return mAdditionalInfo;
    }

    public void save(Context context) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        helper.save(this);
    }
}
