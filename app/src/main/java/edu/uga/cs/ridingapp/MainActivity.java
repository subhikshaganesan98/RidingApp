package edu.uga.cs.ridingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button button;
    private TextView textView;
    private FirebaseUser user;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else{
            textView.setText(user.getEmail());
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item click events here
                int itemId = item.getItemId();

                if (itemId == R.id.nav_item1) {
                    // Handle Item 1 click
                    Toast.makeText(MainActivity.this, "Item 1 clicked", Toast.LENGTH_SHORT).show();
                }
                else if (itemId == R.id.nav_item2) {
                    // Handle Item 2 click
                    Toast.makeText(MainActivity.this, "Item 2 clicked", Toast.LENGTH_SHORT).show();
                }
                else if (itemId == R.id.nav_item3) {
                    // Handle Item 3 click
                    Toast.makeText(MainActivity.this, "Item 3 clicked", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Handle Item 4 click
                    Toast.makeText(MainActivity.this, "Item 4 clicked", Toast.LENGTH_SHORT).show();
                }
                // Add more conditions as needed

                // Close the drawer after handling the item click
                drawerLayout.closeDrawers();
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
}