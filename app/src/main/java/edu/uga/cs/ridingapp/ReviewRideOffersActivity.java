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

public class ReviewRideOffersActivity extends AppCompatActivity
        implements EditRideOfferDialogFragment.EditRideOfferDialogListener {

    public static final String DEBUG_TAG = "ReviewRideOfferActivity";

    private RecyclerView recyclerView;
    private RideOfferRecyclerAdapter recyclerAdapter;

    private List<RideOffer> rideOffersList;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(DEBUG_TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_ride_offers);

        recyclerView = findViewById(R.id.recyclerView);

//        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton);
//        floatingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment newFragment = new EditRideOfferDialogFragment();
//                newFragment.show(getSupportFragmentManager(), null);
//            }
//        });



        // initialize the Ride Offer list
        rideOffersList = new ArrayList<RideOffer>();

        // use a linear layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // the recycler adapter with ride offers is empty at first; it will be updated later
        recyclerAdapter = new RideOfferRecyclerAdapter(rideOffersList, ReviewRideOffersActivity.this);
        recyclerView.setAdapter(recyclerAdapter);

        // get a Firebase DB instance reference
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RideOffers");

        // Set up a listener (event handler) to receive a value for the database reference.
        // This type of listener is called by Firebase once by immediately executing its onDataChange method
        // and then each time the value at Firebase changes.
        //
        // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
        // to maintain ride offers.
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Once we have a DataSnapshot object, we need to iterate over the elements and place them on our ride offer list.
                rideOffersList.clear(); // clear the current content; this is inefficient!
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    RideOffer rideOffer = postSnapshot.getValue(RideOffer.class);
                    rideOffer.setKey(postSnapshot.getKey());
                    rideOffersList.add(rideOffer);
                    Log.d(DEBUG_TAG, "ValueEventListener: added: " + rideOffer);
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

    // This is our own callback for a DialogFragment which edits an existing RideOffer.
    // The edit may be an update or a deletion of this RideOffer.
    // It is called from the EditRideOfferDialogFragment.
    public void updateRideOffer(int position, RideOffer rideOffer, int action) {
        if (action == EditRideOfferDialogFragment.SAVE) {
            Log.d(DEBUG_TAG, "Updating ride offer at: " + position + "(" + rideOffer.getDriverName() + ")");

            // Update the recycler view to show the changes in the updated ride offer in that view
            recyclerAdapter.notifyItemChanged(position);

            // Update this ride offer in Firebase
            // Note that we are using a specific key (one child in the list)
            DatabaseReference ref = database
                    .getReference()
                    .child("RideOffers")
                    .child(rideOffer.getKey());

            // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
            // to maintain ride offers.
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().setValue(rideOffer).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(DEBUG_TAG, "updated ride offer at: " + position + "(" + rideOffer.getDriverName() + ")");
                            Toast.makeText(getApplicationContext(), "Ride offer updated for " + rideOffer.getDriverName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(DEBUG_TAG, "failed to update ride offer at: " + position + "(" + rideOffer.getDriverName() + ")");
                    Toast.makeText(getApplicationContext(), "Failed to update " + rideOffer.getDriverName(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else if (action == EditRideOfferDialogFragment.DELETE) {
            Log.d(DEBUG_TAG, "Deleting ride offer at: " + position + "(" + rideOffer.getDriverName() + ")");

            // remove the deleted ride offer from the list (internal list in the App)
            rideOffersList.remove(position);

            Log.d(DEBUG_TAG, "Deleted ride offer after ArrayList at: " + position + "(" + rideOffer.getDriverName() + ")");

            // Update the recycler view to remove the deleted ride offer from that view
            recyclerAdapter.notifyItemRemoved(position);

            Log.d(DEBUG_TAG, "Deleted ride offer after RecyclerAdapter at: " + position + "(" + rideOffer.getDriverName() + ")");

            // Delete this ride offer in Firebase.
            // Note that we are using a specific key (one child in the list)
            DatabaseReference ref = database
                    .getReference()
                    .child("RideOffers")
                    .child(rideOffer.getKey());

            Log.d(DEBUG_TAG, "Deleted ride offer after Database Ref at: " + position + "(" + rideOffer.getDriverName() + ")");

//            // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
//            // to maintain ride offers.
//            ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d(DEBUG_TAG, "deleted ride offer at: " + position + "(" + rideOffer.getDriverName() + ")");
//                            Toast.makeText(getApplicationContext(), "Ride offer deleted for " + rideOffer.getDriverName(),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Log.d(DEBUG_TAG, "failed to delete ride offer at: " + position + "(" + rideOffer.getDriverName() + ")");
//                    Toast.makeText(getApplicationContext(), "Failed to delete " + rideOffer.getDriverName(),
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }
}
