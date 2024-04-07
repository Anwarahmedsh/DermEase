package com.example.sprint1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myappointmentpge);


        // Assuming you have references to your CardView and the parent layout
        LinearLayout parentLayout = findViewById(R.id.parentLayout); // Assuming a LinearLayout as the parent layout
        // Assuming you have a list of reservations (userModel objects)
        List<userModel> reservations = new ArrayList<>(); // Populate this list with reservations

        // Loop through the reservations and create CardViews dynamically
        for (userModel reservation : reservations) {
            CardView cardView = new CardView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 8, 8, 8);
            cardView.setLayoutParams(params);
            cardView.setCardElevation(10);

            // Inflate the layout for the card view activity_myappointmentpge
            View cardViewContent = getLayoutInflater().inflate(R.layout.activity_myappointmentpge, null);

            // Assuming you have references to TextViews and Buttons in the inflated layout
            TextView textViewServiceType = cardViewContent.findViewById(R.id.textViewServiceType11);
            TextView textViewDoctorName = cardViewContent.findViewById(R.id.textViewDoctorName11);
            TextView textViewDate = cardViewContent.findViewById(R.id.textViewDate11);
            TextView textViewTime = cardViewContent.findViewById(R.id.textViewTime11);
            Button buttonEdit = cardViewContent.findViewById(R.id.buttonEdit11);
            Button buttonDelete = cardViewContent.findViewById(R.id.buttonDelete11);

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

                    // Pass the reservation ID to the EditAppointmentActivity
                    intent.putExtra("RESERVATION_ID", reservationId);

                    // Start the EditAppointmentActivity
                    startActivity(intent);
                }
            });

            // Add the cardViewContent to the CardView
            cardView.addView(cardViewContent);

            // Add the CardView to the parent layout
            parentLayout.addView(cardView);
        }
    }
}
