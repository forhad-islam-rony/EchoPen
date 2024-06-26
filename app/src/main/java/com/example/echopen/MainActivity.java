package com.example.echopen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.echopen.Model.BlogItemModel;
import com.example.echopen.SingleTon.FirebaseDatabaseSingleton;
import com.example.echopen.adapter.BlogAdapter;
import com.example.echopen.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final DatabaseReference blogReference = FirebaseDatabaseSingleton.getInstance().getDatabaseReference();
    private final DatabaseReference userReference = FirebaseDatabaseSingleton.getInstance().getUserReference();
    private final List<BlogItemModel> blogItems = new ArrayList<>();
    private final List<BlogItemModel> filteredBlogItems = new ArrayList<>();
    private BlogAdapter blogAdapter;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.saveArticalButton.setOnClickListener(v -> startActivity(new Intent(this, SavedArticlesActivity.class)));
        binding.profileImage.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        binding.cardView2.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId != null) {
            loadUserProfileImage(userId);
        }

        RecyclerView recyclerView = binding.blogRecyclerView;
        blogAdapter = new BlogAdapter(filteredBlogItems);
        recyclerView.setAdapter(blogAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.searchBlog.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBlogItems(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBlogItems(newText);
                return false;
            }
        });

        // fetch data from Firebase database
        blogReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                blogItems.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    BlogItemModel blogItem = postSnapshot.getValue(BlogItemModel.class);
                    if (blogItem != null) {
                        blogItems.add(blogItem);
                    }
                }
                Collections.reverse(blogItems);
                filterBlogItems(binding.searchBlog.getQuery().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "Blog loading failed", Toast.LENGTH_SHORT).show();
            }
        });

        binding.floatingAddArticleButton.setOnClickListener(v -> startActivity(new Intent(this, AddArticleActivity.class)));
    }

    private void filterBlogItems(String query) {
        filteredBlogItems.clear();
        if (TextUtils.isEmpty(query)) {
            filteredBlogItems.addAll(blogItems);
        } else {
            for (BlogItemModel blogItem : blogItems) {
                if (blogItem.getUserName().toLowerCase().contains(query.toLowerCase()) ||
                        blogItem.getHeading().toLowerCase().contains(query.toLowerCase())) {
                    filteredBlogItems.add(blogItem);
                }
            }
        }
        blogAdapter.notifyDataSetChanged();
    }

    private void loadUserProfileImage(String userId) {
        userReference.child(userId).child("profileImage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String profileImageUrl = snapshot.getValue(String.class);
                if (profileImageUrl != null) {
                    Glide.with(MainActivity.this).load(profileImageUrl).into(binding.profileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error loading profile image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

