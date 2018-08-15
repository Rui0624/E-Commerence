package com.example.android.onlineshopping.utils;

import android.content.SharedPreferences;
import android.content.Context;


public class SharedPreferencesUtil {
    public static SharedPreferences getSp(Context context){
        return context.getSharedPreferences( "User Info", Context.MODE_PRIVATE );
    }

    /**
     * set the SharedPreference of the profiles for account
     * @param context
     * @param msg
     * @param id
     * @param firstname
     * @param lastname
     * @param email
     * @param mobile
     * @param appapikey
     */
    public static void setUserInfo(Context context, String msg, String id, String firstname, String lastname, String email, String mobile, String appapikey){
        SharedPreferences.Editor editor = getSp(context).edit();
        editor.putString("msg", msg);
        editor.putString("id", id);
        editor.putString("firstname", firstname);
        editor.putString("lastname", lastname);
        editor.putString("email", email);
        editor.putString("mobile", mobile);
        editor.putString("appapikey", appapikey);
        editor.commit();
    }

    public static void updateUserInfo(Context context, String firstname, String lastname, String email, String mobile){
        SharedPreferences.Editor editor = getSp(context).edit();
        editor.putString("firstname", firstname);
        editor.putString("lastname", lastname);
        editor.putString("email", email);
        editor.putString("mobile", mobile);
        editor.commit();
    }

    /**
     * set the SharedPreference for the mobile
     * @param context
     * @param mobile
     */
    public static void setRemember(Context context, String mobile){
        SharedPreferences.Editor editor = getSp( context ).edit();
        editor.putString( "remember", mobile );
        editor.commit();
    }

    /**
     * get the SharedPreference
     * @param context
     * @return String
     */
    public static String getRemember(Context context){
        return getSp( context ).getString( "remember", null );
    }
}
