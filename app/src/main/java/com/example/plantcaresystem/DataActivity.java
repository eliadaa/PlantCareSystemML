package com.example.plantcaresystem;

import static com.example.plantcaresystem.Databases.PlantsDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final DatabaseReference PlantsData = FirebaseDatabase.getInstance().getReference("Plants");
    private Spinner spinner_names;
    private String myPlantName;
    private EditText editTextMyPlantName, editTextMinMoisture, editTextMaxMoisture, editTextMinTemp, editTextMaxTemp, editTextMinHumidity, editTextMaxHumidity, editTextMinLuminosity, editTextMaxLuminosity;

    BottomNavigationView bottomNavigationView;
    TextView text_view_info;
    String actual_plant_data;

    // with this class the user can set the data and parameters wanted for his specific plant
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_data);
        // init and assign var
        // set data screen selected
        setButtonNavigation();
        initializeViews();
        getPredefinedPlantsIntoSpinner();

    }

    private void setButtonNavigation(){

        bottomNavigationView = findViewById(R.id.bottom_navigation);
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


    private void initializeViews() {
        editTextMyPlantName = findViewById(R.id.et_plantName);
        editTextMinHumidity = findViewById(R.id.et_minAirHum);
        editTextMaxHumidity = findViewById(R.id.et_maxAirHum);
        editTextMinLuminosity = findViewById(R.id.et_minLumin);
        editTextMaxLuminosity = findViewById(R.id.et_maxLumin);
        editTextMinTemp = findViewById(R.id.et_minTemp);
        editTextMaxTemp = findViewById(R.id.et_maxTemp);
        editTextMinMoisture = findViewById(R.id.et_minMoist);
        editTextMaxMoisture = findViewById(R.id.et_maxMoist);
        spinner_names = findViewById(R.id.spinner_type);
        text_view_info = findViewById(R.id.tv_actual_data);
        // spinner_names.setOnItemSelectedListener(this);
    }


    private void getPredefinedPlantsIntoSpinner() {
        PlantsDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotPlants) {
                List<String> plantNames = new ArrayList<>();
                plantNames.add("Custom"); // Add a "Custom" option

                for (DataSnapshot snapshot : snapshotPlants.getChildren()) {
                    Object nameValue = snapshot.child("name").getValue();
                    String plantName = (nameValue != null) ? nameValue.toString() : null;

                    if (plantName != null)
                        plantNames.add(plantName);
                    else
                        Toast.makeText(DataActivity.this, "No plants from DB", Toast.LENGTH_LONG).show();
                }

                ArrayAdapter<String> plantNameAdapter = new ArrayAdapter<>(DataActivity.this, R.layout.simple_spinner_plant_names, plantNames);
                plantNameAdapter.setDropDownViewResource(R.layout.simple_spinner_plant_names);
                spinner_names.setAdapter(plantNameAdapter);
                spinner_names.setOnItemSelectedListener(DataActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



/*
    private void getPredefinedPlantsIntoSpinner(){
        PlantsDB.addListenerForSingleValueEvent(new ValueEventListener() {
*//*
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotSpin) {
                for (DataSnapshot snapshot : snapshotSpin.getChildren()) {
                    if (snapshot.child("name").getValue(String.class).equals(item)) {
                        editTextMyPlantName.setText(item);

                        Integer minTempValue = snapshot.child("minTemp").getValue(Integer.class);
                        Integer maxTempValue = snapshot.child("maxTemp").getValue(Integer.class);
                        Integer minHumidityValue = snapshot.child("minHum").getValue(Integer.class);
                        Integer maxHumidityValue = snapshot.child("maxHum").getValue(Integer.class);
                        Integer minSoilHumidityValue = snapshot.child("minSoilHum").getValue(Integer.class);
                        Integer maxSoilHumidityValue = snapshot.child("maxSoilHum").getValue(Integer.class);
                        Integer minLuminosityValue = snapshot.child("minLight").getValue(Integer.class);
                        Integer maxLuminosityValue = snapshot.child("maxLight").getValue(Integer.class);

                        if (minTempValue != null)
                            editTextMinTemp.setText(minTempValue.toString());
                        if (maxTempValue != null)
                            editTextMaxTemp.setText(maxTempValue.toString());
                        if (minHumidityValue != null)
                            editTextMinHumidity.setText(minHumidityValue.toString());
                        if (maxHumidityValue != null)
                            editTextMaxHumidity.setText(maxHumidityValue.toString());
                        if (minSoilHumidityValue != null)
                            editTextMinMoisture.setText(minSoilHumidityValue.toString());
                        if (maxSoilHumidityValue != null)
                            editTextMaxMoisture.setText(maxSoilHumidityValue.toString());
                        if (minLuminosityValue != null)
                            editTextMinLuminosity.setText(minLuminosityValue.toString());
                        if (maxLuminosityValue != null)
                            editTextMaxLuminosity.setText(maxLuminosityValue.toString());

                        break; // Assuming there's only one match, so we can exit the loop
                    }
                }
            }*//*


*//*            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotPlants) {
                List<String> plantNames = new ArrayList<>();

                for (DataSnapshot snapshot : snapshotPlants.getChildren()) {
                    // String plantName = snapshot.child("name").getValue(String.class);
                    // String plantName = snapshot.child("name").getValue() != null ? snapshot.child("name").getValue().toString() : null;

                    Object nameValue = snapshot.child("name").getValue();
                    String plantName = (nameValue != null) ? nameValue.toString() : null;

                    if(plantName != null)
                        plantNames.add(plantName);
                    else
                        Toast.makeText(DataActivity.this, "No plants from DB", Toast.LENGTH_LONG).show();
                }


                ArrayAdapter<String> plantNameAdapter = new ArrayAdapter<>(DataActivity.this, R.layout.simple_spinner_plant_names, plantNames);
                plantNameAdapter.setDropDownViewResource(R.layout.simple_spinner_plant_names);
                spinner_names.setAdapter(plantNameAdapter);
                spinner_names.setOnItemSelectedListener(DataActivity.this);
            }*//*

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    private void setActualPlantParameters(){
        if(CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant() == null){
            text_view_info.setText("Provide plant parameters");
        }
        else{
            actual_plant_data = String.format("Plant Name:   %s\nAir Temperature:       %s °C <---> %s °C\nAir Humidity:              %s %% <---> %s %%\nSoil Humidity:            %s %% <---> %s %%\nLuminosity Level:      %s %% <---> %s %%",
                    CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getPlantName(),
                    CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMinTemp(),
                    CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMaxTemp(),
                    CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMinHumid(),
                    CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMaxHumid(),
                    CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMinSoilHumid(),
                    CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMaxSoilHumid(),
                    CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMinLuminosity(),
                    CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMaxLuminosity());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if (item.equals("Custom")) {
            // User wants to manually enter data
            // Clear editTexts or perform any desired action
        } else {
            PlantsDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotSpin) {
                    for (DataSnapshot snapshot : snapshotSpin.getChildren()) {
                        if (snapshot.child("name").getValue() != null && snapshot.child("name").getValue().toString().equals(item)) {
                            editTextMyPlantName.setText(item);

                            Integer minTempValue = snapshot.child("minTemperature").getValue(Integer.class);
                            Integer maxTempValue = snapshot.child("maxTemperature").getValue(Integer.class);
                            Integer minHumidityValue = snapshot.child("minHumidity").getValue(Integer.class);
                            Integer maxHumidityValue = snapshot.child("maxHumidity").getValue(Integer.class);
                            Integer minSoilHumidityValue = snapshot.child("minMoisture").getValue(Integer.class);
                            Integer maxSoilHumidityValue = snapshot.child("maxMoisture").getValue(Integer.class);
                            Integer minLuminosityValue = snapshot.child("minLuminosity").getValue(Integer.class);
                            Integer maxLuminosityValue = snapshot.child("maxLuminosity").getValue(Integer.class);

                            if (minTempValue != null)
                                editTextMinTemp.setText(minTempValue.toString());
                            if (maxTempValue != null)
                                editTextMaxTemp.setText(maxTempValue.toString());
                            if (minHumidityValue != null)
                                editTextMinHumidity.setText(minHumidityValue.toString());
                            if (maxHumidityValue != null)
                                editTextMaxHumidity.setText(maxHumidityValue.toString());
                            if (minSoilHumidityValue != null)
                                editTextMinMoisture.setText(minSoilHumidityValue.toString());
                            if (maxSoilHumidityValue != null)
                                editTextMaxMoisture.setText(maxSoilHumidityValue.toString());
                            if (minLuminosityValue != null)
                                editTextMinLuminosity.setText(minLuminosityValue.toString());
                            if (maxLuminosityValue != null)
                                editTextMaxLuminosity.setText(maxLuminosityValue.toString());

                            break; // Assuming there's only one match, so we can exit the loop
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled if needed
                }
            });
        }
    }


/*    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(item.equals("Manual")) {
            // user wants to manually enter data
            // clearEditTexts
        }
        else {
            PlantsDB.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotSpin) {
                    for (DataSnapshot snapshot : snapshotSpin.getChildren()) {
                        if (snapshot.child("name").getValue(String.class).equals(item)) {
                            editTextMyPlantName.setText(item);

                            Integer minTempValue = snapshot.child("minTemp").getValue(Integer.class);
                            Integer maxTempValue = snapshot.child("maxTemp").getValue(Integer.class);
                            Integer minHumidityValue = snapshot.child("minHum").getValue(Integer.class);
                            Integer maxHumidityValue = snapshot.child("maxHum").getValue(Integer.class);
                            Integer minSoilHumidityValue = snapshot.child("minSoilHum").getValue(Integer.class);
                            Integer maxSoilHumidityValue = snapshot.child("maxSoilHum").getValue(Integer.class);
                            Integer minLuminosityValue = snapshot.child("minLight").getValue(Integer.class);
                            Integer maxLuminosityValue = snapshot.child("maxLight").getValue(Integer.class);

                            if (minTempValue != null)
                                editTextMinTemp.setText(minTempValue.toString());
                            if (maxTempValue != null)
                                editTextMaxTemp.setText(maxTempValue.toString());
                            if (minHumidityValue != null)
                                editTextMinHumidity.setText(minHumidityValue.toString());
                            if (maxHumidityValue != null)
                                editTextMaxHumidity.setText(maxHumidityValue.toString());
                            if (minSoilHumidityValue != null)
                                editTextMinMoisture.setText(minSoilHumidityValue.toString());
                            if (maxSoilHumidityValue != null)
                                editTextMaxMoisture.setText(maxSoilHumidityValue.toString());
                            if (minLuminosityValue != null)
                                editTextMinLuminosity.setText(minLuminosityValue.toString());
                            if (maxLuminosityValue != null)
                                editTextMaxLuminosity.setText(maxLuminosityValue.toString());

                            break; // Assuming there's only one match, so we can exit the loop
                        }
                    }
                }

*//*                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotSpin) {
                    for (DataSnapshot snapshot: snapshotSpin.getChildren()) {
                        if(snapshot.child("name").getValue(String.class).equals(item)){
                            editTextMyPlantName.setText(item);
                            editTextMinTemp.setText(snapshot.child("minTemp").getValue(Integer.class).toString());
                            editTextMaxTemp.setText(snapshot.child("maxTemp").getValue(Integer.class).toString());
                            editTextMinHumidity.setText(snapshot.child("minHum").getValue(Integer.class).toString());
                            editTextMaxHumidity.setText(snapshot.child("maxHum").getValue(Integer.class).toString());
                            editTextMinMoisture.setText(snapshot.child("minSoilHum").getValue(Integer.class).toString());
                            editTextMaxMoisture.setText(snapshot.child("maxSoilHum").getValue(Integer.class).toString());
                            editTextMinLuminosity.setText(snapshot.child("minLight").getValue(Integer.class).toString());
                            editTextMaxLuminosity.setText(snapshot.child("maxLight").getValue(Integer.class).toString());
                        }
                    }
                }*//*

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }*/

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}