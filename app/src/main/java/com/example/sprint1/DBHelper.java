package com.example.sprint1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
    private static final String UserID = "userid" ;
    private static final String Username = "username";
    private static final String Email = "email";
    private static final String PhoneNumber = "phoneNumber";
    private static final String Password = "password";

    // Reservation table column names
    private static final String User_ID = "userid" ;
    private static final String ReservationID = "reservationid" ;
    private static final String Date = "date";
    private static final String Time = "time";
    private static final String Doc_Name = "doctorName";
    private static final String Service = "serviceType";



    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // create the tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTableQuery = "CREATE TABLE IF NOT EXISTS " + USER + " (" +
                UserID + " TEXT PRIMARY KEY AUTOINCREMENT, " +
                Username + " TEXT, " +
                Email + " TEXT, " +
                PhoneNumber + " TEXT, " +
                Password + " TEXT)";
        db.execSQL(createUserTableQuery);

        // Create reservation
        String createReservationTableQuery = "CREATE TABLE IF NOT EXISTS " + RESERVATION + " (" +
               ReservationID + " TEXT PRIMARY KEY AUTOINCREMENT, " +
                Doc_Name + " TEXT, " +
                Service + " TEXT," +
                 Date + " TEXT, " +
                Time + " TEXT, " +
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
    public Boolean insertData(userModel userMod) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Username, userMod.get_user_name());
        contentValues.put(Email, userMod.get_user_email());
        contentValues.put(PhoneNumber, userMod.get_user_phone());
        contentValues.put(Password, userMod.get_user_pass());
        long result = MyDB.insert(USER, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + USER + " where " + Username + " = ?", new
                String[]{username});
        boolean usernameExists = cursor.getCount() > 0;
        cursor.close();
        return usernameExists;
    }
    public Boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + USER + " where " + Username + " = ? and " + Password +
                " = ?", new String[] {username,password});
        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        return userExists;
    }


    // if a user is deleted then all his/her reservations are deleted
    public void deleteUserWithReservations(userModel user) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete user
        db.delete(USER, UserID + " = ?", new String[]{String.valueOf(user.get_user_Id())});

        // Delete reservations associated with the user
        db.delete(RESERVATION, User_ID + " = ?", new String[]{String.valueOf(user.get_user_Id())});

        db.close();
    }

    public Boolean insertReservationData(reservationModel reservation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(User_ID, reservation.getUserId());
        contentValues.put(Date, reservation.get_reservation_date());
        contentValues.put(Time, reservation.get_reservation_time());
        contentValues.put(Service, reservation.get_reservation_service());
        contentValues.put(Doc_Name, reservation.get_reservation_doctor());
        long result = db.insert(RESERVATION, null, contentValues);
        return result != -1;
    }

    public List<reservationModel> getReservationsForUser(int userId) {
        List<reservationModel> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RESERVATION +
                " WHERE " + User_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            int reservationIdIndex = cursor.getColumnIndex(ReservationID);
            int dateIndex = cursor.getColumnIndex(Date);
            int timeIndex = cursor.getColumnIndex(Time);
            int doctorIndex = cursor.getColumnIndex(Doc_Name);
            int serviceIndex = cursor.getColumnIndex(Service);

            do {
                int reservationId = cursor.getInt(reservationIdIndex);
                String date = cursor.getString(dateIndex);
                String time = cursor.getString(timeIndex);
                String doctorName = cursor.getString(doctorIndex);
                String serviceType = cursor.getString(serviceIndex);

                reservationModel reservation = new reservationModel(userId, reservationId, date, time, doctorName, serviceType);
                reservations.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reservations;
    }
    // delete a reservation
    public void deleteReservation(reservationModel reservation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(RESERVATION, ReservationID + " = ?", new String[]{String.valueOf(reservation.get_reservation_id())});
        db.close(); }



    // Method to update user information
    public void updateUser( userModel user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Username, user.get_user_name());
        values.put(Email, user.get_user_email());
        values.put(PhoneNumber, user.get_user_phone());
        values.put(Password, user.get_user_pass());


        db.update(USER, values, UserID + " = ?", new String[]{String.valueOf(user.get_user_Id())});
        db.close();
    }

    // Method to update reservation information
    public boolean updateReservation(reservationModel Res) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Date, Res.get_reservation_date());
        values.put(Time, Res.get_reservation_time());
        values.put(Doc_Name, Res.get_reservation_doctor());

        int rowsAffected = db.update(RESERVATION, values, ReservationID + " = ?", new String[]{String.valueOf(Res.get_reservation_id())});
        db.close();

        return rowsAffected > 0; // Return true if at least one row was updated
    }

    // Method to view reservation details
    public Cursor get_data (int userId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RESERVATION +
                " WHERE " + User_ID + " = ?", new String[]{String.valueOf(userId)});
        return cursor;
    }

}
