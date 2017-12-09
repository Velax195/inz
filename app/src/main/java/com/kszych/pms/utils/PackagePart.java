package com.kszych.pms.utils;

import android.content.Context;

public class PackagePart {

    private int mId = DatabaseHelper.DEFAULT_INT;

    private int mPackageId = DatabaseHelper.DEFAULT_INT;
    private int mPartId = DatabaseHelper.DEFAULT_INT;
    private int mQuantity = DatabaseHelper.DEFAULT_INT;

    public PackagePart( int id, int packageId, int partId, int quantity) {
        this.mId=id;
        this.mPackageId=packageId;
        this.mPartId=partId;
        this.mQuantity = quantity;
    }

    public PackagePart( int packageId, int partId, int quantity){
        this(DatabaseHelper.DEFAULT_INT, packageId, partId, quantity);
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

    public int getQuantity() {
        return mQuantity;
    }

    public void save(Context context) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        helper.save(this);
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(this.getId()).hashCode();
    }

}
