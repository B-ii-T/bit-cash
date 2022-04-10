package com.example.bitcash;

import android.provider.BaseColumns;

public class UnitContract {
    //constructor
    private UnitContract(){}
    //implementing the BaseColumns interface to help create a database table and columns
    public static final class UnitEntry implements BaseColumns{
        //constants declaration
        public static final String TABLE_NAME = "units_table";
        public static final String COLUMN_VALUE = "unit_value";
        public static final String COLUMN_QUANTITY = "unit_qnt";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
