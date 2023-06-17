package com.example.plantcaresystem;

public class CurrentLoggedUser {

    private static CurrentLoggedUser instance; // holds the reference, only one instance can be created
    // meaning, only one user can be logged in at a time
    private UserProfile currentUser; // this is the user

    // his plant has these parameters:
    private int currentMoistWarning = 0, currentTempWarning = 0, currentHumWarning = 0, currentLumWarning = 0;

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

    public int getCurrentMoistWarning() {
        return currentMoistWarning;
    }

    public void setCurrentMoistWarning(int currentMoistWarning) {
        // this.currentMoist = currentMoist;
        // or use something like:
        // using this, you first retrieve the instance of the current logged user then the value is assigned to it
        // so it operates on the singleton instance directly
        CurrentLoggedUser.getInstance().currentMoistWarning = currentMoistWarning;
    }

    public int getCurrentTempWarning() {
        return currentTempWarning;
    }

    public void setCurrentTempWarning(int currentTempWarning) {
        //this.currentTemp = currentTemp;
        CurrentLoggedUser.getInstance().currentTempWarning = currentTempWarning;
    }

    public int getCurrentHumWarning() {
        return currentHumWarning;
    }

    public void setCurrentHumWarning(int currentHumWarning) {
        // this.currentHum = currentHum;
        CurrentLoggedUser.getInstance().currentHumWarning = currentHumWarning;
    }

    public int getCurrentLumWarning() {
        return currentLumWarning;
    }

    public void setCurrentLumWarning(int currentLumWarning) {
        //this.currentLum = currentLum;
        CurrentLoggedUser.getInstance().currentLumWarning = currentLumWarning;
    }
}
