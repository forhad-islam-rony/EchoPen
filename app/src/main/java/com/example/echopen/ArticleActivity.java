package com.example.echopen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echopen.Model.BlogItemModel;
import com.example.echopen.SingleTon.FirebaseDatabaseSingleton;
import com.example.echopen.adapter.ArticleAdapter;
import com.example.echopen.databinding.ActivityArticleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity {

    private ActivityArticleBinding binding;
    private FirebaseAuth auth;
    private ArticleAdapter blogAdapter;
    private static final int EDIT_BLOG_REQUEST_CODE = 123;

    private final DatabaseReference blogReference = FirebaseDatabaseSingleton.getInstance().getDatabaseReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        RecyclerView recyclerView = binding.articleRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (currentUserId != null) {
            blogAdapter = new ArticleAdapter(this, new ArrayList<>(), new ArticleAdapter.OnItemClickListener() {
                @Override
                public void onEditClick(BlogItemModel blogItem) {
                    Intent intent = new Intent(ArticleActivity.this, ReadMoreActivity.class);
                    intent.putExtra("blogItem", blogItem);
                    startActivityForResult(intent, EDIT_BLOG_REQUEST_CODE);
                }

                @Override
                public void onReadMoreClick(BlogItemModel blogItem) {
                    Intent intent = new Intent(ArticleActivity.this, ReadMoreActivity.class);
                    intent.putExtra("blogItem", blogItem);
                    startActivity(intent);
                }

                @Override
                public void onDeleteClick(BlogItemModel blogItem) {
                    deleteBlogPost(blogItem);
                }
            });

            recyclerView.setAdapter(blogAdapter);

            blogReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<BlogItemModel> blogSavedList = new ArrayList<>();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        BlogItemModel blogSaved = postSnapshot.getValue(BlogItemModel.class);
                        if (blogSaved != null && currentUserId.equals(blogSaved.getUserId())) {
                            blogSavedList.add(blogSaved);
                        }
                    }
                    blogAdapter.setData(blogSavedList);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(ArticleActivity.this, "Error loading Saved blogs", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteBlogPost(BlogItemModel blogItem) {
        String postId = blogItem.getPostId();
        DatabaseReference blogPostReference = blogReference.child(postId);

        blogPostReference.removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(ArticleActivity.this, "Blog post deleted successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ArticleActivity.this, "Blog post deletion unsuccessful", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_BLOG_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Handle the result of the edited blog here
        }
    }
}
