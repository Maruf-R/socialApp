package com.example.mikey.database.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by mikey on 20/03/2016.
 */
public class DatabaseInterests  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "interests";
    private static final String TABLE_LOGIN = "interests";




    // Login Table Column names
    private static final String KEY_ID = "id";
    private static final String KEY_MUSIC = "music";
    private static final String KEY_MOVIES = "movies";
    private static final String KEY_SPORTS = "sports";
    private static final String KEY_FOOD = "food";
    private static final String KEY_HOBBIES = "hobbies";
    private static final String KEY_EMAIL = "email";

    private static final String KEY_CREATED_AT = "created_at";

    public DatabaseInterests(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @Override
     * Creates a new table
     * */
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MUSIC + " TEXT,"
                + KEY_MOVIES + " TEXT,"
                + KEY_SPORTS + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_FOOD + " TEXT,"
                + KEY_HOBBIES + " TEXT,"
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
    public void addUser(String music, String movies, String sports, String email, String food, String hobbies) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MUSIC, music);
        values.put(KEY_MOVIES, movies);
        values.put(KEY_SPORTS, sports);
        values.put(KEY_EMAIL, email);
        values.put(KEY_FOOD, food);
        values.put(KEY_HOBBIES, hobbies);


        System.out.println("its stored musci db" + music);
        System.out.println("its stored sports db" + sports);


        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }



    public HashMap getUserInterests() {
        HashMap user = new HashMap();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("music", cursor.getString(1));
            user.put("movies", cursor.getString(2));
            user.put("sports", cursor.getString(3));
            user.put("email", cursor.getString(4));
            user.put("food", cursor.getString(5));
            user.put("hobbies", cursor.getString(6));
            user.put("created_at", cursor.getString(7));
        }
        cursor.close();
        db.close();

        System.out.println("its possible to get " + user.get("music"));
        System.out.println("its possible to get " + user.get("movies"));
     /*   System.out.println("its possible to get " + user.get("age"));
        System.out.println("its possible to get " + user.get("nationality"));
        System.out.println("its possible to get " + user.get("email"));
        System.out.println("its possible to get " + user.get("password"));
        System.out.println("its possible to get " + user.get("answer"));
        System.out.println("its possible to get " + user.get("question"));

*/

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
