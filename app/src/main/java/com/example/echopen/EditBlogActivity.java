package com.example.echopen;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.echopen.Model.BlogItemModel;
import com.example.echopen.databinding.ActivityEditBlogBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EditBlogActivity extends AppCompatActivity {

    private ActivityEditBlogBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBlogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageButton.setOnClickListener(v -> finish());

        BlogItemModel blogItemModel = getIntent().getParcelableExtra("blogItem");

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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://echopen-1e18e-default-rtdb.firebaseio.com/").getReference("blogs");
        String postId = blogItemModel.getPostId();

        databaseReference.child(postId).setValue(blogItemModel)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditBlogActivity.this, "Blog Updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditBlogActivity.this, "Blog Updated Unsuccessfully", Toast.LENGTH_SHORT).show());
    }
}
