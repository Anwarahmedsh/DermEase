package com.example.sprint1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class AppointmentActivity extends AppCompatActivity {

    Intent intent;
    int userId;
    DBHelper dbHelper;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myappointmentpge);
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.navHome);

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

            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle edit button click
                    int reservationId = reservation.get_reservation_id();
                    Intent intent = new Intent(AppointmentActivity.this, editappointmentinfoActivity.class);
                    // Pass the reservation ID to the EditAppointmentActivity
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
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Intent intent2;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navHome) {
                    // Start HomeActivity
                    intent2 = new Intent(AppointmentActivity.this, HomeActivity.class);
                    intent2.putExtra("userID", userId);
                    startActivity(intent2);
                    return true;
                } else if (item.getItemId() == R.id.navRes) {
                    // Start AppointmentActivity
                    intent2 = new Intent(AppointmentActivity.this, AppointmentActivity.class);
                    intent2.putExtra("userID", userId);
                    startActivity(intent2);
                    return true;
                } else if (item.getItemId() == R.id.navprofile) {
                    // Start ProfileActivity
                    intent2 = new Intent(AppointmentActivity.this, profile.class);
                    intent2.putExtra("userID", userId);
                    startActivity(intent2);
                    return true;
                }
                return false;
            }
        });
    }
}
