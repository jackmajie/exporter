package com.wufumall.dataexporter.core;

public class TaskParams {

    private static String ownSign;

    public static String getOwnSign() {
        if (ownSign == null || ownSign.trim().startsWith("${")) {
            ownSign = "ONLINE";
        }
        return ownSign;
    }

    public  void setOwnSign(String ownSign) {
        ownSign = ownSign;
    }
}
