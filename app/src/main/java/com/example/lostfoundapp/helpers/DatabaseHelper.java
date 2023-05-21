package com.example.lostfoundapp.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sit305_7_1P";
    private static final int DATABASE_VERSION = 1;
    private static final String ITEM_TABLE_NAME = "items";
    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!isTableExists(db, ITEM_TABLE_NAME)) {
            String createTableQuery = "CREATE TABLE " + ITEM_TABLE_NAME + " (" +
                    "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Name TEXT, " +
                    "Phone TEXT, " +
                    "Description TEXT, " +
                    "Date INTEGER, " +
                    "Location TEXT, " +
                    "PostType TEXT," +
                    "Latitude REAL," +
                    "Longitude REAL" +
                    ")";
            db.execSQL(createTableQuery);
        }
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        String query = "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + tableName + "'";
        Cursor cursor = db.rawQuery(query, null);
        boolean tableExists = cursor.getCount() > 0;
        cursor.close();
        return tableExists;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade database schema here if needed
    }
}