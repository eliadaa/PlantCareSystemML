package com.example.plantcaresystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlantActivity extends AppCompatActivity { // extends AppCompatActivity extends Fragment

    private LinearLayout layout_soil_moisture, layout_air_humid, layout_temperature, layout_lumin, layout_water_level;

    private TextView tv_soil_moisture, tv_air_humid, tv_temp, tv_lumin, tv_water_level, tv_info;

    private String soil_moist, air_humid, temp, lumin, water_level, info;

    private String soil_moist_time, air_humid_time, temp_time, lumin_time, water_level_time;

    private Handler handler;

    private RequestQueue requestQueue;

    String currentActivityName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_plant);
        requestQueue = Volley.newRequestQueue(this);
        handler = new Handler();
        init();
        initTextViewStrings();
        getSensorDataForTemperature();
        getSensorDataForSoilMoisture();
        getSensorDataForAirHumidity();
        getSensorDataForLuminosity();
        getSensorDataForWaterLevel();

        // refresh the activity every 100 seconds
        // Call the refreshRunnable to start the refreshing process
        handler.postDelayed(refreshRunnable, 60000); // Start the refresh after 60 seconds initially

        setButtonNavigation();
    }


    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {

            currentActivityName = CurrentActivityUtils.getCurrentActivityName(PlantActivity.this);
            if (currentActivityName != null) {
                if (currentActivityName.equals("com.example.plantcaresystem.PlantActivity")) {
                    getSensorDataForTemperature();
                    getSensorDataForSoilMoisture();
                    getSensorDataForAirHumidity();
                    getSensorDataForLuminosity();
                    getSensorDataForWaterLevel();

                    Toast.makeText(PlantActivity.this, "Sensor Data Updated!", Toast.LENGTH_LONG).show();

                    // Schedule the next refresh after 20 seconds
                    handler.postDelayed(refreshRunnable, 60000); // 60000 milliseconds = 60 seconds
                } else  {
                    return;
                }
            }

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(refreshRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(refreshRunnable);
    }

    public void fetchData(){
        String tempAPI = "https://api.thingspeak.com/channels/2175282/feeds.json?api_key=6OY4N35F3U7M4O6L&results=2";
        JsonObjectRequest objectRequest =new JsonObjectRequest(Request.Method.GET, tempAPI, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // tv_temp.setText("20");
                        try {
                            JSONArray feeds = response.getJSONArray("feeds");
                            for(int i=0; i<feeds.length();i++){
                                JSONObject jo = feeds.getJSONObject(i);
                                String l=jo.getString("field1");
                                tv_temp.setText(l);
                                Toast.makeText(PlantActivity.this, temp +"this is temp", Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(PlantActivity.this, temp +"this is temp", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void getSensorDataForTemperature() {
        // URL for the temperature sensor data
        String temperatureSensorURL = "https://api.thingspeak.com/channels/2175282/feeds.json?api_key=6OY4N35F3U7M4O6L&results=2";

        // Create a JsonObjectRequest to make a GET request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, temperatureSensorURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the "feeds" JSON array from the response
                            JSONArray feedsArray = response.getJSONArray("feeds");

                            // Iterate over the feeds array
                            for (int i = 0; i < feedsArray.length(); i++) {
                                JSONObject field = feedsArray.getJSONObject(i);

                                temp = field.getString("field1");
                                // Toast.makeText(PlantActivity.this, "temp:"+temp, Toast.LENGTH_SHORT).show();
                                // i dont get it... it receives the data, but can't display it on the text view field
                                tv_temp.setText(temp + "°C");
/*
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Your ImageView Code & setImageBitmap
                                        tv_temp.setText(temp + "°C");
                                    }
                                }, 20);*/

/*                                PlantActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((TextView)tv_temp).setText(temp);
                                    }
                                });*/

/*                                // Use runOnUiThread to update the TextView on the main thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_temp.setText(temp + "°C");
                                    }
                                });*/


                                /*// Check if there is data in the "field1" channel
                                if (field.getString("field1") != null) {
                                    // Retrieve temperature value and time from the feed
                                    // temp = field.getString("field1");
                                    // tv_temp.setText(temp + "°C");

                                    // it arrives here
                                    // Toast.makeText(PlantActivity.this, temp +"this is temp", Toast.LENGTH_SHORT).show();
                                    //temp_time = field.getString("time: ");

                                    // Update the UI with the temperature value
                                    // tv_temp.setText(temp + "°C");
                                }
                                else {
                                    Toast.makeText(PlantActivity.this, "no temp received", Toast.LENGTH_SHORT).show();
                                }*/
                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing exception
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response here
                    }
                });

        // Add the request to the RequestQueue to send the request to the server
        requestQueue.add(request);
    }


/*


    private void getSensorDataForSoilMoisture(){
        String urlSoilHumid = "https://api.thingspeak.com/channels/2175282/feeds.json?api_key=6OY4N35F3U7M4O6L&results=2";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlSoilHumid, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("feeds");
                            JSONObject field = jsonArray.getJSONObject(jsonArray.length()-1);
                            soil_moist = field.getString("field1");
                            // soil_moist_time = field.getString("created_at").substring(0,10) + " " + field.getString("created_at").substring(11,19);
                            tv_soil_moisture.setText(soil_moist + "%");
                            if(CurrentLoggedUser.getInstance().getCurrentUser().getPlant() != null){
                                if(Integer.parseInt(soil_moist) >= CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getMinSoilMoist()
                                        && Integer.parseInt(soil_moist) <= CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getMinSoilMoist()){
                                    layout_soil_moisture.setBackgroundColor(Color.parseColor("#eff5bf"));
                                    CurrentLoggedUser.getInstance().setCurrentMoist(0);
                                }else{
                                    layout_soil_moisture.setBackgroundColor(Color.parseColor("#aaff0000"));
                                }
                            }
                            if(CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant() != null){
                                if(Integer.parseInt(soil_moist) > CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getMinSoilMoist()){
                                    CurrentLoggedUser.getInstance().setCurrentMoist(1);
                                }
                                if(Integer.parseInt(soil_moist) < CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant().getMinSoilMoist()){
                                    CurrentLoggedUser.getInstance().setCurrentMoist(-1);
                                }
                            }
                            // displayAndFormatInfo();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }
*/



    /// here field4


    private void getSensorDataForSoilMoisture() {
        // URL for the moisture sensor data
        String SoilMoistureURL = "https://api.thingspeak.com/channels/2175282/feeds.json?api_key=6OY4N35F3U7M4O6L&results=2";

        // Create a JsonObjectRequest to make a GET request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, SoilMoistureURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the "feeds" JSON array from the response
                            JSONArray feedsArray = response.getJSONArray("feeds");

                            // Iterate over the feeds array
                            for (int i = 0; i < feedsArray.length(); i++) {
                                JSONObject field = feedsArray.getJSONObject(i);

                                soil_moist = field.getString("field4");
                                // Toast.makeText(PlantActivity.this, "" + soil_moist, Toast.LENGTH_LONG).show();
                                tv_soil_moisture.setText(soil_moist + "%");

                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing exception
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response here
                    }
                });

        // Add the request to the RequestQueue to send the request to the server
        requestQueue.add(request);
    }


/*


    private void getSensorDataForSoilMoisture() {
        // URL for the moisture sensor data
        String SoilMoistureURL = "https://api.thingspeak.com/channels/2175282/feeds.json?api_key=6OY4N35F3U7M4O6L&results=2";

        // Create a JsonObjectRequest to make a GET request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, SoilMoistureURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the "feeds" JSON array from the response
                            JSONArray feedsArray = response.getJSONArray("feeds");

                            // Iterate over the feeds array
                            for (int i = 0; i < feedsArray.length(); i++) {
                                JSONObject field = feedsArray.getJSONObject(i);

                                soil_moist = field.getString("field4");
                                // Toast.makeText(PlantActivity.this, "" + soil_moist, Toast.LENGTH_LONG).show();
                                tv_soil_moisture.setText(soil_moist + "%");

                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing exception
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response here
                    }
                });

        // Add the request to the RequestQueue to send the request to the server
        requestQueue.add(request);
    }
*/


    private void getSensorDataForAirHumidity() {
        // URL for the moisture sensor data
        String AirHumidityURL = "https://api.thingspeak.com/channels/2175282/feeds.json?api_key=6OY4N35F3U7M4O6L&results=2";

        // Create a JsonObjectRequest to make a GET request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, AirHumidityURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the "feeds" JSON array from the response
                            JSONArray feedsArray = response.getJSONArray("feeds");

                            // Iterate over the feeds array
                            for (int i = 0; i < feedsArray.length(); i++) {
                                JSONObject field = feedsArray.getJSONObject(i);

                                air_humid = field.getString("field2");
                                // Toast.makeText(PlantActivity.this, "" + soil_moist, Toast.LENGTH_LONG).show();
                                tv_air_humid.setText(air_humid + "%");

                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing exception
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response here
                    }
                });

        // Add the request to the RequestQueue to send the request to the server
        requestQueue.add(request);
    }


    private void getSensorDataForWaterLevel() {
        // URL for the moisture sensor data
        String SoilMoistureURL = "https://api.thingspeak.com/channels/2175282/feeds.json?api_key=6OY4N35F3U7M4O6L&results=2";

        // Create a JsonObjectRequest to make a GET request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, SoilMoistureURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the "feeds" JSON array from the response
                            JSONArray feedsArray = response.getJSONArray("feeds");

                            // Iterate over the feeds array
                            for (int i = 0; i < feedsArray.length(); i++) {
                                JSONObject field = feedsArray.getJSONObject(i);

                                water_level = field.getString("field5");
                                // Toast.makeText(PlantActivity.this, "" + soil_moist, Toast.LENGTH_LONG).show();
                                tv_water_level.setText(water_level + "%");

                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing exception
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response here
                    }
                });

        // Add the request to the RequestQueue to send the request to the server
        requestQueue.add(request);
    }


