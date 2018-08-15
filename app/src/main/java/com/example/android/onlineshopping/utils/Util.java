package com.example.android.onlineshopping.utils;

public class Util {
    private static Util utilInstance;

    public static synchronized Util getUtilInstance(){
        utilInstance = new Util();
        return utilInstance;
    }

    public boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

}
