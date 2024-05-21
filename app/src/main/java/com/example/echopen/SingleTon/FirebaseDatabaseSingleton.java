package com.example.echopen.SingleTon;

// FirebaseDatabaseSingleton.java

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseSingleton {

    private static FirebaseDatabaseSingleton instance;
    private final DatabaseReference databaseReference;
    private final DatabaseReference userReference;

    // Private constructor to prevent instantiation
    private FirebaseDatabaseSingleton() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://echopen-1e18e-default-rtdb.firebaseio.com/");
        this.databaseReference = database.getReference("blogs");
        this.userReference = database.getReference("users");
    }

    // Method to provide the singleton instance
    public static synchronized FirebaseDatabaseSingleton getInstance() {
        if (instance == null) {
            instance = new FirebaseDatabaseSingleton();
        }
        return instance;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public DatabaseReference getUserReference() {
        return userReference;
    }
}
