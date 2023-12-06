package edu.uga.cs.ridingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserPointsActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private TextView userPointsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_points);

        // Initialize Firebase components
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users"); // Assuming "users" is the node where user data is stored
        auth = FirebaseAuth.getInstance();

        // Initialize UI components
        userPointsTextView = findViewById(R.id.textView2);

        // Get current user's email from Firebase Authentication
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String currentEmail = currentUser.getEmail();

            // Query the database to find the user with matching email
            Query query = reference.orderByChild("email").equalTo(currentEmail);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User with matching email found
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HelperClass helperClass = snapshot.getValue(HelperClass.class);
                            if (helperClass != null) {
                                // Display user points in TextView
                                String userPoints = String.valueOf(helperClass.getUserPoints());
                                userPointsTextView.setText("User Points: " + userPoints);
                            }
                        }
                    } else {
                        // No user found with matching email
                        userPointsTextView.setText("User not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error
                    Toast.makeText(UserPointsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // User not authenticated, handle accordingly
            // For example, you may redirect to the login screen
            startActivity(new Intent(UserPointsActivity.this, Login.class));
            finish();
        }
    }
}

