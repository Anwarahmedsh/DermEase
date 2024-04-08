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


        public userModel(int id, String name, String email, String phone, String password) {
            this.UserID = id ;
            this.Username = name;
            this.Email = email;
            this.PhoneNumber = phone;
            this.Password = password;
            this.reservations = new ArrayList<>();
        }

        // Constructor including reservation properties

        @NonNull
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("UserID: ").append(UserID).append('\n')
                    .append("Username: ").append(Username).append('\n')
                    .append("Email: ").append(Email).append('\n')
                    .append("PhoneNumber: ").append(PhoneNumber).append('\n')
                    .append("Password: ").append(Password).append('\n')
                    .append("Reservations:");

            for (reservationModel reservation : reservations) {
                stringBuilder.append(reservation.toString()).append("\n");
            }

            return stringBuilder.toString();
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

    public List<reservationModel> getReservations() {
        return new ArrayList<>(reservations);
    }

         public void addReservation(reservationModel reservation) {
        this.reservations.add(reservation);
        }

        public void removeReservation(reservationModel reservation) {
        this.reservations.remove(reservation);
       }

    }




