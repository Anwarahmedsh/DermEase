package com.example.sprint1;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class userModel {
        private int UserID ;
        private String Username ;
        private String Email ;
        private String PhoneNumber ;
        private String Password ;
        private List<reservationModel> reservations;

        private int ReservationID ;
        private String Date ;
        private String Time ;
        private String Doc_Name ;
        private String Service ;


        public userModel(int id, String name, String email, String phone, String password) {
            this.UserID = id ;
            this.Username = name;
            this.Email = email;
            this.PhoneNumber = phone;
            this.Password = password;
            this.reservations = new ArrayList<>();
        }

        // Constructor including reservation properties
        public userModel(int u_id, int id, String date, String time, String doctor, String service ) {
            this.UserID = u_id;
            this.ReservationID = id;
            this.Date = " ";
            this.Time = time ;
            this.Doc_Name = doctor;
            this.Service = service;
        }
        @NonNull
        @Override
        public String toString() {
            return "userModel{" +
                    "user ID =" + UserID +
                    ", Name ='" + Username + '\'' +
                    ", Email ='" + Email + '\'' +
                    ", Phone Number ='" + PhoneNumber + '\'' +
                    ", password ='" + Password + '\'' +
                    ", Reservation ID ='" + ReservationID + '\'' +
                    ", Date=" + Date +
                    ", Time=" + Time +
                    ", Doctor =" + Doc_Name +
                    ", Service Type =" + Service +
                    '}';
        }
        public int get_user_Id() {
            return UserID;
        }

        public void set_user_Id(int id) {
            this.UserID = id;
        }

        public String get_user_name() {
            return Username;
        }

        public void set_user_name(String name) {
            this.Username = name;
        }

        public String get_user_email() {
            return Email;
        }

        public void set_user_email(String email) {
            this.Email = email;
        }

        public String get_user_phone() {
            return PhoneNumber;
        }

        public void set_user_phone(String phone) {
            this.PhoneNumber = phone;
        }

        public String get_user_pass() {
            return Password;
        }

        public void set_user_pass(String pass) {
            this.Password = pass;
        }

        public int get_reservation_id() {
            return ReservationID;
        }

        public void set_reservation_id(int id) {
            this.ReservationID = id;
        }

        public String get_reservation_date() {
            return Date;
        }

        public void set_reservation_date(String date) {
            this.Date = date;
        }

        public String get_reservation_time() {
            return Time;
        }

        public void set_reservation_time(String time) {
            this.Time = time;
        }

        public String get_reservation_service() {
            return Service;
        }

        public void set_reservation_service(String service) {
            this.Service = service;
        }

        public String get_reservation_doctor() {
            return Doc_Name;
        }

        public void set_reservation_doctor(String doctor) {
            this.Doc_Name = doctor;
        }

         public void addReservation(reservationModel reservation) {
        this.reservations.add(reservation);
        }

        public void removeReservation(reservationModel reservation) {
        this.reservations.remove(reservation);
       }

    }




