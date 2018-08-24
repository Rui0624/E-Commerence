package com.example.android.onlineshopping.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "database";
    public static final int DATABASE_VERSION = 1;
    private static final String VARCHAR = "varchar";
    private static final String INTEGER = "INTEGER";

//    private static final String SQL_CART =
//            "create table shoppingCart(" +
//                    "id INTEGER primary key autoincrement," +
//                    "mobile varchar," +
//            "pid varchar," +
//            "pname varchar," +
//            "quantity INTEGER," +
//            "prize varchar," +
//            "image varchar)";
//
//    private static final String SQL_WISH =
//            "create table wishCart(" +
//            "id INTEGER primary key autoincrement," +
//            "mobile varchar," +
//            "pid varchar," +
//            "pname varchar," +
//            "quantity INTEGER," +
//            "prize varchar," +
//            "image varchar)";


    public DatabaseHelper(Context context) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table shoppingCart(" +
                "id INTEGER primary key autoincrement," +
                "mobile varchar," +
                "pid varchar," +
                "pname varchar," +
                "quantity INTEGER," +
                "prize varchar," +
                "imageurl varchar)";
        db.execSQL(sql);

        String likeSQL = "create table wishCart(" +
                "id INTEGER primary key autoincrement," +
                "mobile varchar," +
                "pid varchar," +
                "pname varchar," +
                "quantity INTEGER," +
                "prize varchar," +
                "imageurl varchar)";
        db.execSQL(likeSQL);
//        db.execSQL(SQL_CART);
//        db.execSQL(SQL_WISH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
