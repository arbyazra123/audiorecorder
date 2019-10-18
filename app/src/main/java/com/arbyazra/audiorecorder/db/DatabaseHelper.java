package com.arbyazra.audiorecorder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "audioexample.db";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseContract.AudioBase.TABLE_NAME + " (" +
                    DatabaseContract.AudioBase._ID + " INTEGER PRIMARY KEY, " +
                    DatabaseContract.AudioBase.COLUMN_NAME_FILE + " TEXT , " +
                    DatabaseContract.AudioBase.COLUMN_NAME_OUTPUTFILE + " TEXT, " +
                    DatabaseContract.AudioBase.COLUMN_NAME_ONCREATED + " TEXT )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.AudioBase.TABLE_NAME;


}
