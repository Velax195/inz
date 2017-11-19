package com.kszych.getdata.utils;

public interface GetRFIDTaskCompleteListener {
    void cardIsRead(String uidHex, String uidDec);
}
