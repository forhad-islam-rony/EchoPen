//package com.example.echopen;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.echopen.Model.BlogItemModel;
//import com.example.echopen.Model.UserData;
//import com.example.echopen.SingleTon.FirebaseDatabaseSingleton;
//import com.example.echopen.databinding.ActivityAddArticleBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class AddArticleActivity extends AppCompatActivity {
//    private ActivityAddArticleBinding binding;
//    private final FirebaseAuth auth = FirebaseAuth.getInstance();
//    private final DatabaseReference databaseReference = FirebaseDatabaseSingleton.getInstance().getDatabaseReference();
//    private final DatabaseReference userReference = FirebaseDatabaseSingleton.getInstance().getUserReference();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityAddArticleBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        binding.imageButton.setOnClickListener(v -> finish());
//
//        binding.addBlogButton.setOnClickListener(v -> {
//            String title = binding.blogTitle.getEditText().getText().toString().trim();
//            String description = binding.blogDescription.getEditText().getText().toString().trim();
//
//            // Check if the title or description fields are empty
//            if (title.isEmpty() || description.isEmpty()) {
//                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            FirebaseUser user = auth.getCurrentUser();
//            if (user != null) {
//                String userId = user.getUid();
//
//                userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot snapshot) {
//                        Log.d("AddArticleActivity", "Snapshot: " + snapshot.getValue());
//
//                        UserData userData = snapshot.getValue(UserData.class);
//                        if (userData != null) {
//                            String userNameFromDB = userData.getName();
//                            String userImageUrlFromDB = userData.getProfileImage();
//
//                            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//
//                            BlogItemModel blogItem = new BlogItemModel(
//                                    title,
//                                    userNameFromDB,
//                                    currentDate,
//                                    description,
//                                    userId,
//                                    0,
//                                    userImageUrlFromDB
//                            );
//
//                            String key = databaseReference.push().getKey();
//                            if (key != null) {
//                                blogItem.setPostId(key);
//                                DatabaseReference blogReference = databaseReference.child(key);
//                                blogReference.setValue(blogItem).addOnCompleteListener(task -> {
//                                    if (task.isSuccessful()) {
//                                        startActivity(new Intent(AddArticleActivity.this, MainActivity.class));
//                                        finish();
//                                    } else {
//                                        Toast.makeText(AddArticleActivity.this, "Failed to add blog", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            } else {
//                                Toast.makeText(AddArticleActivity.this, "Failed to generate unique key", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(AddArticleActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        Toast.makeText(AddArticleActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}




package com.example.echopen;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.echopen.Model.BlogItemModel;
import com.example.echopen.Model.UserData;
import com.example.echopen.SingleTon.FirebaseDatabaseSingleton;
import com.example.echopen.databinding.ActivityAddArticleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddArticleActivity extends AppCompatActivity {
    private ActivityAddArticleBinding binding;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference databaseReference = FirebaseDatabaseSingleton.getInstance().getDatabaseReference();
    private final DatabaseReference userReference = FirebaseDatabaseSingleton.getInstance().getUserReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageButton.setOnClickListener(v -> finish());

        binding.addBlogButton.setOnClickListener(v -> {
            String title = binding.blogTitle.getEditText().getText().toString().trim();
            String description = binding.blogDescription.getEditText().getText().toString().trim();

            // Check if the title or description fields are empty
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                String userId = user.getUid();

                userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d("AddArticleActivity", "Snapshot: " + snapshot.getValue());

                        UserData userData = snapshot.getValue(UserData.class);
                        if (userData != null) {
                            String userNameFromDB = userData.getName();
                            String userImageUrlFromDB = userData.getProfileImage();

                            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                            BlogItemModel blogItem = new BlogItemModel(
                                    title,
                                    userNameFromDB,
                                    currentDate,
                                    description,
                                    userId,
                                    0,
                                    userImageUrlFromDB
                            );

                            String key = databaseReference.push().getKey();
                            if (key != null) {
                                blogItem.setPostId(key); // Set postId here
                                DatabaseReference blogReference = databaseReference.child(key);
                                blogReference.setValue(blogItem).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(AddArticleActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(AddArticleActivity.this, "Failed to add blog", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(AddArticleActivity.this, "Failed to generate unique key", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddArticleActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(AddArticleActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

