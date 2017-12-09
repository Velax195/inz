package com.kszych.pms.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Client implements Parcelable {

    private int mId = DatabaseHelper.DEFAULT_INT;

    private String mName = DatabaseHelper.DEFAULT_STRING;
    private String mStreet = DatabaseHelper.DEFAULT_STRING;
    private int mNumber = DatabaseHelper.DEFAULT_INT;
    private String mCity = DatabaseHelper.DEFAULT_STRING;
    private int mPhone = DatabaseHelper.DEFAULT_INT;
    private  String mAdditionalInfo = DatabaseHelper.DEFAULT_STRING;

    public Client(int id, String name, String street, int number, String city, int phone, String additionalInfo){
        this.mId = id;
        this.mName = name;
        this.mStreet = street;
        this.mNumber = number;
        this.mCity = city;
        this.mPhone = phone;
        this.mAdditionalInfo = additionalInfo;
    }

    public Client( String name, String street, int number, String city, int phone, String additionalInfo){
        this(DatabaseHelper.DEFAULT_INT, name, street, number, city, phone, additionalInfo);
    }

    public Client(Parcel in){
        String data[] = new String[7];

        in.readStringArray(data);

        this.mId = Integer.parseInt(data[0]);
        this.mName = data[1];
        this.mStreet = data[2];
        this.mNumber = Integer.parseInt(data[2]);
        this.mCity = data[4];
        this.mPhone = Integer.parseInt(data[5]);
        this.mAdditionalInfo = data[6];
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getStreet() {
        return mStreet;
    }

    public int getNumber() {
        return mNumber;
    }

    public String getCity() {
        return mCity;
    }

    public int getPhone() {
        return mPhone;
    }

    public String getAdditionalInfo() {
        return mAdditionalInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                Integer.toString(this.mId)
                ,this.mName
                ,this.mStreet
                ,Integer.toString(this.mNumber)
                ,this.mCity
                ,Integer.toString(this.mPhone)
                ,this.mAdditionalInfo
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Client>(){
        @Override
        public Client createFromParcel(Parcel parcel) {
            return new Client(parcel);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Client && ((Client) obj).getId() == this.getId();
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(this.getId()).hashCode();
    }
}
