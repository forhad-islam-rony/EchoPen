package com.example.echopen;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.echopen.Model.UserData;
import com.example.echopen.SingleTon.FirebaseDatabaseSingleton;
import com.example.echopen.databinding.ActivitySignInAndRegistrationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignInAndRegistrationActivity extends AppCompatActivity {

    private ActivitySignInAndRegistrationBinding binding;
    public FirebaseAuth auth;
    private FirebaseStorage storage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private final DatabaseReference userReference = FirebaseDatabaseSingleton.getInstance().getUserReference();

    @RequiresApi(Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInAndRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        String action = getIntent().getStringExtra("action");
        if ("login".equals(action)) {
            binding.loginEmail.setVisibility(View.VISIBLE);
            binding.loginPassword.setVisibility(View.VISIBLE);
            binding.loginButton.setVisibility(View.VISIBLE);

            binding.RegisterButton.setEnabled(false);
            binding.RegisterButton.setAlpha(0.5f);
            binding.registerNewHere.setEnabled(false);
            binding.registerNewHere.setAlpha(0.5f);

            binding.registerEmail.setVisibility(View.GONE);
            binding.registerName.setVisibility(View.GONE);
            binding.registerPassword.setVisibility(View.GONE);
            binding.cardView.setVisibility(View.GONE);

            binding.loginButton.setOnClickListener(v -> {
                String loginEmail = binding.loginEmail.getText().toString();
                String loginPassword = binding.loginPassword.getText().toString();

                if (loginEmail.isEmpty() || loginPassword.isEmpty()) {
                    Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                        } else {
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else if ("register".equals(action)) {
            binding.loginButton.setEnabled(false);
            binding.loginButton.setAlpha(0.5f);

            binding.RegisterButton.setOnClickListener(v -> {
                Log.d("register", "register button is clicked");
                String registerName = binding.registerName.getText().toString();
                String registerEmail = binding.registerEmail.getText().toString();
                String registerPassword = binding.registerPassword.getText().toString();

                if (registerName.isEmpty() || registerEmail.isEmpty() || registerPassword.isEmpty()) {
                    Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(registerEmail, registerPassword).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            auth.signOut();
                            if (user != null) {
                                String userId = user.getUid();
                                // Initialize UserData with empty profileImage
                                UserData userData = new UserData(registerEmail, registerName, "");
                                userReference.child(userId).setValue(userData);
                                // Upload image to Firebase storage
                                StorageReference storageReference = storage.getReference().child("profile_image/" + userId + ".jpg");

                                if (imageUri != null) {
                                    storageReference.putFile(imageUri).addOnCompleteListener(uploadTask -> {
                                        if (uploadTask.isSuccessful()) {
                                            storageReference.getDownloadUrl().addOnCompleteListener(uriTask -> {
                                                if (uriTask.isSuccessful()) {
                                                    String imageUrl = uriTask.getResult().toString();
                                                    // Save the image URL to the Realtime Database
                                                    userReference.child(userId).child("profileImage").setValue(imageUrl);
                                                }
                                            });
                                        }
                                    });
                                }
                                Toast.makeText(this, "User Registration Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            }
                        }
                    });
                }
            });
        }

        // Set an OnClickListener for the Choose image
        binding.cardView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.registerUserImage);
        }
    }

    public void showToast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

}
