package com.example.plantcaresystem;

import static com.example.plantcaresystem.Databases.PlantsDB;
import static com.example.plantcaresystem.Databases.usersDB;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class DataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Spinner spinner_names;
    private String myPlantName;
    private EditText editTextMyPlantName, editTextMinMoisture, editTextMaxMoisture, editTextMinTemp, editTextMaxTemp, editTextMinHumidity, editTextMaxHumidity, editTextMinLuminosity, editTextMaxLuminosity;
    private float maxMoist, minMoist, maxTemp, minTemp, maxHumi, minHumi, maxLight, minLight;
    private FirebaseAuth authProfile;
    BottomNavigationView bottomNavigationView;
    TextView text_view_info;
    String actual_plant_data;

    private Button save_button;

    // with this class the user can set the data and parameters wanted for his specific plant
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_data);

        authProfile = FirebaseAuth.getInstance();

        setButtonNavigation();            // set data screen selected
        init();                           // init and assign var
        getPredefinedPlantsIntoSpinner(); // populate spinner menu with values from the Plants database
        setActualPlantParameters();       // setting plant parameters, upper and lower threshold for sensors, plant name..
        SaveButtonListener();             // on click listener on the save button
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


    private void init() {
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
        save_button = findViewById(R.id.button_save_data);
        // spinner_names.setOnItemSelectedListener(this);
    }

    private void SaveButtonListener(){
        save_button.setOnClickListener(v -> SetAndUpdatePlantParameters()); // Update the plant parameters
    }

    // Method to update the plant parameters
    private void SetAndUpdatePlantParameters(){
        FirebaseUser currentUser = authProfile.getCurrentUser();

        // display and focus on incomplete fields?

        // check if all fields filled
        if (isEmptyEditText(editTextMyPlantName) ||
                isEmptyEditText(editTextMaxMoisture) ||
                isEmptyEditText(editTextMaxTemp) ||
                isEmptyEditText(editTextMaxLuminosity) ||
                isEmptyEditText(editTextMinHumidity) ||
                isEmptyEditText(editTextMaxHumidity) ||
                isEmptyEditText(editTextMinLuminosity) ||
                isEmptyEditText(editTextMinMoisture) ||
                isEmptyEditText(editTextMinTemp)) {
            Toast.makeText(this, "Make sure you fill all fields!", Toast.LENGTH_LONG).show();
            return;
        }

        // check if the values entered for sensor data are numbers
        if (!isNumeric(editTextMaxMoisture) || !isNumeric(editTextMinMoisture) ||
            !isNumeric(editTextMaxTemp) || !isNumeric(editTextMinTemp) ||
            !isNumeric(editTextMaxLuminosity) || !isNumeric(editTextMinLuminosity) ||
            !isNumeric(editTextMaxHumidity) || !isNumeric(editTextMinHumidity)){
            Toast.makeText(this, "The sensor threshold value introduced must be numeric!", Toast.LENGTH_LONG).show();
            return;
        }

        // check if min sensor data set < max sensor data set
        if(parseFloatEditText(editTextMaxLuminosity) <= parseFloatEditText(editTextMinLuminosity) ||
            parseFloatEditText(editTextMaxHumidity) <= parseFloatEditText(editTextMinLuminosity) ||
            parseFloatEditText(editTextMaxLuminosity) <= parseFloatEditText(editTextMinLuminosity) ||
            parseFloatEditText(editTextMaxMoisture) <= parseFloatEditText(editTextMinMoisture)) {
            Toast.makeText(this, "min should be smaller (<) than the max for each sensor setting", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve values from editTexts
        myPlantName = editTextMyPlantName.getText().toString();
        maxMoist = Float.parseFloat(editTextMaxMoisture.getText().toString());
        minMoist = Float.parseFloat(editTextMinMoisture.getText().toString());
        maxTemp = Float.parseFloat(editTextMaxTemp.getText().toString());
        minTemp = Float.parseFloat(editTextMinTemp.getText().toString());
        maxHumi = Float.parseFloat(editTextMaxHumidity.getText().toString());
        minHumi = Float.parseFloat(editTextMinHumidity.getText().toString());
        maxLight = Float.parseFloat(editTextMaxLuminosity.getText().toString());
        minLight = Float.parseFloat(editTextMinLuminosity.getText().toString());

        // Create a new Plant object
        // CurrentLoggedUser.getInstance().setCurrentUserProfile(currentUser);

//            public Plant(String plantName, float minHumid, float maxHumid, float minTemp, float maxTemp, float minLuminosity, float maxLuminosity, float minSoilMoist, float maxSoilMoist)
        Plant plant = new Plant(myPlantName, minHumi, maxHumi, minTemp, maxTemp, minLight,  maxLight, minMoist, maxMoist);
        usersDB.child(currentUser.getUid()).child("plant").setValue(plant);
        CurrentLoggedUser.getInstance().getCurrentUserProfile().setPlant(plant);
        removeDataFromEditTexts();
        setActualPlantParameters();
        Toast.makeText(this, "Info saved", Toast.LENGTH_LONG).show();

/*
        sendDataToThingSpeak("https://api.thingspeak.com/update?api_key=VVSDJEX2MALIAU1D&field6=10");
        Toast.makeText(this, "sent info ", Toast.LENGTH_LONG).show();
*/

//        sendDataToThingSpeak("VVSDJEX2MALIAU1D", Float.toString(CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMaxLuminosity()));
//        Toast.makeText(this, "sent info ", Toast.LENGTH_LONG).show();

/*        if(CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMaxLuminosity() > 0){
            // got the left limit, send this range to thingspeak -> ESP8266 to control the pump
            sendDataToThingSpeak("VVSDJEX2MALIAU1D", Float.toString(CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMaxLuminosity()));
            Toast.makeText(this, "sent info ", Toast.LENGTH_LONG).show();
        }*/

//        if(CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant() != null){
//            if(parseFloatEditText(editTextMinMoisture) > 0){
//                // got the left limit, send this range to thingspeak -> ESP8266 to control the pump
//                sendDataToThingSpeak("VVSDJEX2MALIAU1D", Float.toString(parseFloatEditText(editTextMinMoisture)));
//                Toast.makeText(this, "sent info ", Toast.LENGTH_LONG).show();
//            }
//        }

    }

    private boolean isEmptyEditText(EditText editText) {
        return editText.getText() == null || editText.getText().toString().isEmpty();
    }

    private boolean isNumeric(EditText editText){
        return CurrentLoggedUser.isFloat(editText.getText().toString());
    }

    private float parseFloatEditText(EditText editText){
        return Float.parseFloat(editText.getText().toString());
    }

//    private static DataActivity instance;
//    public DataActivity(){
//        instance = this;
//    }

/*
    private void sendDataToThingSpeak(String apiKey, String value){

        // Create an instance of the HttpClient class
        HttpClient httpClient = HttpClientBuilder.create().build();

        // Create an instance of the HttpPost class
        HttpPost request = new HttpPost("https://api.thingspeak.com/update.json");

        try {
            String jsonData = "{\"api_key\":\"" + apiKey + "\",\"field5\":" + value + "}";

            // Set the JSON payload as the request entity
            StringEntity params = new StringEntity(jsonData, "UTF-8");
            params.setContentType("application/json");
            request.setEntity(params);

            // Send the HTTP POST request to ThingSpeak
            HttpResponse response = httpClient.execute(request);

            // Process the response if needed
            System.out.println("Response status: " + response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/



/*    private void sendDataToThingSpeak(String apiKey, String value){
        try {
            String urlString = "https://api.thingspeak.com/update?api_key=" + apiKey + "&field6=" + value;

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Data sent successfully
                // You can handle the response if needed
            } else {
                // Error handling
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

/*    private void sendDataToThingSpeak(String apiKey, String value){
            try {
                // Create a URL object with the desired URL
                URL url = new URL("http://www.example.com");

                // Open a connection to the URL
                URLConnection connection = url.openConnection();

                // Read the content from the URL
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

/*    public static void sendDataToThingSpeak(String url) {
        try {
            // Create a URL object with the desired URL
            URL website = new URL(url);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) website.openConnection();

            // Set the request method (GET by default)
            connection.setRequestMethod("GET");

            // Connect to the URL
            connection.connect();

            // Get the response code (optional)
            int responseCode = connection.getResponseCode();

            // Disconnect/close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*//*    public static void sendDataToThingSpeak(String url) {
        try {
            // Create a URL object with the desired URL
            URL website = new URL(url);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) website.openConnection();

            // Set the request method (GET by default)
            connection.setRequestMethod("GET");

            // Connect to the URL
            connection.connect();

            // Get the response code (optional)
            int responseCode = connection.getResponseCode();

            // Disconnect/close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


/*    private void sendDataToThingSpeak(String apiKey, String value){
        try {
            String urlString = "https://api.thingspeak.com/update?api_key=" + apiKey + "&field6=" + value;
//            https://api.thingspeak.com/update?api_key=VVSDJEX2MALIAU1D&field1=0

            Toast.makeText(this, urlString, Toast.LENGTH_SHORT).show();

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Data sent successfully
                // You can handle the response if needed
            } else {
                // Error handling
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


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


    private void setActualPlantParameters(){
        if(CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant() == null){
            text_view_info.setText("Provide plant parameters.\nEither select a plant type from the list, or set it to custom settings...");
            Toast.makeText(this, "Please set up your plant information!", Toast.LENGTH_LONG).show();
        }
        else{
            actual_plant_data = String.format("Plant Care Settings for plant:   <b>%s</b><br>" +
                                              "Temp:        <b>%s°C - %s°C</b>     |  Humid:   <b>%s%% - %s%%</b><br>" +
                                              "Soil Moist:  <b>%s%% - %s%%</b>      |  Light:   <b>%s%% - %s%%</b>",
                    CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getPlantName(),
                    CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getMinTemp(),
                    CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getMaxTemp(),
                    CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getMinHumid(),
                    CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getMaxHumid(),
                    CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getMinSoilMoist(),
                    CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getMaxSoilMoist(),
                    CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getMinLuminosity(),
                    CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getMaxLuminosity());
            // text_view_info.setText(actual_plant_data);
            text_view_info.setText(Html.fromHtml(actual_plant_data));

/*            Float minRangeMoist = CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMinSoilMoist();
            Toast.makeText(this, ""+ minRangeMoist, Toast.LENGTH_SHORT).show();*/

/*
            sendDataToThingSpeak("VVSDJEX2MALIAU1D", String.valueOf(minRangeMoist));
            Toast.makeText(this, "sent", Toast.LENGTH_SHORT).show();

            */






//
/*            if(CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMinSoilMoist() > 0){
                // got the left limit, send this range to thingspeak -> ESP8266 to control the pump
                sendDataToThingSpeak("VVSDJEX2MALIAU1D", Float.toString(CurrentLoggedUser.getInstance().getCurrentUser().getPlant().getMinSoilMoist()));
                Toast.makeText(this, "sent info ", Toast.LENGTH_LONG).show();
            }*/
        }
    }

