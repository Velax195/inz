package com.kszych.getdata.utils;

import android.content.Context;

/**
 * Created by kszyc on 30.10.2017.
 */

public class PackagePart {

    public static final int DEFAULT_INT = -1;

    private int mId = DEFAULT_INT;

    private int mPackageId = DEFAULT_INT;
    private int mPartId = DEFAULT_INT;

    public PackagePart( int id, int packageId, int partId) {
        this.mId=id;
        this.mPackageId=packageId;
        this.mPartId=partId;
    }

    public PackagePart( int packageId, int partId){
        this(DEFAULT_INT, packageId, partId);
    }

    public int getId() {
        return mId;
    }

    public int getmPackageId() {
        return mPackageId;
    }

    public int getmPartId() {
        return mPartId;
    }

    public void save(Context context) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        helper.save(this);
    }

}
