package com.example.plantcaresystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class AdviceActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvAdvice;

    private int currentAdviceIndex = 0;
    private int totalAdvices;
    private List<AdviceListModel> adviceListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_advice);

        initAdviceList();
        initViews();
        displayAdvice(currentAdviceIndex);
        initBottomNavigation();

        Button previousButton = findViewById(R.id.previousButton);
        previousButton.setOnClickListener(v -> showPreviousAdvice());

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> showNextAdvice());

        Toast.makeText(this, "" + adviceListModel.size(), Toast.LENGTH_SHORT).show();
    }

    private void showPreviousAdvice() {
        if (currentAdviceIndex > 0) {
            currentAdviceIndex--;
            displayAdvice(currentAdviceIndex);
        }
    }

    private void showNextAdvice() {
        if (currentAdviceIndex < totalAdvices - 1) {
            currentAdviceIndex++;
            displayAdvice(currentAdviceIndex);
        }
    }

    private void displayAdvice(int index) {
        AdviceListModel advice = adviceListModel.get(index);
        tvTitle.setText(advice.getTitle());
        tvAdvice.setText(advice.getAdvice());
    }

    private void initAdviceList() {
        adviceListModel = new ArrayList<>();

        if (CurrentLoggedUser.getInstance().getCurrentTempWarning().equals(WarningLevel.NORMAL) &&
                CurrentLoggedUser.getInstance().getCurrentHumWarning().equals(WarningLevel.NORMAL) &&
                CurrentLoggedUser.getInstance().getCurrentLumWarning().equals(WarningLevel.NORMAL) &&
                CurrentLoggedUser.getInstance().getCurrentMoistWarning().equals(WarningLevel.NORMAL)) {
            adviceListModel.add(new AdviceListModel(getString(R.string.normal_behavior_title),
                    getString(R.string.good_behavior_advice)));
        }

        if (CurrentLoggedUser.getInstance().getCurrentLumWarning().equals(WarningLevel.LOW)) {
            adviceListModel.add(new AdviceListModel(getString(R.string.low_luminosity_title),
                    getString(R.string.low_luminosity_advice)));
        }
        if (CurrentLoggedUser.getInstance().getCurrentLumWarning().equals(WarningLevel.HIGH)) {
            adviceListModel.add(new AdviceListModel(getString(R.string.high_luminosity_title),
                    getString(R.string.high_luminosity_advice)));
        }

        if (CurrentLoggedUser.getInstance().getCurrentMoistWarning().equals(WarningLevel.LOW)) {
            adviceListModel.add(new AdviceListModel(getString(R.string.low_soil_moisture_title),
                    getString(R.string.low_soil_moisture_advice)));
        }
        if (CurrentLoggedUser.getInstance().getCurrentMoistWarning().equals(WarningLevel.HIGH)) {
            adviceListModel.add(new AdviceListModel(getString(R.string.high_soil_moisture_title),
                    getString(R.string.high_soil_moisture_advice)));
        }

        if (CurrentLoggedUser.getInstance().getCurrentHumWarning().equals(WarningLevel.LOW)) {
            adviceListModel.add(new AdviceListModel(getString(R.string.low_air_humidity_title),
                    getString(R.string.low_air_humidity_advice)));
        }
        if (CurrentLoggedUser.getInstance().getCurrentHumWarning().equals(WarningLevel.HIGH)) {
            adviceListModel.add(new AdviceListModel(getString(R.string.high_air_humidity_title),
                    getString(R.string.high_air_humidity_advice)));
        }

        if (CurrentLoggedUser.getInstance().getCurrentTempWarning().equals(WarningLevel.LOW)) {
            adviceListModel.add(new AdviceListModel(getString(R.string.low_temperature_title),
                    getString(R.string.low_temperature_advice)));
        }
        if (CurrentLoggedUser.getInstance().getCurrentTempWarning().equals(WarningLevel.HIGH)) {
            adviceListModel.add(new AdviceListModel(getString(R.string.high_temperature_title),
                    getString(R.string.high_temperature_advice)));
        }

        totalAdvices = adviceListModel.size();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvAdvice = findViewById(R.id.tv_advice);
    }

    private void initBottomNavigation() {
        // init and assign var
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // set data screen selected
        bottomNavigationView.setSelectedItemId(R.id.advice_screen);

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
                    startActivity(new Intent(getApplicationContext(), DataActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.advice_screen) {
                    return true;
                }

                return false;
            }
        });
    }
}
