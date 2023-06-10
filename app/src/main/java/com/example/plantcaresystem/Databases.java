package com.example.plantcaresystem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// we need the instance of the databases,
// using 2: one for the registered users and another one for the predefined plants created by me, with the required parameters specific for each plant
public class Databases {
    public static final DatabaseReference usersDB = FirebaseDatabase.getInstance().getReference("Users");
    public static final DatabaseReference PlantsDB = FirebaseDatabase.getInstance().getReference("Plants");
}
