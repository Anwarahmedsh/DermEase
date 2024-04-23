package com.example.sprint1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
                UserID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Username + " TEXT, " +
                Email + " TEXT, " +
                PhoneNumber + " INTEGER, " +
                Password + " TEXT)";
        db.execSQL(createUserTableQuery);

        // Create reservation
        String createReservationTableQuery = "CREATE TABLE IF NOT EXISTS " + RESERVATION + " (" +
               ReservationID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Doc_Name + " TEXT, " +
                Service + " TEXT," +
                 Date + " TEXT, " +
                Time + " TEXT, " +
                User_ID + " INTEGER, "+
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
    public void updateReservation(reservationModel Res) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Date, Res.get_reservation_date());
        values.put(Time, Res.get_reservation_time());

        try {
            db.update(RESERVATION, values, ReservationID + " = ?", new String[]{String.valueOf(Res.get_reservation_id())});
        } catch (Exception e) {
            Log.e("DB_UPDATE_ERROR", "Error updating reservation: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    // Method to view reservation details
    public Cursor get_data (int userId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RESERVATION +
                " WHERE " + User_ID + " = ?", new String[]{String.valueOf(userId)});
        return cursor;
    }

    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // Default value if user is not found

        Cursor cursor = db.rawQuery("SELECT " + UserID + " FROM " + USER + " WHERE " + Username + " = ?", new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(UserID);
            if (userIdIndex != -1) {
                userId = cursor.getInt(userIdIndex);
            }
        }
        if(cursor != null){
            cursor.close();
        }
        return userId;
    }

    // Inside DBHelper class

    public reservationModel getReservationByDTD(String selectedDate, String selectedTime, String selectedDermatologist) {
        SQLiteDatabase db = this.getReadableDatabase();
        reservationModel reservation = null;

        // Construct the SQL query
        String query = "SELECT * FROM " + RESERVATION +
                " WHERE " + Date + " = ?" +
                " AND " + Time + " = ?" +
                " AND " + Doc_Name + " = ?";
        String[] selectionArgs = {selectedDate, selectedTime, selectedDermatologist};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(User_ID);
            int reservationIdIndex = cursor.getColumnIndex(ReservationID);
            int dateIndex = cursor.getColumnIndex(Date);
            int timeIndex = cursor.getColumnIndex(Time);
            int doctorIndex = cursor.getColumnIndex(Doc_Name);
            int serviceIndex = cursor.getColumnIndex(Service);

            int userId = cursor.getInt(userIdIndex);
            int reservationId = cursor.getInt(reservationIdIndex);
            String date = cursor.getString(dateIndex);
            String time = cursor.getString(timeIndex);
            String doctorName = cursor.getString(doctorIndex);
            String serviceType = cursor.getString(serviceIndex);

            reservation = new reservationModel(userId, reservationId, date, time, doctorName, serviceType);
        }
        cursor.close();
        return reservation;
    }

    public reservationModel getReservationById(int reservationId) {
        SQLiteDatabase db = this.getReadableDatabase();
        reservationModel reservation = null;

        Cursor cursor = db.query(
                RESERVATION,
                new String[]{ReservationID, User_ID, Service, Doc_Name, Date, Time},
                ReservationID + "=?",
                new String[]{String.valueOf(reservationId)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(User_ID);
            int dateIndex = cursor.getColumnIndex(Date);
            int timeIndex = cursor.getColumnIndex(Time);
            int doctorIndex = cursor.getColumnIndex(Doc_Name);
            int serviceIndex = cursor.getColumnIndex(Service);

            int userId = cursor.getInt(userIdIndex);
            String date = cursor.getString(dateIndex);
            String time = cursor.getString(timeIndex);
            String doctorName = cursor.getString(doctorIndex);
            String serviceType = cursor.getString(serviceIndex);

            // Create the reservation model object
            reservation = new reservationModel(reservationId, userId, serviceType, doctorName, date, time);

            cursor.close();
        }

        return reservation;
    }

    public userModel getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        userModel user = null;

        Cursor cursor = db.query(
                USER,
                new String[]{UserID, Username, Email, PhoneNumber, Password},
                UserID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(UserID);
            int nameIndex = cursor.getColumnIndex(Username);
            int emailIndex = cursor.getColumnIndex(Email);
            int phoneIndex = cursor.getColumnIndex(PhoneNumber);
            int passIndex = cursor.getColumnIndex(Password);

            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String email = cursor.getString(emailIndex);
            String phone = cursor.getString(phoneIndex);
            String pass = cursor.getString(passIndex);

            // Create the user model object
            user = new userModel(id, name, email, phone, pass);

            cursor.close();
        }

        return user;
    }
}

