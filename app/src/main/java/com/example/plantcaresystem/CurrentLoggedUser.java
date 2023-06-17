package com.example.plantcaresystem;

public class CurrentLoggedUser {

    private static CurrentLoggedUser instance; // holds the reference, only one instance can be created
    // meaning, only one user can be logged in at a time
    private UserProfile currentUser; // this is the user

    // his plant has these parameters:
    private WarningLevel currentMoistWarning, currentTempWarning, currentHumWarning, currentLumWarning, currentWaterWarning;

    public UserProfile getCurrentUserProfile(){
        return currentUser;
    }

    public static CurrentLoggedUser getInstance(){
        if(instance == null)
            instance = new CurrentLoggedUser();
        return instance;
    }

    // check if the parameters received are integers
    public static boolean isFloat(String value){
        try{
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException exception){
            return false;
        }
    }


    public static void setInstance(CurrentLoggedUser instance) {
        CurrentLoggedUser.instance = instance;
    }

    /*public UserProfile getCurrentUserProfile() {
        return currentUser;
    }
*/
    public void setCurrentUserProfile(UserProfile currentUser) {
        this.currentUser = currentUser;
    }

    public WarningLevel getCurrentMoistWarning() {
        return currentMoistWarning;
    }

    public void setCurrentMoistWarning(WarningLevel currentMoistWarning) {
        // this.currentMoist = currentMoist;
        // or use something like:
        // using this, you first retrieve the instance of the current logged user then the value is assigned to it
        // so it operates on the singleton instance directly
        CurrentLoggedUser.getInstance().currentMoistWarning = currentMoistWarning;
    }

    public WarningLevel getCurrentTempWarning() {
        return currentTempWarning;
    }

    public void setCurrentTempWarning(WarningLevel currentTempWarning) {
        //this.currentTemp = currentTemp;
        CurrentLoggedUser.getInstance().currentTempWarning = currentTempWarning;
    }

    public WarningLevel getCurrentWaterWarning() {
        return currentWaterWarning;
    }


    public void setCurrentWaterWarning(WarningLevel currentWaterWarning) {
        CurrentLoggedUser.getInstance().currentWaterWarning = currentWaterWarning;
    }


    public WarningLevel getCurrentHumWarning() {
        return currentHumWarning;
    }

    public void setCurrentHumWarning(WarningLevel currentHumWarning) {
        // this.currentHum = currentHum;
        CurrentLoggedUser.getInstance().currentHumWarning = currentHumWarning;
    }

    public WarningLevel getCurrentLumWarning() {
        return currentLumWarning;
    }

    public void setCurrentLumWarning(WarningLevel currentLumWarning) {
        //this.currentLum = currentLum;
        CurrentLoggedUser.getInstance().currentLumWarning = currentLumWarning;
    }
}
