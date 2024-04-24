package com.example.sprint1;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class reservationModel implements Serializable {

        private int UserId;
        private int reservationId;
        private String date;
        private String time;
        private String doctorName;
        private String serviceType;

        // Constructor
        public reservationModel(int UserId, int reservationId, String date, String time, String doctorName, String serviceType) {
            this.reservationId = reservationId;
            this.date = date;
            this.time = time;
            this.doctorName = doctorName;
            this.serviceType = serviceType;
            this.UserId = UserId;
        }


    // Getters and setters
    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getUserId() {
        return UserId;
    }


        public int get_reservation_id() {
            return reservationId;
        }

        public void set_reservation_id(int reservationId) {
            this.reservationId = reservationId;
        }

        public String get_reservation_date() {
            return date;
        }

        public void set_reservation_date(String date) {
            this.date = date;
        }

        public String get_reservation_time() {
            return time;
        }

        public void set_reservation_time(String time) {
            this.time = time;
        }

        public String get_reservation_doctor() {
            return doctorName;
        }

        public void set_reservation_doctor(String doctorName) {
            this.doctorName = doctorName;
        }

        public String get_reservation_service() {
            return serviceType;
        }

        public void set_reservation_service(String serviceType) {
            this.serviceType = serviceType;
        }

    @NonNull
    public String toString() {
        return "Reservation{" + '\n'+
                "reservationId: " + reservationId + "\n" +
                "date: " + date  + "\n" +
                "time: " + time + "\n" +
                "doctorName: " + doctorName+ "\n" +
                "serviceType: " + serviceType + "\n" +
                '}';
    }
}


