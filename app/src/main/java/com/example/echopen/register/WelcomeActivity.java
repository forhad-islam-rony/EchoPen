package com.example.echopen.register;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.echopen.MainActivity;
import com.example.echopen.R;
import com.example.echopen.SignInAndRegistrationActivity;
import com.example.echopen.databinding.ActivityWelcomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {
    ActivityWelcomeBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, SignInAndRegistrationActivity.class);
            intent.putExtra("action", "login");
            startActivity(intent);
            finish();
        });

        binding.RegisterButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, SignInAndRegistrationActivity.class);
            intent.putExtra("action", "register");
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    }
}
