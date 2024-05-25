//package com.example.echopen;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.example.echopen.Model.BlogItemModel;
//import com.example.echopen.databinding.ActivityReadMoreBinding;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.*;
//
//public class ReadMoreActivity extends AppCompatActivity {
//
//    private ActivityReadMoreBinding binding;
//    private DatabaseReference blogReference;
//    private DatabaseReference userSavedPostsReference;
//    private String postId;
//    private FirebaseAuth mAuth;
//    private FloatingActionButton saveButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityReadMoreBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        postId = getIntent().getStringExtra("postId");
//        saveButton = findViewById(R.id.saveButton);
//
//        if (postId == null) {
//            Toast.makeText(this, "No blog post ID provided", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        mAuth = FirebaseAuth.getInstance();
//
//        blogReference = FirebaseDatabase.getInstance("https://echopen-1e18e-default-rtdb.firebaseio.com/")
//                .getReference().child("blogs").child(postId);
//
//        if (mAuth.getCurrentUser() != null) {
//            userSavedPostsReference = FirebaseDatabase.getInstance().getReference()
//                    .child("users").child(mAuth.getCurrentUser().getUid()).child("saveBlogPosts");
//        }
//
//        binding.backButton.setOnClickListener(v -> finish());
//
//        binding.likeButton.setOnClickListener(v -> likePost());
//
//        binding.saveButton.setOnClickListener(v -> savePost());
//
//        fetchBlogPost();
//    }
//
//    private void fetchBlogPost() {
//        blogReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                BlogItemModel blogItem = snapshot.getValue(BlogItemModel.class);
//                if (blogItem != null) {
//                    displayBlogPost(blogItem);
//                } else {
//                    Toast.makeText(ReadMoreActivity.this, "Blog post not found", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ReadMoreActivity.this, "Failed to load blog post", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void displayBlogPost(BlogItemModel blogItem) {
//        binding.titleText.setText(blogItem.getHeading());
//        binding.userName.setText(blogItem.getUserName());
//        binding.Date.setText(blogItem.getDate());
//        binding.blogDescriptionTextView.setText(blogItem.getPost());
//        loadImage(blogItem.getProfileImage());
//
//        if (mAuth.getCurrentUser() != null) {
//            blogReference.child("likes").child(mAuth.getCurrentUser().getUid())
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()) {
//                                binding.likeButton.setImageResource(R.drawable.btn_star);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Toast.makeText(ReadMoreActivity.this, "Failed to check like status", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//            userSavedPostsReference.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        saveButton.setImageResource(R.drawable.save); // Adjust the icon as necessary
//                    } else {
//                        saveButton.setImageResource(R.drawable.ic_menu_save);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(ReadMoreActivity.this, "Failed to check saved status", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    private void loadImage(String imageUrl) {
//        Glide.with(this).load(imageUrl).into(binding.profileImage);
//    }
//
//    private void likePost() {
//        if (mAuth.getCurrentUser() != null) {
//            blogReference.child("likes").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        blogReference.child("likes").child(mAuth.getCurrentUser().getUid()).removeValue()
//                                .addOnSuccessListener(aVoid -> {
//                                    blogReference.child("likeCount").addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            if (snapshot.exists()) {
//                                                long likeCount = (long) snapshot.getValue();
//                                                if (likeCount > 0) {
//                                                    likeCount--;
//                                                    blogReference.child("likeCount").setValue(likeCount);
//                                                    Toast.makeText(ReadMoreActivity.this, "Post like cancelled", Toast.LENGTH_SHORT).show();
//                                                    binding.likeButton.setImageResource(R.drawable.love); // Change the icon to unliked
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//                                        }
//                                    });
//                                })
//                                .addOnFailureListener(e -> {
//                                });
//                    } else {
//                        blogReference.child("likes").child(mAuth.getCurrentUser().getUid()).setValue(true)
//                                .addOnSuccessListener(aVoid -> {
//                                    blogReference.child("likeCount").addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            if (snapshot.exists()) {
//                                                long likeCount = (long) snapshot.getValue();
//                                                likeCount++;
//                                                blogReference.child("likeCount").setValue(likeCount);
//                                                Toast.makeText(ReadMoreActivity.this, "Post liked", Toast.LENGTH_SHORT).show();
//                                                binding.likeButton.setImageResource(R.drawable.btn_star); // Change the icon to liked
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//                                        }
//                                    });
//                                })
//                                .addOnFailureListener(e -> {
//                                });
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//        } else {
//            Toast.makeText(this, "You need to login to like the post", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void savePost() {
//        if (mAuth.getCurrentUser() != null) {
//            userSavedPostsReference.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        userSavedPostsReference.child(postId).removeValue()
//                                .addOnSuccessListener(aVoid -> {
//                                    saveButton.setImageResource(R.drawable.ic_menu_save);
//                                    Toast.makeText(ReadMoreActivity.this, "Post unsaved", Toast.LENGTH_SHORT).show();
//                                })
//                                .addOnFailureListener(e -> Toast.makeText(ReadMoreActivity.this, "Failed to unsave the post", Toast.LENGTH_SHORT).show());
//                    } else {
//                        userSavedPostsReference.child(postId).setValue(true)
//                                .addOnSuccessListener(aVoid -> {
//                                    saveButton.setImageResource(R.drawable.save); // Adjust the icon as necessary
//                                    Toast.makeText(ReadMoreActivity.this, "Post saved", Toast.LENGTH_SHORT).show();
//                                })
//                                .addOnFailureListener(e -> Toast.makeText(ReadMoreActivity.this, "Failed to save the post", Toast.LENGTH_SHORT).show());
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(ReadMoreActivity.this, "Failed to check saved status", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(this, "You need to login to save the post", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
//
//





package com.example.echopen;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.echopen.Model.BlogItemModel;
import com.example.echopen.SingleTon.FirebaseDatabaseSingleton;
import com.example.echopen.databinding.ActivityReadMoreBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReadMoreActivity extends AppCompatActivity {

    private ActivityReadMoreBinding binding;
    private DatabaseReference blogReference;
    private DatabaseReference userSavedPostsReference;
    private String postId;
    private FirebaseAuth mAuth;
    private FloatingActionButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postId = getIntent().getStringExtra("postId");
        saveButton = findViewById(R.id.saveButton);

        if (postId == null) {
            Toast.makeText(this, "No blog post ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mAuth = FirebaseAuth.getInstance();

        blogReference = FirebaseDatabaseSingleton.getInstance().getDatabaseReference().child(postId);

        if (mAuth.getCurrentUser() != null) {
            userSavedPostsReference = FirebaseDatabaseSingleton.getInstance().getUserReference()
                    .child(mAuth.getCurrentUser().getUid()).child("saveBlogPosts");
        }

        binding.backButton.setOnClickListener(v -> finish());

        binding.likeButton.setOnClickListener(v -> likePost());

        binding.saveButton.setOnClickListener(v -> savePost());

        fetchBlogPost();
    }

    private void fetchBlogPost() {
        blogReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BlogItemModel blogItem = snapshot.getValue(BlogItemModel.class);
                if (blogItem != null) {
                    displayBlogPost(blogItem);
                } else {
                    Toast.makeText(ReadMoreActivity.this, "Blog post not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReadMoreActivity.this, "Failed to load blog post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBlogPost(BlogItemModel blogItem) {
        binding.titleText.setText(blogItem.getHeading());
        binding.userName.setText(blogItem.getUserName());
        binding.Date.setText(blogItem.getDate());
        binding.blogDescriptionTextView.setText(blogItem.getPost());
        loadImage(blogItem.getProfileImage());

        if (mAuth.getCurrentUser() != null) {
            blogReference.child("likes").child(mAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                binding.likeButton.setImageResource(R.drawable.red_love);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ReadMoreActivity.this, "Failed to check like status", Toast.LENGTH_SHORT).show();
                        }
                    });

            userSavedPostsReference.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        saveButton.setImageResource(R.drawable.red_save); // Adjust the icon as necessary
                    } else {
                        saveButton.setImageResource(R.drawable.final_save);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ReadMoreActivity.this, "Failed to check saved status", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadImage(String imageUrl) {
        Glide.with(this).load(imageUrl).into(binding.profileImage);
    }

    private void likePost() {
        if (mAuth.getCurrentUser() != null) {
            blogReference.child("likes").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        blogReference.child("likes").child(mAuth.getCurrentUser().getUid()).removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    blogReference.child("likeCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                long likeCount = (long) snapshot.getValue();
                                                if (likeCount > 0) {
                                                    likeCount--;
                                                    blogReference.child("likeCount").setValue(likeCount);
                                                    Toast.makeText(ReadMoreActivity.this, "Post like cancelled", Toast.LENGTH_SHORT).show();
                                                    binding.likeButton.setImageResource(R.drawable.final_love); // Change the icon to unliked
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                })
                                .addOnFailureListener(e -> {
                                });
                    } else {
                        blogReference.child("likes").child(mAuth.getCurrentUser().getUid()).setValue(true)
                                .addOnSuccessListener(aVoid -> {
                                    blogReference.child("likeCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                long likeCount = (long) snapshot.getValue();
                                                likeCount++;
                                                blogReference.child("likeCount").setValue(likeCount);
                                                Toast.makeText(ReadMoreActivity.this, "Post liked", Toast.LENGTH_SHORT).show();
                                                binding.likeButton.setImageResource(R.drawable.red_love); // Change the icon to liked
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                })
                                .addOnFailureListener(e -> {
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            Toast.makeText(this, "You need to login to like the post", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePost() {
        if (mAuth.getCurrentUser() != null) {
            userSavedPostsReference.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userSavedPostsReference.child(postId).removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    saveButton.setImageResource(R.drawable.red_save);
                                    Toast.makeText(ReadMoreActivity.this, "Post saved", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(ReadMoreActivity.this, "Failed to unsave the post", Toast.LENGTH_SHORT).show());
                    } else {
                        userSavedPostsReference.child(postId).setValue(true)
                                .addOnSuccessListener(aVoid -> {
                                    saveButton.setImageResource(R.drawable.final_save); // Adjust the icon as necessary
                                    Toast.makeText(ReadMoreActivity.this, "Post Unsaved", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(ReadMoreActivity.this, "Failed to save the post", Toast.LENGTH_SHORT).show());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ReadMoreActivity.this, "Failed to check saved status", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "You need to login to save the post", Toast.LENGTH_SHORT).show();
        }
    }
}

