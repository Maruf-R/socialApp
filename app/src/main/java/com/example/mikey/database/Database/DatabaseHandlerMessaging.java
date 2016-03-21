package com.example.mikey.database.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHandlerMessaging extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Messages";
    private static final String TABLE_LOGIN = "Messages";


    // Login Table Column names
    private static final String KEY_ID = "id";
    private static final String SENDER_NAME = "senderusername";
    private static final String RECEIVER  = "receiverusername";
    private static final String CONTENT = "msgcontent";
    private static final String KEY_CREATED_AT = "created_at";

    public DatabaseHandlerMessaging(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @Override
     * Creates a new table
     * */
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SENDER_NAME + " TEXT,"
                + RECEIVER + " TEXT,"
                + CONTENT + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);     //TODO: change this for the messaging sql table once created

    }

    /**
     * @Override
     * Upgrading database
     * */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        // Create tables again
        onCreate(db);
    }

    /**
     * Retrieve user details from the database
     */
    public HashMap getUserDetails() {
        HashMap user = new HashMap();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("age", cursor.getString(2));
            user.put("nationality", cursor.getString(3));
            user.put("email", cursor.getString(4));
            user.put("password", cursor.getString(5));
            user.put("answer", cursor.getString(6));
            user.put("question", cursor.getString(7));
            user.put("created_at", cursor.getString(8));
        }
        cursor.close();
        db.close();

        System.out.println("its possible to get " + user.get("name"));
        System.out.println("its possible to get " + user.get("age"));
        System.out.println("its possible to get " + user.get("nationality"));
        System.out.println("its possible to get " + user.get("email"));
        System.out.println("its possible to get " + user.get("password"));
        System.out.println("its possible to get " + user.get("answer"));
        System.out.println("its possible to get " + user.get("question"));



        // return user
        return user;
    }


    /**
     * Add a Message to the database
     * */
    public void addMessage(String sname, String rname, String content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SENDER_NAME, sname );
        values.put(RECEIVER, rname);
        values.put(CONTENT, content);

        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }



    public HashMap getUserMessages() {
        HashMap user = new HashMap();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("sname", cursor.getString(1));
            user.put("rname", cursor.getString(2));
            user.put("content", cursor.getString(3));

        }
        cursor.close();
        db.close();


        // return user
        return user;
    }



    /**
     * Getting users login status
     * return true if rows are in table
     */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
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
    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

}