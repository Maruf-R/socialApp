package com.example.mikey.database.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;


public class DatabaseHandler extends SQLiteOpenHelper {
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
    private static final String KEY_EDUCATION = "education";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_COUNTRY = "Country";
    private static final String KEY_CITY = "City";
    private static final String KEY_CREATED_AT = "created_at";

    public DatabaseHandler(Context context) {
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
                + KEY_EDUCATION + " TEXT,"
                + KEY_GENDER + " TEXT,"
                + KEY_COUNTRY + " TEXT,"
                + KEY_CITY + " TEXT,"
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
    public void addUser(String name, String age, String nationality, String email, String password, String answer, String question,String education, String gender,String country,String city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name );
        values.put(KEY_AGE, age);
        values.put(KEY_FROM, nationality);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORDHASH, password);
        values.put(KEY_ANSWER, answer);
        values.put(KEY_QUESTION, question);
        values.put(KEY_EDUCATION, education);
        values.put(KEY_GENDER, gender);
        values.put(KEY_COUNTRY, country);
        values.put(KEY_CITY, city);

        System.out.println("its stored gender db" + gender);
        System.out.println("its stored edu db" + education);


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
            user.put("education", cursor.getString(8));
            user.put("gender", cursor.getString(9));
            user.put("country", cursor.getString(10));
            user.put("city", cursor.getString(11));

            user.put("created_at", cursor.getString(12));
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