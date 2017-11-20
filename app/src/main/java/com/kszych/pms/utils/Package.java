package com.kszych.pms.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kszych.pms.R;

public class Package implements Parcelable {

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

    public Package(int id, String rfidTag) {
        this.mId = id;
        this.mRfidTag = rfidTag;
    }

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

    public Package(Parcel in) {
        String[] data = new String[11];

        in.readStringArray(data);

        this.mId = Integer.parseInt(data[0]);
        this.mRfidTag = data[1];
        this.mMass = Integer.parseInt(data[2]);
        this.mDimH = Integer.parseInt(data[3]);
        this.mDimW = Integer.parseInt(data[4]);
        this.mDimD = Integer.parseInt(data[5]);
        this.mAdditionalText = data[6];
        this.mBarcode = data[7];
        this.mAisle = data[8];
        this.mRack = Integer.parseInt(data[9]);
        this.mShelf = Integer.parseInt(data[10]);
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

    @Nullable
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                Integer.toString(this.mId)
                , this.mRfidTag
                , Integer.toString(this.mMass)
                , Integer.toString(this.mDimH)
                , Integer.toString(this.mDimW)
                , Integer.toString(this.mDimD)
                , this.mAdditionalText
                , this.mBarcode
                , this.mAisle
                , Integer.toString(this.mRack)
                , Integer.toString(this.mShelf)
        });
    }

    public String getDimensionsString(@NonNull String separator, @Nullable String append, int... args) {
        boolean isNull = true;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length && isNull; i++) {
            if(args[i] != DatabaseHelper.DEFAULT_INT) {
                isNull = false;
            }

            if(i != 0) {
                builder.append(separator);
            }
            builder.append(args[i] == DatabaseHelper.DEFAULT_INT ? "0" : Integer.toString(args[i]));
        }
        if(append != null) {
            builder.append(append);
        }

        if(isNull) {
            return DatabaseHelper.NULL_VAL;
        }

        return builder.toString();
    }

    public String getLocationString(@Nullable String aisle, int rack, int shelf) {
        if(aisle == DatabaseHelper.DEFAULT_STRING
                && rack == DatabaseHelper.DEFAULT_INT
                && shelf == DatabaseHelper.DEFAULT_INT) {
            return DatabaseHelper.NULL_VAL;
        }

        return R.string.label_aisle + " "
                + (aisle == DatabaseHelper.DEFAULT_STRING ? "?" : aisle) + ": R"
                + (rack == DatabaseHelper.DEFAULT_INT ? "?" : rack) + " P"
                + (shelf == DatabaseHelper.DEFAULT_INT ? "?" : shelf);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Package>() {
        @Override
        public Package createFromParcel(Parcel parcel) {
            return new Package(parcel);
        }

        @Override
        public Package[] newArray(int size) {
            return new Package[size];
        }
    };
}
