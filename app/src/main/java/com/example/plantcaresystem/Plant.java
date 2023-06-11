package com.example.plantcaresystem;

public class Plant {
    private String plantName;
    private int minHumid, maxHumid;  // humidity dht11
    private int minTemp, maxTemp;    // temperature dht11
    private int minLuminosity, maxLuminosity;  // luminosity temt6000
    private int minSoilHumid, maxSoilHumid;    // soil moisture

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

    public Plant(){

    }


    // also add the water level stuff
    public Plant(String plantName, int minHumid, int maxHumid, int minTemp, int maxTemp, int minLuminosity, int maxLuminosity, int minSoilHumid, int maxSoilHumid) {
        this.plantName = plantName;
        this.minHumid = minHumid;
        this.maxHumid = maxHumid;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minLuminosity = minLuminosity;
        this.maxLuminosity = maxLuminosity;
        this.minSoilHumid = minSoilHumid;
        this.maxSoilHumid = maxSoilHumid;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public int getMinHumid() {
        return minHumid;
    }

    public void setMinHumid(int minHumid) {
        this.minHumid = minHumid;
    }

    public int getMaxHumid() {
        return maxHumid;
    }

    public void setMaxHumid(int maxHumid) {
        this.maxHumid = maxHumid;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinLuminosity() {
        return minLuminosity;
    }

    public void setMinLuminosity(int minLuminosity) {
        this.minLuminosity = minLuminosity;
    }

    public int getMaxLuminosity() {
        return maxLuminosity;
    }

    public void setMaxLuminosity(int maxLuminosity) {
        this.maxLuminosity = maxLuminosity;
    }

    public int getMinSoilHumid() {
        return minSoilHumid;
    }

    public void setMinSoilHumid(int minSoilHumid) {
        this.minSoilHumid = minSoilHumid;
    }

    public int getMaxSoilHumid() {
        return maxSoilHumid;
    }

    public void setMaxSoilHumid(int maxSoilHumid) {
        this.maxSoilHumid = maxSoilHumid;
    }
}


