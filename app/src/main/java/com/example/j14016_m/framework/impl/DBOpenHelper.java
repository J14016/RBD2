package com.example.j14016_m.framework.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by J14016_M on 2016/02/08.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "RBD2Database.db";
    public static final String TABLE_NAME = "time";
    private static DBOpenHelper instance = null;


    static void create(Context context) {
        instance = new DBOpenHelper(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        db.close();
    }

    static void destroy() {
        instance.close();
        instance = null;
    }

    public static DBOpenHelper getInstance() {
        return instance;
    }

    public static SQLiteDatabase getWriteDatabase() {
        return instance.getWritableDatabase();
    }

    public static SQLiteDatabase getReadDatabase() {
        return instance.getReadableDatabase();
    }

    private DBOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" +
                "_id integer primary key autoincrement not null,"
                + "time real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
