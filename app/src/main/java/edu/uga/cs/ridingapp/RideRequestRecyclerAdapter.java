package edu.uga.cs.ridingapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RideRequestRecyclerAdapter extends RecyclerView.Adapter<RideRequestRecyclerAdapter.RideRequestHolder> {

    public static final String DEBUG_TAG = "RideRequestRecyclerAdapter";

    private List<RideRequest> rideRequestList;
    private Context context;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    public RideRequestRecyclerAdapter(List<RideRequest> rideRequestList, Context context) {
        this.rideRequestList = rideRequestList;
        this.context = context;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class RideRequestHolder extends RecyclerView.ViewHolder {

        TextView riderName;
        TextView date;
        TextView time;
        TextView pickup;
        TextView dropoff;

        Button acceptRequestButton;

        public RideRequestHolder(View itemView) {
            super(itemView);

            riderName = itemView.findViewById(R.id.rideRequests_riderName_TextView);
            date = itemView.findViewById(R.id.rideRequests_date_textView);
            time = itemView.findViewById(R.id.rideRequests_time_TextView);
            pickup = itemView.findViewById(R.id.rideRequests_pickup_textView);
            dropoff = itemView.findViewById(R.id.rideRequests_destination_TextView);

            acceptRequestButton = itemView.findViewById(R.id.rideRequests_accept_button);
        }
    }

    @NonNull
    @Override
    public RideRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ride_requests, parent, false);
        return new RideRequestHolder(view);
    }

    // This method fills in the values of the Views to show a RideRequests
    @Override
    public void onBindViewHolder(RideRequestHolder holder, int position) {
        RideRequest rideRequest = rideRequestList.get(position);

        Log.d(DEBUG_TAG, "onBindViewHolder: " + rideRequest);

        String key = rideRequest.getKey();
        String riderName = rideRequest.getRiderName();
        String date = rideRequest.getDate();
        String time = rideRequest.getTime();
        String pickup = rideRequest.getPickup();
        String dropoff = rideRequest.getDropoff();

        holder.riderName.setText(riderName);
        holder.date.setText(date);
        holder.time.setText(time);
        holder.pickup.setText(pickup);
        holder.dropoff.setText(dropoff);

        holder.acceptRequestButton.setOnClickListener(new AcceptButtonClickListener(position));

        // We can attach an OnClickListener to the itemView of the holder;
        // itemView is a public field in the Holder class.
        // It will be called when the user taps/clicks on the whole item, i.e., one of
        // the ride requests shown.
        // This will indicate that the user wishes to edit (modify or delete) this item.
        // We create and show an EditRideRequestDialogFragment.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditRideRequestDialogFragment editRideRequestDialogFragment =
                        EditRideRequestDialogFragment.newInstance(holder.getAdapterPosition(), key, riderName, date, time, pickup, dropoff);
                editRideRequestDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), null);
            }
        });
    }


    private class AcceptButtonClickListener implements View.OnClickListener {
        int position; // retrieves ride request from list
        public AcceptButtonClickListener (int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            RideRequest rideRequest = rideRequestList.get(position);

            database = FirebaseDatabase.getInstance();
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            Log.d(DEBUG_TAG, "User Driver Email is: " + user.getEmail());
            //create a new Accepted Ride from rideRequest values
            AcceptedRequest acceptedRide = new AcceptedRequest(user.getEmail(), rideRequest.getRiderName(),
                    rideRequest.getDate(), rideRequest.getTime(), rideRequest.getPickup(),
                    rideRequest.getDropoff(), 50, false);

            // Add a new element (AcceptedRides) to the list of ride requests in Firebase.
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("AcceptedRequests");

            myRef.push().setValue(acceptedRide)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                            Log.d("Accepted Ride Requests created for ", acceptedRide.getDriverName());

                            // Log success
                            Log.d("RideRequestRecylcerAdapter", "Accepted Ride request created successfully");


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Log failure
                            Log.d("RideRequestRecylcerAdapter", "Failed to create a accepted ride request");
                        }
                    });

            // delete Unaccpeted RideRequest from the RideRequests List


            Log.d(DEBUG_TAG, "Deleting Unaccpeted ride request at: " + position + "(" + rideRequest.getRiderName() + ")");

            // remove the deleted ride request from the list (internal list in the App)
            rideRequestList.remove(position);

            Log.d(DEBUG_TAG, "Deleted Unaccpeted ride request after ArrayList at: " + position + "(" + rideRequest.getRiderName() + ")");

            // Update the recycler view to remove the deleted ride request from that view
            notifyItemRemoved(position);

            Log.d(DEBUG_TAG, "Deleted Unaccpeted ride request after RecyclerAdapter at: " + position + "(" + rideRequest.getRiderName() + ")");

            Log.d(DEBUG_TAG, "Deleted Unaccpeted ride request key: " + rideRequest.getKey());

            // Delete this ride request in Firebase.
            // Note that we are using a specific key (one child in the list)
            DatabaseReference ref = database
                    .getReference()
                    .child("RideRequests")
                    .child(rideRequest.getKey());

            Log.d(DEBUG_TAG, "Deleted Unaccpeted ride request after Database Ref at: " + position + "(" + rideRequest.getRiderName() + ")");

            // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
            // to maintain ride requets.
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(DEBUG_TAG, "deleted unaccepted ride requests at: " + position + "(" + rideRequest.getRiderName() + ")");

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(DEBUG_TAG, "failed to delete ride request at: " + position + "(" + rideRequest.getRiderName() + ")");

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return rideRequestList.size();
    }
}
