package com.example.bitcash;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
//importing everything from thr unitContact class to avoid writing it each time
import com.example.bitcash.UnitContract.*;

public class UnitDBH extends SQLiteOpenHelper {
    //variables declaration
    public static final String DATABASE_NAME = "unitsDB.db";
    public static final int DATABASE_VERSION = 1;
    //constructor
    public UnitDBH(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //this method is called to create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //declaring the string to hold the create table statement
        final String CREATE_UNITS_TABLE = "CREATE TABLE " +
                UnitEntry.TABLE_NAME + " (" +
                UnitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UnitEntry.COLUMN_VALUE + " INTEGER NOT NULL, " +
                UnitEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                UnitEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ");";
        //executing the statement
        sqLiteDatabase.execSQL(CREATE_UNITS_TABLE);
    }
    //this method is called when the database version is updated
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //we are just dropping the table here and then create a new one, no schema updatesS
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UnitEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
