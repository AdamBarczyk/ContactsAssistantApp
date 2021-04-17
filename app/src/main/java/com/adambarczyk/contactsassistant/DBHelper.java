package com.adambarczyk.contactsassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contacts(" +
                "contact_id INTEGER PRIMARY KEY," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT," +
                "email TEXT UNIQUE," +
                "phone TEXT NOT NULL UNIQUE," +
                "address TEXT," +
                "notes TEXT)");

        db.execSQL("CREATE TABLE services(" +
                "service_id INTEGER PRIMARY KEY," +
                "contact_id INTEGER NOT NULL," +
                "service_desc TEXT," +
                "service_cost TEXT," +
                "FOREIGN KEY (contact_id) REFERENCES contacts (contact_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
