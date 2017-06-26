package com.example.mikey.database.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;


public class DatabaseUsernameId extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "users";
    private static final String TABLE_LOGIN = "users";




    // Login Table Column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_FROM = "nationality";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORDHASH = "password";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_QUESTION = "question";

    private static final String KEY_CREATED_AT = "created_at";

    public DatabaseUsernameId(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @Override
     * Creates a new table
     * */
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_AGE + " TEXT,"
                + KEY_FROM + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORDHASH + " TEXT,"
                + KEY_ANSWER + " TEXT,"
                + KEY_QUESTION + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);



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
     * Adding a user to the database
     */
    public void addUser(String name, String age, String nationality, String email, String password, String answer, String question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name );
        values.put(KEY_AGE, age);
        values.put(KEY_FROM, nationality);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORDHASH, password);
        values.put(KEY_ANSWER, answer);
        values.put(KEY_QUESTION, question);


        System.out.println("its stored" + email);
        System.out.println("its stored question" + question);


        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }



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