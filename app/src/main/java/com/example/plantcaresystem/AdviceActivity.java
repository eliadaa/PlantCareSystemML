package com.example.plantcaresystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class AdviceActivity extends AppCompatActivity {

    private RecyclerView adviceList;
    private AdviceListAdapter adviceListAdapter;
    private List<AdviceListModel> adviceListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_advice);

        initBottomNavigation();
        initViews();
        initAdviceList();
        setAdviceListRecyclerView();
        Toast.makeText(this, ""+adviceListModel.size(), Toast.LENGTH_SHORT).show();
    }

    private void initAdviceList() {
        adviceListModel = new ArrayList<>();

        if (CurrentLoggedUser.getInstance().getCurrentTempWarning().equals(WarningLevel.NORMAL) &&
                CurrentLoggedUser.getInstance().getCurrentHumWarning().equals(WarningLevel.NORMAL) &&
                CurrentLoggedUser.getInstance().getCurrentLumWarning().equals(WarningLevel.NORMAL) &&
                CurrentLoggedUser.getInstance().getCurrentMoistWarning().equals(WarningLevel.NORMAL)) {
            adviceListModel.add(new AdviceListModel(getString(R.string.normal_behavior_title),
                    getString(R.string.good_behaviour_advice)));
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
    }

    private void initViews() {
        adviceList = findViewById(R.id.rv_advice_list);
    }

    private void setAdviceListRecyclerView() {
        initAdviceList();
        adviceListAdapter = new AdviceListAdapter(adviceListModel);
        adviceList.setLayoutManager(new LinearLayoutManager(this));
        adviceList.setAdapter(adviceListAdapter);
    }

    private void initBottomNavigation(){
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