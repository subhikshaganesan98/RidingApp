package edu.uga.cs.ridingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewRideOffer extends AppCompatActivity {

    private static final String TAG = "NewRideOffer"; // Define a TAG for logging

    private TextView driverName;
    private TextView date;
    private TextView time;
    private TextView pickup;
    private TextView dropoff;

    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ride_offer);

        driverName = findViewById(R.id.rideOfferDialog_editText1);
        date = findViewById(R.id.editText2);
        time = findViewById(R.id.editText3);
        pickup = findViewById(R.id.editText4);
        dropoff = findViewById(R.id.editText5);
        saveButton = findViewById(R.id.button);

        // Set a click listener for the save button
        saveButton.setOnClickListener(new ButtonClickListener());
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String driverNameText = driverName.getText().toString();
            String dateText = date.getText().toString();
            String timeText = time.getText().toString();
            String pickupText = pickup.getText().toString();
            String dropoffText = dropoff.getText().toString();

            final RideOffer rideOffer = new RideOffer(driverNameText, timeText, dateText, pickupText, dropoffText);

            // Add a new element (RideOffer) to the list of ride offers in Firebase.
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("RideOffers");

            // First, a call to push() appends a new node to the existing list (one is created
            // if this is done for the first time). Then, we set the value in the newly created
            // list node to store the new ride offer.
            // This listener will be invoked asynchronously.
            myRef.push().setValue(rideOffer)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Show a quick confirmation
                            Toast.makeText(getApplicationContext(), "Ride offer created for " + rideOffer.getDriverName(),
                                    Toast.LENGTH_SHORT).show();

                            Log.d("Ride offer created for ", rideOffer.getDriverName());

                            // Clear the TextViews for next use.
                            driverName.setText("");
                            date.setText("");
                            time.setText("");
                            pickup.setText("");
                            dropoff.setText("");

                            // Log success
                            Log.d(TAG, "Ride offer created successfully");

                            // Navigate to RiderActivity
                            Intent intent = new Intent(NewRideOffer.this, DriverActivity.class);
                            startActivity(intent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to create a ride offer for " + rideOffer.getDriverName(),
                                    Toast.LENGTH_SHORT).show();

                            // Log failure
                            Log.e(TAG, "Failed to create a ride offer", e);
                        }
                    });
        }
    }
}
