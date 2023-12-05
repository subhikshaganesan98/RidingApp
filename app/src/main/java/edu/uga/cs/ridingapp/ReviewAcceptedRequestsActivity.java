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

public class ReviewAcceptedRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AcceptedRequestsRecyclerAdapter recyclerAdapter;
    private List<AcceptedRequest> acceptedRequestsList;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_accepted_ride_requests);

        recyclerView = findViewById(R.id.recyclerView4);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        acceptedRequestsList = new ArrayList<>();
        recyclerAdapter = new AcceptedRequestsRecyclerAdapter(acceptedRequestsList, this);
        recyclerView.setAdapter(recyclerAdapter);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        // Load accepted requests from Firebase
        loadAcceptedRequests();
    }

    private void loadAcceptedRequests() {
        // Reference to the "AcceptedRequests" node in Firebase
        DatabaseReference acceptedRequestsRef = database.getReference("AcceptedRequests");

        acceptedRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                acceptedRequestsList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AcceptedRequest acceptedRequest = snapshot.getValue(AcceptedRequest.class);
                    if (acceptedRequest != null) {
                        acceptedRequestsList.add(acceptedRequest);
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
