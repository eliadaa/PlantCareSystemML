package com.example.plantcaresystem;

public class UserProfile {
    private String FullName;
    private String email;
    private String uid;
    private Plant plant;

    public UserProfile(){

    }

    public UserProfile(String fullName, String email, String uid, Plant plant) {
        FullName = fullName;
        this.email = email;
        this.uid = uid;
        this.plant = plant;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
