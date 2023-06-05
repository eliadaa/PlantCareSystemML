package com.example.plantcaresystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DataActivity extends AppCompatActivity {

    // with this class the user can set the data and parameters wanted for his specific plant
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        // init and assign var
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // set data screen selected
        bottomNavigationView.setSelectedItemId(R.id.data_screen);

        // perform listener on selected
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.plant_screen) {
                    startActivity(new Intent(getApplicationContext(), PlantActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.data_screen) {
                    return true;
                } else if (itemId == R.id.advice_screen) {
                    startActivity(new Intent(getApplicationContext(), AdviceActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }

                return false;
            }
        });
    }
}