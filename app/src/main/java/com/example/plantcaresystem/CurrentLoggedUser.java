package com.example.plantcaresystem;

public class CurrentLoggedUser {

    private static CurrentLoggedUser instance; // holds the reference, only one instance can be created
    // meaning, only one user can be logged in at a time
    private UserProfile currentUser; // this is the user

    // his plant has these parameters:
    private int currentMoist = 0, currentTemp = 0, currentHum = 0, currentLum = 0;

    public UserProfile getCurrentUserProfile(){
        return currentUser;
    }

    public static CurrentLoggedUser getInstance(){
        if(instance == null)
            instance = new CurrentLoggedUser();
        return instance;
    }

    // check if the parameters received are integers
    public static boolean isInt(String value){
        try{
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException exception){
            return false;
        }
    }


    public static void setInstance(CurrentLoggedUser instance) {
        CurrentLoggedUser.instance = instance;
    }

    public UserProfile getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUserProfile(UserProfile currentUser) {
        this.currentUser = currentUser;
    }

    public int getCurrentMoist() {
        return currentMoist;
    }

    public void setCurrentMoist(int currentMoist) {
        // this.currentMoist = currentMoist;
        // or use something like:
        // using this, you first retrieve the instance of the current logged user then the value is assigned to it
        // so it operates on the singleton instance directly
        CurrentLoggedUser.getInstance().currentMoist=currentMoist;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        //this.currentTemp = currentTemp;
        CurrentLoggedUser.getInstance().currentTemp = currentTemp;
    }

    public int getCurrentHum() {
        return currentHum;
    }

    public void setCurrentHum(int currentHum) {
        // this.currentHum = currentHum;
        CurrentLoggedUser.getInstance().currentHum = currentHum;
    }

    public int getCurrentLum() {
        return currentLum;
    }

    public void setCurrentLum(int currentLum) {
        //this.currentLum = currentLum;
        CurrentLoggedUser.getInstance().currentLum = currentLum;
    }
}
