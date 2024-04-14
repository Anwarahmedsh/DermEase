package com.example.sprint1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AppointmentActivity extends AppCompatActivity {

    Intent intent;
    int userId;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myappointmentpge);

        userId = getIntent().getIntExtra("userID", 0);
        dbHelper = new DBHelper(this);

        // Fetch reservations for the logged-in user
        List<reservationModel> reservations = dbHelper.getReservationsForUser(userId);

        // Assuming you have a reference to the parent layout
        LinearLayout parentLayout = findViewById(R.id.parentLayout);

        // Loop through the reservations and create CardViews dynamically
        for (reservationModel reservation : reservations) {
            // Inflate the layout for the card view
            View cardViewContent = getLayoutInflater().inflate(R.layout.item_appointment, null);

            // Get references to views in the card view content
            TextView textViewServiceType = cardViewContent.findViewById(R.id.textViewServiceType);
            TextView textViewDoctorName = cardViewContent.findViewById(R.id.textViewDoctorName);
            TextView textViewDate = cardViewContent.findViewById(R.id.textViewDate);
            TextView textViewTime = cardViewContent.findViewById(R.id.textViewTime);
            Button buttonEdit = cardViewContent.findViewById(R.id.buttonEdit);
            Button buttonDelete = cardViewContent.findViewById(R.id.buttonDelete);

            // Set reservation details to the TextViews
            textViewServiceType.setText(reservation.get_reservation_service());
            textViewDoctorName.setText(reservation.get_reservation_doctor());
            textViewDate.setText(reservation.get_reservation_date());
            textViewTime.setText(reservation.get_reservation_time());

            // Set click listener for edit button
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle edit button click
                    int reservationId = reservation.get_reservation_id();

                    // Create an Intent to navigate to the EditAppointmentActivity
                    Intent intent = new Intent(AppointmentActivity.this, editappointmentinfoActivity.class);
                    // Pass the reservation ID, user ID to the EditAppointmentActivity
                    intent.putExtra("RESERVATION_ID", reservationId);
                    intent.putExtra("userId", userId);

                    // Start the EditAppointmentActivity
                    startActivity(intent);
                }
            });

            // Set click listener for delete button
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle delete button click
                    dbHelper.deleteReservation(reservation);
                    // Remove the card view from the parent layout after deletion
                    parentLayout.removeView(cardViewContent);
                }
            });

            // Add the card view content to the parent layout
            parentLayout.addView(cardViewContent);
        }
    }
}
