package edu.uga.cs.ridingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewRideRequest extends AppCompatActivity {

    private static final String TAG = "NewRideRequest"; // Define a TAG for logging

    private TextView riderName;
    private TextView date;
    private TextView time;
    private TextView pickup;
    private TextView dropoff;

    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ride_request);

        riderName = findViewById(R.id.rideRequest_editText1); // Update the EditText ID
        date = findViewById(R.id.rideRequest_editText2); // Update the EditText ID
        time = findViewById(R.id.rideRequest_editText3); // Update the EditText ID
        pickup = findViewById(R.id.rideRequest_editText4); // Update the EditText ID
        dropoff = findViewById(R.id.rideRequest_editText5); // Update the EditText ID
        saveButton = findViewById(R.id.request_button); // Update the Button ID

        // Set a click listener for the save button
        saveButton.setOnClickListener(new ButtonClickListener());
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String riderNameText = riderName.getText().toString();
            String dateText = date.getText().toString();
            String timeText = time.getText().toString();
            String pickupText = pickup.getText().toString();
            String dropoffText = dropoff.getText().toString();

            final RideRequest rideRequest = new RideRequest(riderNameText, timeText, dateText, pickupText, dropoffText);

            // Add a new element (RideRequest) to the list of ride requests in Firebase.
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("RideRequests");

            // First, a call to push() appends a new node to the existing list (one is created
            // if this is done for the first time). Then, we set the value in the newly created
            // list node to store the new ride request.
            // This listener will be invoked asynchronously.
            myRef.push().setValue(rideRequest)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Show a quick confirmation
                            Toast.makeText(getApplicationContext(), "Ride request created for " + rideRequest.getRiderName(),
                                    Toast.LENGTH_SHORT).show();

                            // Clear the TextViews for next use.
                            riderName.setText("");
                            date.setText("");
                            time.setText("");
                            pickup.setText("");
                            dropoff.setText("");

                            // Log success
                            Log.d(TAG, "Ride request created successfully");

                            // Navigate to RiderActivity
                            Intent intent = new Intent(NewRideRequest.this, RiderActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to create a ride request for " + rideRequest.getRiderName(),
                                    Toast.LENGTH_SHORT).show();

                            // Log failure
                            Log.e(TAG, "Failed to create a ride request", e);
                        }
                    });



        }
    }
}
