package com.aimuhire.onlinecontactlist;


import android.provider.BaseColumns;

public final class DBContract {
    // To prevent someone from accidentally instantiating the contract class,
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactList.TABLE_NAME + " (" +
                    ContactList._ID + " INTEGER PRIMARY KEY," +
                    ContactList.COLUMN_NAME_NAMES + " TEXT," +
                    ContactList.COLUMN_NAME_PHONE + " TEXT," +
                    ContactList.COLUMN_NAME_IMAGE + " TEXT)";

    // make the constructor private.

    private DBContract() {}

    /* Inner class that defines the table contents */
    public static class ContactList implements BaseColumns {

        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME_NAMES= "names";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_IMAGE= "imageSrc";
    }



}
