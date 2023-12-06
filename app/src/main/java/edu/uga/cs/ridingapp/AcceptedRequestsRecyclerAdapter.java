package edu.uga.cs.ridingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AcceptedRequestsRecyclerAdapter extends RecyclerView.Adapter<AcceptedRequestsRecyclerAdapter.AcceptedRequestHolder> {

    public static final String DEBUG_TAG = "AcceptedRequestsRecyclerAdapter";

    private List<AcceptedRequest> acceptedRequestList;
    private Context context;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    public AcceptedRequestsRecyclerAdapter(List<AcceptedRequest> acceptedRequestList, Context context) {
        this.acceptedRequestList = acceptedRequestList;
        this.context = context;
    }

    class AcceptedRequestHolder extends RecyclerView.ViewHolder {

        TextView driverName;
        TextView riderName;
        TextView date;
        TextView time;
        TextView pickup;
        TextView dropoff;
        Button confirmRequestButton;

        public AcceptedRequestHolder(View itemView) {
            super(itemView);

            riderName = itemView.findViewById(R.id.accepted_rideRequests_riderName_TextView);
            driverName = itemView.findViewById(R.id.accepted_rideRequests_driverName_TextView);
            date = itemView.findViewById(R.id.accepted_rideRequests_date_textView);
            time = itemView.findViewById(R.id.accepted_rideRequests_time_TextView);
            pickup = itemView.findViewById(R.id.accepted_rideRequests_pickup_textView);
            dropoff = itemView.findViewById(R.id.accepted_rideRequests_destination_TextView);

            confirmRequestButton = itemView.findViewById(R.id.accepted_rideRequests_accept_button);
        }
    }

    @NonNull
    @Override
    public AcceptedRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_accepted_requests, parent, false);
        return new AcceptedRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(AcceptedRequestHolder holder, int position) {
        AcceptedRequest acceptedRequest = acceptedRequestList.get(position);

        Log.d(DEBUG_TAG, "onBindViewHolder: " + acceptedRequest);

        String driverName = acceptedRequest.getDriverName();
        String riderName = acceptedRequest.getRiderName();
        String date = acceptedRequest.getDate();
        String time = acceptedRequest.getTime();
        String pickup = acceptedRequest.getPickup();
        String dropoff = acceptedRequest.getDropoff();

        holder.driverName.setText(driverName);
        holder.riderName.setText(riderName);
        holder.date.setText(date);
        holder.time.setText(time);
        holder.pickup.setText(pickup);
        holder.dropoff.setText(dropoff);

        // Add a click listener for the confirm button
        holder.confirmRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    // Get the current user's email
                    String currentEmail = currentUser.getEmail();

                    // Query the database to find the user with the matching email
                    reference.orderByChild("email").equalTo(currentEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Loop through the results (there should be only one match)
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    // Get the userPoints snapshot
                                    DataSnapshot userPointsSnapshot = snapshot.child("userPoints");

                                    // Check if the "userPoints" node exists
                                    if (userPointsSnapshot.exists()) {
                                        // Get the current userPoints value
                                        Object rawUserPoints = userPointsSnapshot.getValue();

                                        // Check if rawUserPoints is not null
                                        if (rawUserPoints != null) {
                                            // Attempt to convert the value to Integer
                                            try {
                                                Integer existingUserPoints = Integer.valueOf(rawUserPoints.toString());

                                                // Add 50 to the existing userPoints
                                                int updatedUserPoints = existingUserPoints - 50;

                                                // Update the userPoints in the database
                                                reference.child(snapshot.getKey()).child("userPoints").setValue(updatedUserPoints);

                                                // Access the context from the adapter
                                                if (context != null) {
                                                    Toast.makeText(context, "UserPoints are updated", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.e("TAG", "Context is null");
                                                }
                                            } catch (NumberFormatException e) {
                                                // Handle the case where the "userPoints" value cannot be converted to Integer
                                                Log.e("TAG", "Error converting userPoints to Integer: " + e.getMessage());
                                            }
                                        } else {
                                            // Handle the case where rawUserPoints is null
                                            // This may happen if the "userPoints" value is missing or null in the database
                                            Log.e("TAG", "userPoints value is missing or null in the database");
                                        }
                                    } else {
                                        // Handle the case where the "userPoints" node does not exist in the database
                                        Log.e("TAG", "userPoints node does not exist in the database");
                                    }
                                }
                            } else {
                                // No user found with the current user's email
                                if (context != null) {
                                    Toast.makeText(context, "No user found with the current user's email", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("TAG", "Context is null");
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("TAG", "Database error: " + databaseError.getMessage());
                        }
                    });
                } else {
                    // User is not signed in
                    if (context != null) {
                        Toast.makeText(context, "User is not signed in", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("TAG", "Context is null");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return acceptedRequestList.size();
    }
}
