package com.example.nao.recpoints;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DBNAME = "points.db";
    private static final int DBVERSION = 1;
    public static final String TABLE_POINTS = "points";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LON = "lon";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_REMARKS = "remarks";
    private static final String CREATE_TABLE_SQL =
            "create table " + TABLE_POINTS  + " "
                    + "(" + COLUMN_ID +" integer primary key autoincrement,"
                    + COLUMN_LAT + " real not null,"
                    + COLUMN_LON + " real not null,"
                    + COLUMN_ADDRESS + " text null,"
                    + COLUMN_REMARKS + " text not null)";

    public DatabaseHelper(Context context) {
        super(context,DBNAME,null,DBVERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {


    }

}
