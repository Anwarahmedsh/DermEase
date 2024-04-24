package com.example.sprint1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppointmentActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private LinearLayout parentLayout;
    private DBHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myappointmentpge);

        initializeViews();
        initializeNavigation();
        loadAppointments();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments();  // This will refresh the appointment list every time the activity resumes
    }

    private void initializeViews() {
        bottomNavigationView = findViewById(R.id.bottomNav);
        parentLayout = findViewById(R.id.parentLayout);
        dbHelper = new DBHelper(this);
        userId = getIntent().getIntExtra("userID", 0);
        bottomNavigationView.setSelectedItemId(R.id.navHome);
    }

    private void initializeNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navHome) {
                    navigate(HomeActivity.class);
                    return true;
                } else if (id == R.id.navprofile) {
                    navigate(profile.class);
                    return true;
                }
                return false;
            }

        });
    }

    private void navigate(Class<?> activityClass) {
        Intent intent = new Intent(AppointmentActivity.this, activityClass);
        intent.putExtra("userID", userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    private void loadAppointments() {
        parentLayout.removeAllViews(); // Clear all existing views to avoid duplicate entries

        List<reservationModel> reservations = dbHelper.getReservationsForUser(userId);

        if (reservations.isEmpty()) {
            Log.d("AppointmentActivity", "No appointments found for user: " + userId);
        } else {
            for (reservationModel reservation : reservations) {
                View appointmentView = getLayoutInflater().inflate(R.layout.item_appointment, parentLayout, false);
                setupCardView(appointmentView, reservation);
                parentLayout.addView(appointmentView);
            }
        }
    }


    private void setupCardView(View cardViewContent, reservationModel reservation) {
        TextView textViewServiceType = cardViewContent.findViewById(R.id.textViewServiceType);
        TextView textViewDoctorName = cardViewContent.findViewById(R.id.textViewDoctorName);
        TextView textViewDate = cardViewContent.findViewById(R.id.textViewDate);
        TextView textViewTime = cardViewContent.findViewById(R.id.textViewTime);
        Button buttonEdit = cardViewContent.findViewById(R.id.buttonEdit);
        Button buttonDelete = cardViewContent.findViewById(R.id.buttonDelete);

        textViewServiceType.setText(reservation.get_reservation_service());
        textViewDoctorName.setText(reservation.get_reservation_doctor());
        textViewDate.setText(reservation.get_reservation_date());
        textViewTime.setText(reservation.get_reservation_time());

        buttonEdit.setOnClickListener(v -> editAppointment(reservation));
        buttonDelete.setOnClickListener(v -> deleteAppointment(cardViewContent, reservation));
    }

    private void editAppointment(reservationModel reservation) {
        Intent intent = new Intent(AppointmentActivity.this, editappointmentinfoActivity.class);
        intent.putExtra("Reservation", reservation);
        intent.putExtra("userID", userId);
        startActivity(intent);
    }

    private void deleteAppointment(View cardViewContent, reservationModel reservation) {
        dbHelper.deleteReservation(reservation);
        parentLayout.removeView(cardViewContent); // Remove the card view from the parent layout
    }
}
