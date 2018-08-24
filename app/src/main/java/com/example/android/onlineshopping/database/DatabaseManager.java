package com.example.android.onlineshopping.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.onlineshopping.dataItem.CartInfo;
import com.example.android.onlineshopping.dataItem.OrderInfo;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private String cartTable = "shoppingCart";
    private String wishTable = "wishCart";


    public DatabaseManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void openDatabase(){
        mSQLiteDatabase = databaseHelper.getReadableDatabase();
    }

    public void closeDatabase(){
        mSQLiteDatabase.close();
    }

    public void addItemToCart(String mobile, String pid, String pname, int quantity, String prize, String image){
        ContentValues contentValues = new ContentValues();
        contentValues.put("mobile", mobile);
        contentValues.put("pid", pid);
        contentValues.put("pname", pname);
        contentValues.put("quantity", quantity);
        contentValues.put("prize", prize);
        contentValues.put("imageurl", image);
        this.mSQLiteDatabase.insertOrThrow(cartTable, null, contentValues);
    }

    public void addItemToWish(String mobile, String pid, String pname, int quantity, String prize, String image){
        ContentValues contentValues = new ContentValues();
        contentValues.put("mobile", mobile);
        contentValues.put("pid", pid);
        contentValues.put("pname", pname);
        contentValues.put("quantity", quantity);
        contentValues.put("prize", prize);
        contentValues.put("imageurl", image);
        this.mSQLiteDatabase.insertOrThrow(wishTable, null, contentValues);
    }

    //check whether item already exist in cart
    public int verifyItemInCart(String productName, String mobile) {
        int quantity = -1;
        String sql = "SELECT quantity FROM " + cartTable + " WHERE pname =" + "\"" + productName + "\"" + "AND mobile =" + "\"" + mobile + "\"";
        Cursor result = mSQLiteDatabase.rawQuery(sql, null);
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            quantity = result.getInt(0);
        }

        return quantity;
    }

    //check whether item already exist in cart
    public int verifyItemWish(String pid, String userId) {
        int quantity = -1;
        String sql = "SELECT pname FROM " + wishTable + " WHERE pid =" + "\"" + pid + "\"" + "AND mobile =" + "\"" + userId + "\"";
        Cursor result = mSQLiteDatabase.rawQuery(sql, null);
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            quantity = result.getInt(0);
        }

        return quantity;
    }

    public void updataCartQuantity(int newQuantity, String productName, String mobile){
        String sql = "UPDATE " + cartTable + " "
                + "SET quantity =" + "\"" + newQuantity + "\"" + "WHERE pname=" + "\"" + productName + "\"" + "AND mobile =" + "\"" + mobile + "\"";
        mSQLiteDatabase.execSQL(sql);
    }



    public List<OrderInfo> getCartList(String mobile){
        List<OrderInfo> list = new ArrayList<>();
        String sql = "SELECT pid, pname, quantity, prize, imageurl From " + cartTable + " WHERE mobile =" + "\"" + mobile + "\"";
        Cursor result = mSQLiteDatabase.rawQuery(sql +"", null);
        for(result.moveToFirst(); !result.isAfterLast(); result.moveToNext()){
            OrderInfo orderInfo = new OrderInfo(result.getString(0)
                    , result.getString(1), Integer.valueOf(result.getString(2))
                    , result.getString(3), result.getString(4));
            list.add(orderInfo);
        }

        return list;
    }

    public List<OrderInfo> getWishList(String mobile){
        List<OrderInfo> list = new ArrayList<>();
        String sql = "SELECT pid, pname, quantity, prize, imageurl FROM " + cartTable + " WHERE mobile =" + "\"" + mobile + "\"";
        Cursor result = mSQLiteDatabase.rawQuery(sql + "", null);
        for(result.moveToFirst(); !result.isAfterLast(); result.moveToNext()){
            OrderInfo orderInfo = new OrderInfo(result.getString(0)
                    , result.getString(1), result.getInt(2)
                    , result.getString(3), result.getString(4));
            list.add(orderInfo);
        }

        return list;
    }

    public void deleteItem(String mobile){
        String sql = "DELETE FROM " + cartTable + " WHERE mobile=" +  "\"" + mobile + "\"";
        mSQLiteDatabase.execSQL(sql);
    }

    public void deleteItemCart(String mobile, String pid){
        String sql = "DELETE FROM " + cartTable + " WHERE mobile=" +  "\"" + mobile + "\"" + "AND pid =" + "\"" + pid + "\"";
        mSQLiteDatabase.execSQL(sql);
    }

    public void deleteItemWish(String mobile, String pid){
        String sql = "DELETE FROM " + wishTable + " WHERE mobile=" +  "\"" + mobile + "\"" + "AND pid =" + "\"" + pid + "\"";
        mSQLiteDatabase.execSQL(sql);
    }




}
