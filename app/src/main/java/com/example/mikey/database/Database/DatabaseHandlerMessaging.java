package com.example.mikey.database.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DatabaseHandlerMessaging extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Messages";
    private static final String TABLE_MESSAGE = "Messages";


    // Login Table Column names
    private static final String KEY_ID = "id";
    private static final String SENDER_UNAME = "senderusername";
    private static final String RECEIVER_UNAME  = "receiverusername";
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
        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SENDER_UNAME + " TEXT,"
                + RECEIVER_UNAME + " TEXT,"
                + CONTENT + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_MESSAGES_TABLE);

    }

    /**
     * @Override
     * Upgrading database
     * */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        // Create tables again
        onCreate(db);
    }



    /**
     * Add a Message to the database
     * */
    public void addMessage(String sname, String rname, String content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SENDER_UNAME, sname );
        values.put(RECEIVER_UNAME, rname);
        values.put(CONTENT, content);
        values.put(KEY_CREATED_AT, getDateTime());

        db.insert(TABLE_MESSAGE, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Gets the current date and time
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    /**
     * Retrieve all of the users Messages
     * */
    public HashMap getAllUserMessages() {
        HashMap user = new HashMap();
        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGE;

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
     * Retrieves messages user sent to a particular recipient
     * */
    public HashMap getMessagesTo(String r){
        HashMap recipient = new HashMap();
        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGE; //TODO: implement the correct query

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            recipient.put("sname", cursor.getString(1));
            recipient.put("rname", cursor.getString(2));
            recipient.put("content", cursor.getString(3));

        }
        cursor.close();
        db.close();

        // return user
        return recipient;
    }

    /**
     * Getting users login status
     * return true if rows are in table
     */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MESSAGE;
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
        db.delete(TABLE_MESSAGE, null, null);
        db.close();
    }

}