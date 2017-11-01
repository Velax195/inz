package com.kszych.getdata.utils;

import android.content.Context;

public class Package {

    private int mId = DatabaseHelper.DEFAULT_INT;
    private String mRfidTag;
    private int mMass = DatabaseHelper.DEFAULT_INT;
    private int mDimH = DatabaseHelper.DEFAULT_INT;
    private int mDimW = DatabaseHelper.DEFAULT_INT;
    private int mDimD = DatabaseHelper.DEFAULT_INT;
    private String mAdditionalText = DatabaseHelper.DEFAULT_STRING;
    private String mBarcode = DatabaseHelper.DEFAULT_STRING;
    private String mAisle = DatabaseHelper.DEFAULT_STRING;
    private int mRack = DatabaseHelper.DEFAULT_INT;
    private int mShelf = DatabaseHelper.DEFAULT_INT;

    public Package(int id, String rfidTag, int mass, int dimH, int dimW, int dimD
            , String additionalText, String barcode, String aisle, int rack, int shelf) {
        this.mId = id;
        this.mRfidTag = rfidTag;
        this.mMass = mass;
        this.mDimH = dimH;
        this.mDimW = dimW;
        this.mDimD = dimD;
        this.mAdditionalText = additionalText;
        this.mBarcode = barcode;
        this.mAisle = aisle;
        this.mRack = rack;
        this.mShelf = shelf;

    }

    public Package(String rfidTag, int mass, int dimH, int dimW, int dimD
            , String additionalText, String barcode, String aisle, int rack, int shelf) {
        this(DatabaseHelper.DEFAULT_INT, rfidTag, mass, dimH, dimW, dimD, additionalText, barcode
                , aisle, rack, shelf);
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

    public String getAisle() {
        return mAisle;
    }

    public int getRack() {
        return mRack;
    }

    public int getShelf() {
        return mShelf;
    }

    public void save(Context context) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        helper.save(this);
    }
}
