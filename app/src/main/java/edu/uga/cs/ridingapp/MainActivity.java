package edu.uga.cs.ridingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;

import android.widget.Button;

import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView nvDrawer;
    private FirebaseDatabase database;
    private Button riderButton;
    private Button drvierButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Assigning ID of the toolbar to a variable
        toolbar = findViewById(R.id.toolbar);

        // Using toolbar as ActionBar
        setSupportActionBar(toolbar);

        // Displaying Up icon (<-), replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = findViewById(R.id.nvView);
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            // Set "Signed in as:" as a default value
            MenuItem accountMenuItem = nvDrawer.getMenu().findItem(R.id.account);
            if (accountMenuItem != null) {
                accountMenuItem.setTitle("Signed in as: " + user.getEmail());
            }
        }

        // "I am rider" Button
        riderButton = findViewById(R.id.button2);
        riderButton.setOnClickListener(new RidderButtonClickListener());

        // "I am driver" Button
        riderButton = findViewById(R.id.button3);
        riderButton.setOnClickListener(new DriverButtonClickListener());
    }


    private class RidderButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // start the user registration activity
            Intent intent = new Intent(view.getContext(), RiderActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class DriverButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // start the user registration activity
            Intent intent = new Intent(view.getContext(), DriverActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create an intent to launch activities based on nav item clicked
        Intent intent;
        if(menuItem.getItemId() == R.id.userPoints){
            intent = new Intent(getApplicationContext(), UserPointsActivity.class);
            startActivity(intent);
            finish();
        }
        else if (menuItem.getItemId() == R.id.changePassword) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
            EditText oPassword = dialogView.findViewById(R.id.passwordEt);
            EditText nPassword = dialogView.findViewById(R.id.cPasswordEt);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialogView.findViewById(R.id.updatePasswordBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentPassword = oPassword.getText().toString().trim();
                    String newPassword = nPassword.getText().toString().trim();
                    //validate data
                    if (TextUtils.isEmpty(currentPassword)) {
                        Toast.makeText(MainActivity.this, "Enter your current password.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newPassword.length() < 5) {
                        Toast.makeText(MainActivity.this, "Password length must be minimum 5 characters", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    updatePassword(currentPassword, newPassword);
                }
            });
            dialogView.findViewById(R.id.cancelCPassword).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            dialog.show();

        }
        else if (menuItem.getItemId() == R.id.deleteAcc) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userEmail = user.getEmail();
                deleteUserAccount(userEmail);
            }
        }
        else if (menuItem.getItemId() == R.id.logoutUser) {
            FirebaseAuth.getInstance().signOut();
            intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }


    // Function to delete user account and associated data
    private void deleteUserAccount(String userEmail) {
        DatabaseReference usersRef = database.getReference("users"); // Assuming "users" is the top-level node in your database
        Query query = usersRef.orderByChild("email").equalTo(userEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Delete the user based on the snapshot key (username in your case)
                    String usernameToDelete = userSnapshot.getKey();
                    deleteUserData(usernameToDelete);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Log.e("Firebase", "Error querying user data", databaseError.toException());
            }
        });
    }

    // Function to delete user data from the Realtime Database
    private void deleteUserData(String usernameToDelete) {
        DatabaseReference userToDeleteRef = database.getReference("users").child(usernameToDelete);
        userToDeleteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // User data successfully deleted
                    Toast.makeText(MainActivity.this, "User data deleted successfully.", Toast.LENGTH_SHORT).show();
                    // Now, delete the user account
                    deleteUserAccount();
                } else {
                    // Handle the error
                    Toast.makeText(MainActivity.this, "Failed to delete user data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Function to delete the user account from Firebase Authentication
    private void deleteUserAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Account removed successfully
                        Toast.makeText(MainActivity.this, "Account removed successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                    } else {
                        // Failed to remove account
                        Toast.makeText(MainActivity.this, "Failed to remove account.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void updatePassword(String oldPassword, String newPassword) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //password updated
                                        dialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //failed updating password
                                        dialog.dismiss();
                                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // Make sure to pass in a valid toolbar reference
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        // Close the drawer if it's open. Otherwise, let the default behavior handle it.
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
