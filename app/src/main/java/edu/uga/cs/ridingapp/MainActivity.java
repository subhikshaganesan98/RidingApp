package edu.uga.cs.ridingapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView nvDrawer;

    private Button riderButton;
    private Button drvierButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else{
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
         if (menuItem.getItemId() == R.id.changePassword) {
            intent = new Intent(this, RideOffers.class);
            startActivity(intent);
         }
         else if (menuItem.getItemId() == R.id.deleteAcc) {
             FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
             if (user != null) {
                 user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful()) {
                             Toast.makeText(MainActivity.this, "Account removed successfully.", Toast.LENGTH_SHORT).show();
                             Intent intent = new Intent(MainActivity.this, Login.class);
                             startActivity(intent);
                         }
                         else {
                             Toast.makeText(MainActivity.this, "Failed to remove account.", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
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