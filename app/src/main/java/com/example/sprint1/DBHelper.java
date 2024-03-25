package com.example.sprint1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    //private OnReservationDeletedListener onReservationDeletedListener;

    //private OnUserUpdatedListener onUserUpdatedListener;
    //private OnReservationUpdatedListener onReservationUpdatedListener;

    private static final String DATABASE_NAME = "DermEase.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String USER = "user";
    private static final String RESERVATION = "reservation";

    // User table column names
    private static final String UserID = "userID";
    private static final String Username = "username";
    private static final String Email = "email";
    private static final String PhoneNumber = "phoneNumber";
    private static final String Password = "password";

    // Reservation table column names
    private static final String User_ID = "user_ID";
    private static final String ReservationID = "reservationID";
    private static final String Date = "date";
    private static final String Doc_Name = "doctorName";
    private static final String Service = "serviceType";



    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // create the tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTableQuery = "CREATE TABLE IF NOT EXISTS " + USER + " (" +
                UserID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Username + " TEXT, " +
                Email + " TEXT, " +
                PhoneNumber + " TEXT, " +
                Password + " TEXT)";
        db.execSQL(createUserTableQuery);

        // Create reservation
        String createReservationTableQuery = "CREATE TABLE IF NOT EXISTS " + RESERVATION + " (" +
               ReservationID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Doc_Name + " TEXT, " +
                Service + " TEXT," +
                 Date + " TEXT, " +
                "FOREIGN KEY (" + User_ID + ") REFERENCES " + USER + "(" + UserID + ") ON DELETE CASCADE)";

        db.execSQL(createReservationTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER);
        db.execSQL("DROP TABLE IF EXISTS " + RESERVATION);

        // Create tables again
        onCreate(db);
    }
    public Boolean insertData(String username, String email, String phone, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(Username, username);
        contentValues.put(Email, email);
        contentValues.put(PhoneNumber, phone);
        contentValues.put(Password, password);
        long result = MyDB.insert(USER, null, contentValues);
        if(result==-1) return false;
        return true;
    }
    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + USER + " where " + Username + " = ?", new
                String[]{username});
        if (cursor.getCount() > 0) return true;
        return false;
    }
    public Boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + USER + " where " + Username + " = ? and " + Password +
                " = ?", new String[] {username,password});
        if(cursor.getCount()>0) return true;
        return false;
    }


    // if a user is deleted then all his/her reservations are deleted
    public void deleteUserWithReservations(long userID) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete user
        db.delete(USER, UserID + " = ?", new String[]{String.valueOf(userID)});

        // Delete reservations associated with the user
        db.delete(RESERVATION, User_ID + " = ?", new String[]{String.valueOf(userID)});

        db.close();
    }

    public Boolean insertData(String docName, String service, String date){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(Doc_Name, docName);
        contentValues.put(Service, service);
        contentValues.put(Date, date);
        long result = MyDB.insert(RESERVATION, null, contentValues);
        if(result==-1) return false;
        return true;
    }
    // delete a reservation
    public void deleteReservation(long reservationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(RESERVATION, ReservationID + " = ?", new String[]{String.valueOf(reservationId)});
        db.close(); }



    // Method to update user information
    public void updateUser( long id, String username, String email, String PH, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Username, username);
        values.put(Email, email);
        values.put(PhoneNumber, PH);
        values.put(Password, pass);


        db.update(USER, values, UserID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Method to update reservation information
    public void updateReservation(String reservationId, String date, String service) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Date, date);
        values.put(Service, service);

        db.update(RESERVATION, values, ReservationID + " = ?", new String[]{String.valueOf(reservationId)});
        db.close();
    }

}
