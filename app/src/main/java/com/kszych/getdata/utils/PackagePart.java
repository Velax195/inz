package com.kszych.getdata.utils;

import android.content.Context;

public class PackagePart {

    private int mId = DatabaseHelper.DEFAULT_INT;

    private int mPackageId = DatabaseHelper.DEFAULT_INT;
    private int mPartId = DatabaseHelper.DEFAULT_INT;

    public PackagePart( int id, int packageId, int partId) {
        this.mId=id;
        this.mPackageId=packageId;
        this.mPartId=partId;
    }

    public PackagePart( int packageId, int partId){
        this(DatabaseHelper.DEFAULT_INT, packageId, partId);
    }

    public int getId() {
        return mId;
    }

    public int getPackageId() {
        return mPackageId;
    }

    public int getPartId() {
        return mPartId;
    }

    public void save(Context context) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        helper.save(this);
    }

}
