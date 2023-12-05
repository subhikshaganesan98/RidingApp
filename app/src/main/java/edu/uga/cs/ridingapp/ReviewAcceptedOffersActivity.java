package edu.uga.cs.ridingapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ReviewAcceptedOffersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AcceptedOffersRecyclerAdapter recyclerAdapter;
    private List<AcceptedOffer> acceptedOffersList;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_accepted_ride_offers);

        recyclerView = findViewById(R.id.recyclerView3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        acceptedOffersList = new ArrayList<>();
        recyclerAdapter = new AcceptedOffersRecyclerAdapter(acceptedOffersList, this);
        recyclerView.setAdapter(recyclerAdapter);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        // Load accepted offers from Firebase
        loadAcceptedOffers();
    }

    private void loadAcceptedOffers() {
        // Reference to the "AcceptedOffers" node in Firebase
        DatabaseReference acceptedOffersRef = database.getReference("AcceptedOffers");

        acceptedOffersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                acceptedOffersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AcceptedOffer acceptedOffer = snapshot.getValue(AcceptedOffer.class);
                    if (acceptedOffer != null) {
                        acceptedOffersList.add(acceptedOffer);
                    }
                }

                // Notify the adapter that the data has changed
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event if needed
            }
        });
    }
}
