package com.example.echopen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.echopen.databinding.ActivityProfileBinding;
import com.example.echopen.register.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.AddNewBlogButton.setOnClickListener(v -> startActivity(new Intent(this, SavedArticlesActivity.class)));
        binding.articleButton.setOnClickListener(v -> startActivity(new Intent(this, ArticleActivity.class)));

        binding.LogOutButton.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        });

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://echopen-1e18e-default-rtdb.firebaseio.com/")
                .getReference("users");

        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId != null) {
            loadUserProfileData(userId);
        }
    }

    private void loadUserProfileData(String userId) {
        DatabaseReference userReference = databaseReference.child(userId);

        userReference.child("profileImage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String profileImageUrl = snapshot.getValue(String.class);
                if (profileImageUrl != null) {
                    Glide.with(ProfileActivity.this)
                            .load(profileImageUrl)
                            .into(binding.userProfile);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load user image", Toast.LENGTH_SHORT).show();
            }
        });

        userReference.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String userName = snapshot.getValue(String.class);
                if (userName != null) {
                    binding.userName.setText(userName);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error if needed
            }
        });
    }
}
