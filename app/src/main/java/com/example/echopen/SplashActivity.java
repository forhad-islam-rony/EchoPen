package com.example.echopen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.echopen.register.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Check if the user is signed in
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                // User is signed in, go to MainActivity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // User is not signed in, go to WelcomeActivity
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            }
            finish();
        }, 3000); // 3 seconds delay
    }
}