/*
    public static void sendDataToThingSpeak(String url, String value) {
        try {
            URL urlObject = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "field1=" + value; // Customize this based on your ThingSpeak channel

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(postData.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
//                System.out.println("Data sent successfully to ThingSpeak.");
                Toast.makeText(instance, "Sent data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(instance, "Error sending data", Toast.LENGTH_LONG).show();
//                System.out.println("Error sending data to ThingSpeak. Response Code: " + responseCode);
            }

            connection.disconnect();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if (item.equals("Custom")) {
            // User wants to manually enter data
            // Clear editTexts or perform any desired action
            removeDataFromEditTexts();
        } else {
            // User selected an item from the spinner
            // Fetch corresponding data from the Firebase Database
            PlantsDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotSpin) {
                    // Iterate over the data snapshot to find a match for the selected item
                    for (DataSnapshot snapshot : snapshotSpin.getChildren()) {
                        if (snapshot.child("name").getValue() != null && snapshot.child("name").getValue().toString().equals(item)) {
                            // Update the editTexts with the fetched data
                            editTextMyPlantName.setText(item);

                            Integer minTempValue = snapshot.child("minTemperature").getValue(Integer.class);
                            Integer maxTempValue = snapshot.child("maxTemperature").getValue(Integer.class);
                            Integer minHumidityValue = snapshot.child("minHumidity").getValue(Integer.class);
                            Integer maxHumidityValue = snapshot.child("maxHumidity").getValue(Integer.class);
                            Integer minSoilHumidityValue = snapshot.child("minMoisture").getValue(Integer.class);
                            Integer maxSoilHumidityValue = snapshot.child("maxMoisture").getValue(Integer.class);
                            Integer minLuminosityValue = snapshot.child("minLuminosity").getValue(Integer.class);
                            Integer maxLuminosityValue = snapshot.child("maxLuminosity").getValue(Integer.class);

                            // Set the fetched values to corresponding editTexts
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

                            // Exit the loop assuming there's only one match
                            break;
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

    // Method to remove data from editTexts
    private void removeDataFromEditTexts(){
        editTextMyPlantName.getText().clear();
        editTextMinMoisture.getText().clear();
        editTextMaxMoisture.getText().clear();
        editTextMinTemp.getText().clear();
        editTextMaxTemp.getText().clear();
        editTextMinHumidity.getText().clear();
        editTextMaxHumidity.getText().clear();
        editTextMinLuminosity.getText().clear();
        editTextMaxLuminosity.getText().clear();
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