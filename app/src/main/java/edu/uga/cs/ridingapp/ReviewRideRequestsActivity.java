package edu.uga.cs.ridingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewRideRequestsActivity extends AppCompatActivity
        implements EditRideRequestDialogFragment.EditRideRequestDialogListener {

    public static final String DEBUG_TAG = "ReviewRideRequestsActivity";

    private RecyclerView recyclerView;
    private RideRequestRecyclerAdapter recyclerAdapter;

    private List<RideRequest> rideRequestsList;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(DEBUG_TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_ride_requests);

        recyclerView = findViewById(R.id.recyclerView2);

//        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton);
//        floatingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment newFragment = new EditRideOfferDialogFragment();
//                newFragment.show(getSupportFragmentManager(), null);
//            }
//        });



        // initialize the Ride Request list
        rideRequestsList = new ArrayList<RideRequest>();

        // use a linear layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // the recycler adapter with ride requests is empty at first; it will be updated later
        recyclerAdapter = new RideRequestRecyclerAdapter(rideRequestsList, ReviewRideRequestsActivity.this);
        recyclerView.setAdapter(recyclerAdapter);

        // get a Firebase DB instance reference
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RideRequests");

        // Set up a listener (event handler) to receive a value for the database reference.
        // This type of listener is called by Firebase once by immediately executing its onDataChange method
        // and then each time the value at Firebase changes.
        //
        // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
        // to maintain ride requests.
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Once we have a DataSnapshot object, we need to iterate over the elements and place them on our ride request list.
                rideRequestsList.clear(); // clear the current content; this is inefficient!
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    RideRequest rideRequest = postSnapshot.getValue(RideRequest.class);
                    rideRequest.setKey(postSnapshot.getKey());
                    rideRequestsList.add(rideRequest);
                    Log.d(DEBUG_TAG, "ValueEventListener: added: " + rideRequest);
                    Log.d(DEBUG_TAG, "ValueEventListener: key: " + postSnapshot.getKey());
                }

                Log.d(DEBUG_TAG, "ValueEventListener: notifying recyclerAdapter");
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("ValueEventListener: reading failed: " + databaseError.getMessage());
            }
        });
    }

    // This is our own callback for a DialogFragment which edits an existing RideRequest.
    // The edit may be an update or a deletion of this RideRequest.
    // It is called from the EditRideRequestDialogFragment.
    public void updateRideRequest(int position, RideRequest rideRequest, int action) {
        if (action == EditRideRequestDialogFragment.SAVE) {
            Log.d(DEBUG_TAG, "Updating ride request at: " + position + "(" + rideRequest.getRiderName() + ")");

            // Update the recycler view to show the changes in the updated ride request in that view
            recyclerAdapter.notifyItemChanged(position);

            // Update this ride request in Firebase
            // Note that we are using a specific key (one child in the list)
            DatabaseReference ref = database
                    .getReference()
                    .child("RideRequests")
                    .child(rideRequest.getKey());

            // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
            // to maintain ride requests.
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().setValue(rideRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(DEBUG_TAG, "updated ride request at: " + position + "(" + rideRequest.getRiderName() + ")");
                            Toast.makeText(getApplicationContext(), "Ride request updated for " + rideRequest.getRiderName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(DEBUG_TAG, "failed to update ride request at: " + position + "(" + rideRequest.getRiderName() + ")");
                    Toast.makeText(getApplicationContext(), "Failed to update " + rideRequest.getRiderName(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else if (action == EditRideRequestDialogFragment.DELETE) {
            Log.d(DEBUG_TAG, "Deleting ride requests at: " + position + "(" + rideRequest.getRiderName() + ")");

            // remove the deleted ride requests from the list (internal list in the App)
            rideRequestsList.remove(position);

            // Update the recycler view to remove the deleted ride requests from that view
            recyclerAdapter.notifyItemRemoved(position);

            Log.d(DEBUG_TAG, "Deleted ride request after RecyclerAdapter at: " + position + "(" + rideRequest.getRiderName() + ")");

            Log.d(DEBUG_TAG, "Deleted ride request key: " + rideRequest.getKey());

            // Delete this ride requests in Firebase.
            // Note that we are using a specific key (one child in the list)
            DatabaseReference ref = database
                    .getReference()
                    .child("RideRequests")
                    .child(rideRequest.getKey());

            Log.d(DEBUG_TAG, "Deleted ride request after database ref of key at: " + position + "(" + rideRequest.getRiderName() + ")");

            // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
            // to maintain ride requests.
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(DEBUG_TAG, "deleted ride request at: " + position + "(" + rideRequest.getRiderName() + ")");
                            Toast.makeText(getApplicationContext(), "Ride request deleted for " + rideRequest.getRiderName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(DEBUG_TAG, "failed to delete ride request at: " + position + "(" + rideRequest.getRiderName() + ")");
                    Toast.makeText(getApplicationContext(), "Failed to delete " + rideRequest.getRiderName(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
