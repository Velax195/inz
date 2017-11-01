package com.kszych.getdata.utils;

import android.content.Context;

/**
 * Created by kszyc on 29.10.2017.
 */

public class Package {

    public static final int DEFAULT_INT = -1;
    public static final String DEFAULT_STRING = null;

    private int mId = DEFAULT_INT;
    private String mRfidTag;
    private int mMass = DEFAULT_INT;
    private int mDimH = DEFAULT_INT;
    private int mDimW = DEFAULT_INT;
    private int mDimD = DEFAULT_INT;
    private String mAdditionalText = DEFAULT_STRING;
    private String mBarcode = DEFAULT_STRING;

    public Package(int id, String rfidTag, int mass, int dimH, int dimW, int dimD
            , String additionalText, String barcode) {
        this.mId = id;
        this.mRfidTag = rfidTag;
        this.mMass = mass;
        this.mDimH = dimH;
        this.mDimW = dimW;
        this.mDimD = dimD;
        this.mAdditionalText = additionalText;
        this.mBarcode = barcode;
    }

    public Package(String rfidTag, int mass, int dimH, int dimW, int dimD
            , String additionalText, String barcode) {
        this(DEFAULT_INT, rfidTag, mass, dimH, dimW, dimD, additionalText, barcode);
    }


    public int getId() {
        return mId;
    }

    public String getRfidTag() {
        return mRfidTag;
    }

    public int getMass() {
        return mMass;
    }

    public int getDimH() {
        return mDimH;
    }

    public int getDimW() {
        return mDimW;
    }

    public int getDimD() {
        return mDimD;
    }

    public String getAdditionalText() {
        return mAdditionalText;
    }

    public String getBarcode() {
        return mBarcode;
    }

    public void save(Context context) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        helper.save(this);
    }
}
