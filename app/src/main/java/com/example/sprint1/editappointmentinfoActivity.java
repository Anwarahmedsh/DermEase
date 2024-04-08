package com.example.sprint1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class editappointmentinfoActivity extends AppCompatActivity {

    private Spinner timeDropDown;
    private Spinner dermatologists;
    private Spinner dateSpinner; // Change to Spinner
    private Button buttonAdd;

    private DBHelper dbHelper;
    private reservationModel reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editappointmentinfo);

        timeDropDown = findViewById(R.id.timeDropDown);
        dermatologists = findViewById(R.id.dermatologists);
        dateSpinner = findViewById(R.id.dateSpinner); // Initialize Spinner
        buttonAdd = findViewById(R.id.buttonAdd);

        dbHelper = new DBHelper(this);

        reservation = getIntent().getParcelableExtra("RESERVATION");

        if (reservation == null) {
            Toast.makeText(this, "Error: Reservation not found.", Toast.LENGTH_SHORT).show();
            finish();
        }

        populateTimes();
        populateDermatologists();
        populateDates(); // Populate the Spinner with dates

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAppointment();
            }
        });
    }

    private void populateTimes() {
        // Assume you have a list of available times in an array
        String[] times = {"9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeDropDown.setAdapter(adapter);
    }

    private void populateDermatologists() {
        // Assume you have a list of dermatologists' names in an array
        String[] dermatologistsArray = {"Dr. Smith", "Dr. Johnson", "Dr. Williams", "Dr. Brown"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dermatologistsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dermatologists.setAdapter(adapter);
    }
    private void populateDates() {
        // Populate the Spinner with dates
        List<String> datesList = new ArrayList<>(); // Populate this list with dates

        // Add dates to the list (e.g., from a database or a predefined list)
        datesList.add("2024-6-19");
        datesList.add("2024-6-21");
        datesList.add("2024-7-1");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, datesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter);
    }

    private void updateAppointment() {
        String selectedTime = timeDropDown.getSelectedItem().toString();
        String selectedDoctor = dermatologists.getSelectedItem().toString();
        String selectedDate = dateSpinner.getSelectedItem().toString(); // Get selected date from Spinner

        // Update the reservation information including date
        reservation.set_reservation_date(selectedDate);
        reservation.set_reservation_time(selectedTime);
        reservation.set_reservation_doctor(selectedDoctor);

        // Update the reservation in the database
        boolean updated = dbHelper.updateReservation(reservation);

        if (updated) {
            Toast.makeText(editappointmentinfoActivity.this, "Appointment updated successfully.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(editappointmentinfoActivity.this, "Error updating appointment.", Toast.LENGTH_SHORT).show();
        }
    }
}