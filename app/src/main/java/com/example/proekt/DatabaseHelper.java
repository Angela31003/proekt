package com.example.proekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TouristGuide.db";
    private static final int DATABASE_VERSION = 2;


    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FIRSTNAME = "firstname";
    private static final String COLUMN_LASTNAME = "lastname";
    private static final String COLUMN_EMAIL = "email";


    private static final String TABLE_TRAVELS = "travels";
    private static final String COLUMN_TRAVEL_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_DESTINATION = "destination";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LNG = "lng";


    private static final String TABLE_PLACES = "places";
    private static final String COLUMN_PLACE_NAME = "name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_FIRSTNAME + " TEXT, "
                + COLUMN_LASTNAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE);";
        db.execSQL(CREATE_USERS_TABLE);


        String CREATE_TRAVELS_TABLE = "CREATE TABLE " + TABLE_TRAVELS + " ("
                + COLUMN_TRAVEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " TEXT, "
                + COLUMN_DESTINATION + " TEXT, "
                + COLUMN_LAT + " REAL, "
                + COLUMN_LNG + " REAL);";
        db.execSQL(CREATE_TRAVELS_TABLE);


        String CREATE_PLACES_TABLE = "CREATE TABLE " + TABLE_PLACES + " ("
                + COLUMN_PLACE_NAME + " TEXT, "
                + COLUMN_LAT + " REAL, "
                + COLUMN_LNG + " REAL);";
        db.execSQL(CREATE_PLACES_TABLE);

        insertDefaultPlaces(db);
    }

    private void insertDefaultPlaces(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_PLACES + " (" + COLUMN_PLACE_NAME + ", " + COLUMN_LAT + ", " + COLUMN_LNG + ") VALUES " +
                "('The Old Bazaar', 41.9981, 21.4361), " +
                "('Fortress Kale', 42.00211, 21.43306), " +
                "('Macedonia Square', 41.99654, 21.43320), " +
                "('Stone Bridge', 41.9969689, 21.4330350), " +
                "('City Trade Center Skopje', 41.994900, 21.434332), " +
                "('Museum of Contemporary Art', 42.003941, 21.432785), " +
                "('Archbishop Cathedral „St. Clement of Ohrid”', 41.998513, 21.426472), " +
                "('Mother Teresa Memorial House', 41.993848, 21.430877), " +
                "('City Park', 42.008883, 21.414495)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVELS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        onCreate(db);
    }


    public boolean registerUser(String username, String password, String firstname, String lastname, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_FIRSTNAME, firstname);
        values.put(COLUMN_LASTNAME, lastname);
        values.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }


    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }


    public boolean saveTravel(String username, String destination, double lat, double lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, username);
        values.put(COLUMN_DESTINATION, destination);
        values.put(COLUMN_LAT, lat);
        values.put(COLUMN_LNG, lng);

        long result = db.insert(TABLE_TRAVELS, null, values);
        db.close();
        return result != -1;
    }


    public List<String> getUserTravels(String username) {
        List<String> travels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT destination FROM travels WHERE user_id = ?", new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                travels.add(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return travels;
    }



    public String getNearestPlaceName(double lat, double lng) {
        SQLiteDatabase db = this.getReadableDatabase();
        String placeName = "unknown location";

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PLACE_NAME + " FROM " + TABLE_PLACES + " WHERE " +
                        "(" + COLUMN_LAT + " BETWEEN ? AND ?) AND (" + COLUMN_LNG + " BETWEEN ? AND ?) " +
                        "ORDER BY (ABS(" + COLUMN_LAT + " - ?) + ABS(" + COLUMN_LNG + " - ?)) LIMIT 1",
                new String[]{
                        String.valueOf(lat - 0.001), String.valueOf(lat + 0.001),
                        String.valueOf(lng - 0.001), String.valueOf(lng + 0.001),
                        String.valueOf(lat), String.valueOf(lng)
                });

        if (cursor.moveToFirst()) {
            placeName = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return placeName;
    }
}

