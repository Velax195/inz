package com.kszych.pms.utils;


public class PartInOrder {

    private int mId = DatabaseHelper.DEFAULT_INT;

    private int mOrderId = DatabaseHelper.DEFAULT_INT;
    private int mPartId = DatabaseHelper.DEFAULT_INT;
    private int mQuantity = DatabaseHelper.DEFAULT_INT;


    public PartInOrder(int id, int orderId, int partId, int quantity){
        this.mId=id;
        this.mOrderId = orderId;
        this.mPartId = partId;
        this.mQuantity = quantity;
    }

    public PartInOrder(int orderId, int partId, int quantity) {
        this(DatabaseHelper.DEFAULT_INT, orderId, partId, quantity);
    }


    public int getId() {
        return mId;
    }

    public int getOrderId() {
        return mOrderId;
    }

    public int getPartId() {
        return mPartId;
    }

    public int getQuantity() {
        return mQuantity;
    }

}
