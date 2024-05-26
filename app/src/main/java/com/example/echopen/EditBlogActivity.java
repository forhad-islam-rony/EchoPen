




package com.example.echopen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.echopen.Model.BlogItemModel;
import com.example.echopen.SingleTon.FirebaseDatabaseSingleton;
import com.example.echopen.databinding.ActivityEditBlogBinding;
import com.google.firebase.database.DatabaseReference;

public class EditBlogActivity extends AppCompatActivity {

    private ActivityEditBlogBinding binding;
    private final DatabaseReference blogReference = FirebaseDatabaseSingleton.getInstance().getDatabaseReference();
    private BlogItemModel blogItemModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBlogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageButton.setOnClickListener(v -> finish());

        blogItemModel = getIntent().getParcelableExtra("blogItem");

        if (blogItemModel != null) {
            binding.blogTitle.getEditText().setText(blogItemModel.getHeading());
            binding.blogDescription.getEditText().setText(blogItemModel.getPost());
        }

        binding.saveBlogButton.setOnClickListener(v -> {
            String updatedTitle = binding.blogTitle.getEditText().getText().toString().trim();
            String updatedDescription = binding.blogDescription.getEditText().getText().toString().trim();

            if (updatedTitle.isEmpty() || updatedDescription.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            } else {
                if (blogItemModel != null) {
                    blogItemModel.setHeading(updatedTitle);
                    blogItemModel.setPost(updatedDescription);
                    updateDataInFirebase(blogItemModel);
                }
            }
        });
    }

    private void updateDataInFirebase(BlogItemModel blogItemModel) {
        String postId = blogItemModel.getPostId();

        if (postId != null && !postId.isEmpty()) {
            blogReference.child(postId).setValue(blogItemModel)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditBlogActivity.this, "Blog Updated successfully", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedBlogItem", blogItemModel);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(EditBlogActivity.this, "Blog Update Unsuccessful", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No blog post ID provided", Toast.LENGTH_SHORT).show();
        }
    }
}
