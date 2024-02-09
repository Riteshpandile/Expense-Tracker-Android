package com.example.eptrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);

        // Set item click listener for navigation menu items
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here
                if (item.getItemId() == R.id.menu_dashboard) {
                    // Handle Dashboard click
                    showToast("Coming soon");
                } else if (item.getItemId() == R.id.nav_add_expense) {
                    // Handle Add Expense click
                    handleNavigationItemSelected(item);
                } else if (item.getItemId() == R.id.menu_total_expense) {
                    // Handle Total Expense click
                    openTotalExpenseActivity();
                }

                // Close the drawer when an item is selected
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void openTotalExpenseActivity() {
        Intent totalExpenseIntent = new Intent(this, TotalExpenseActivity.class);
        startActivity(totalExpenseIntent);
    }

    private void handleNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation item selection here
        if (menuItem.getItemId() == R.id.nav_add_expense) {
            // Open the Add Expense activity
            Intent addExpenseIntent = new Intent(this, AddExpenseActivity.class);
            startActivity(addExpenseIntent);
        }
        // Add more cases for other menu items if needed
    }
}