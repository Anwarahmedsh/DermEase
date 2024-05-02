package com.example.sprint1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.MenuInflater;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;



import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppointmentActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private LinearLayout parentLayout;
    private DBHelper dbHelper;
    private int userId;
    CardView   resCard;

    ArrayAdapter resArrayAdapter;



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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this appointment?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the reservation from the database
                dbHelper.deleteReservation(reservation);

                parentLayout.removeView(cardViewContent);
                Toast.makeText(AppointmentActivity.this, "Appointment deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String s) {
                List<reservationModel> searchResult = dbHelper.search(s);
                if (searchResult.isEmpty()) {
                    Toast.makeText(AppointmentActivity.this, "Reservation not found!", Toast.LENGTH_SHORT).show();
                } else {
                    // Update LinearLayout with search results
                    updateLinearLayout(searchResult);
                }
                return false;
            }

            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    // Method to update LinearLayout with search results
    private void updateLinearLayout(List<reservationModel> searchResult) {
        parentLayout.removeAllViews(); // Clear existing views

        for (reservationModel reservation : searchResult) {
            View appointmentView = getLayoutInflater().inflate(R.layout.item_appointment, parentLayout, false);
            setupCardView(appointmentView, reservation);
            parentLayout.addView(appointmentView);
        }
    }


}
