package com.example.mikey.database.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by maruf on 14/02/2016.
 * This class will be used to handle all database functionalities like adding users, deleting users and retrieveing users etc
 */
public class DatabaseHandlerContacts extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contacts";



    private static final String DATABASE_CONTACTS = "contacts";

    // Login Table Column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_FROM = "nationality";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAMEFRIEND = "namef";
    private static final String KEY_MAXAGE = "maxa";
    private static final String KEY_MINAGE = "mina";
    private static final String KEY_CREATED_AT = "created_at";

    public DatabaseHandlerContacts(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @Override Creates a new table
     */
    public void onCreate(SQLiteDatabase db) {



        String CREATE_REGISTER_TABLE = "CREATE TABLE " + DATABASE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_AGE + " TEXT,"
                + KEY_FROM + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_MAXAGE + " TEXT,"
                + KEY_MINAGE + " TEXT,"
                + KEY_NAMEFRIEND + " TEXT UNIQUE,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_REGISTER_TABLE);


    }

    /**
     * @Override Upgrading database
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CONTACTS);
        // Create tables again
        onCreate(db);
    }

    /**
     * Adding a user to the database
     */


    public void addUserContacts(String name, String age, String nationality, String email, String maxa, String mina, String namef) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_AGE, age);
        values.put(KEY_FROM, nationality);
        values.put(KEY_EMAIL, email);
        values.put(KEY_MAXAGE, maxa);
        values.put(KEY_MINAGE, mina);
        values.put(KEY_NAMEFRIEND, namef);
        System.out.println("its stored IN CONTACTS" + email);

        // Inserting Row
        db.insert(DATABASE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }




    public HashMap getUserContacts() {
        HashMap user = new HashMap();
        String selectQuery = "SELECT  * FROM " + DATABASE_CONTACTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("age", cursor.getString(2));
            user.put("nationality", cursor.getString(3));
            user.put("email", cursor.getString(4));
            user.put("maxa", cursor.getString(5));
            user.put("mina", cursor.getString(6));
            user.put("namef", cursor.getString(7));


            //     user.put("uid", cursor.getString(6));
            user.put("created_at", cursor.getString(8));
        }
        cursor.close();
        db.close();

        System.out.println("its possible to get name " + user.get("name"));
        System.out.println("its possible to get age" + user.get("age"));
        System.out.println("its possible to get natio" + user.get("nationality"));
        System.out.println("its possible to get username email  " + user.get("email"));

        System.out.println("its possible to get maxa " + user.get("maxa"));
        System.out.println("its possible to get mina" + user.get("mina"));

        System.out.println("its possible to get friend" + user.get("namef"));

        // return user
        return user;
    }


    /**
     * Getting users login status
     * return true if rows are in table
     */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + DATABASE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re-create database
     * Delete all tables and create them again
     */


    public void resetTablesContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(DATABASE_CONTACTS, null, null);
        db.close();
    }
}
