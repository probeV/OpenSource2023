package com.example.opensource2023;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public DBHelper(Context context) {
        super(context, "diarydb", null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String diarySQL = "create table diarylist (" + "id integer primary key autoincrement, " + "year, " + "month, " + "day)";
        sqLiteDatabase.execSQL(diarySQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i1 == DATABASE_VERSION) {
            sqLiteDatabase.execSQL("drop table diarylist");
            onCreate(sqLiteDatabase);
        }
    }
}
