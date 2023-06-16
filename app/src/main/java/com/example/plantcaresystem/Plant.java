package com.example.plantcaresystem;

public class Plant {
    private String plantName;
    private float minHumid, maxHumid;  // humidity dht11
    private float minTemp, maxTemp;    // temperature dht11
    private float minLuminosity, maxLuminosity;  // luminosity temt6000
    private float minSoilMoist, maxSoilMoist;    // soil moisture

    // Default constructor
    public Plant() {
        // Initialize default values or leave empty
    }

//    private int minWaterLevel, maxWaterLevel;  // water level sensor

/*    public int getMinWaterLevel() {
        return minWaterLevel;
    }

    public void setMinWaterLevel(int minWaterLevel) {
        this.minWaterLevel = minWaterLevel;
    }*/

/*
    public int getMaxWaterLevel() {
        return maxWaterLevel;
    }

    public void setMaxWaterLevel(int maxWaterLevel) {
        this.maxWaterLevel = maxWaterLevel;
    }
*/


    // also add the water level stuff
    public Plant(String plantName, float minHumid, float maxHumid, float minTemp, float maxTemp, float minLuminosity, float maxLuminosity, float minSoilMoist, float maxSoilMoist) {
        this.plantName = plantName;
        this.minHumid = minHumid;
        this.maxHumid = maxHumid;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minLuminosity = minLuminosity;
        this.maxLuminosity = maxLuminosity;
        this.minSoilMoist = minSoilMoist;
        this.maxSoilMoist = maxSoilMoist;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public float getMinHumid() {
        return minHumid;
    }

    public void setMinHumid(int minHumid) {
        this.minHumid = minHumid;
    }

    public float getMaxHumid() {
        return maxHumid;
    }

    public void setMaxHumid(int maxHumid) {
        this.maxHumid = maxHumid;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public float getMinLuminosity() {
        return minLuminosity;
    }

    public void setMinLuminosity(int minLuminosity) {
        this.minLuminosity = minLuminosity;
    }

    public float getMaxLuminosity() {
        return maxLuminosity;
    }

    public void setMaxLuminosity(int maxLuminosity) {
        this.maxLuminosity = maxLuminosity;
    }

    public float getMinSoilMoist() {
        return minSoilMoist;
    }

    public void setMinSoilMoist(int minSoilMoist) {
        this.minSoilMoist = minSoilMoist;
    }

    public float getMaxSoilMoist() {
        return maxSoilMoist;
    }

    public void setMaxSoilMoist(int maxSoilMoist) {
        this.maxSoilMoist = maxSoilMoist;
    }
}