/*
    private void getSensorDataForTemperature(){
        String TemperatureSensorURL = "https://api.thingspeak.com/channels/2175282/feeds.json?api_key=6OY4N35F3U7M4O6L&results=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, TemperatureSensorURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray feedsArray = response.getJSONArray("feeds"); // get the feeds
                    for (int i = 0; i < feedsArray.length(); i++) {
                        JSONObject field = feedsArray.getJSONObject(i);
                        if(field.getString("field1") != null) { // is there something in the channel field1?
                            temp = field.getString("field1");
                            temp_time = field.getString("time: ");
                            tv_temperature.setText(temp + "°C");
                        }

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });



    }*/



    private void setButtonNavigation(){

        // init and assign var
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // set data screen selected
        bottomNavigationView.setSelectedItemId(R.id.plant_screen);

        // perform listener on selected
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.plant_screen) {
                    return true;
                } else if (itemId == R.id.data_screen) {
                    startActivity(new Intent(getApplicationContext(), DataActivity.class));
                    overridePendingTransition(0, 0);
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




    private void getSensorDataForLuminosity() {
        // URL for the moisture sensor data
        String AirHumidityURL = "https://api.thingspeak.com/channels/2175282/feeds.json?api_key=6OY4N35F3U7M4O6L&results=2";

        // Create a JsonObjectRequest to make a GET request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, AirHumidityURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the "feeds" JSON array from the response
                            JSONArray feedsArray = response.getJSONArray("feeds");

                            // Iterate over the feeds array
                            for (int i = 0; i < feedsArray.length(); i++) {
                                JSONObject field = feedsArray.getJSONObject(i);

                                lumin = field.getString("field3");
                                // Toast.makeText(PlantActivity.this, "" + soil_moist, Toast.LENGTH_LONG).show();
                                tv_lumin.setText(lumin + "%");

                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing exception
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response here
                    }
                });

        // Add the request to the RequestQueue to send the request to the server
        requestQueue.add(request);
    }


    private void setColors(){
        // set default colors for text views if the user doesn't have a plant
    }

    private void init(){
        tv_soil_moisture = findViewById(R.id.tv_soil_moisture);
        tv_air_humid = findViewById(R.id.tv_air_humid);
        tv_temp = findViewById(R.id.tv_temperature);
        tv_water_level = findViewById(R.id.tv_water_level);
        tv_lumin = findViewById(R.id.tv_lumin);
        tv_info = findViewById(R.id.tv_info);
        layout_lumin = findViewById(R.id.layout_lumin);
        layout_air_humid = findViewById(R.id.layout_air_humid);
        layout_temperature = findViewById(R.id.layout_temperature);
        layout_water_level = findViewById(R.id.layout_water_level);
        layout_soil_moisture = findViewById(R.id.layout_soil_moisture);
    }

    private void initTextViewStrings(){
        soil_moist = "-";
        air_humid = "-";
        temp = "-";
        lumin = "-";
        water_level = "-";
        air_humid_time = "-";
        soil_moist_time = "-";
        lumin_time = "-";
        water_level_time = "-";
        temp_time = "-";
        info = "Information about the plant will be displayed here...";
    }

}
