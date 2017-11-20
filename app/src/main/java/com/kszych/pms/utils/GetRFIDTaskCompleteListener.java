package com.kszych.pms.utils;

public interface GetRFIDTaskCompleteListener {
    void cardIsRead(String uidHex, String uidDec);
}
