package com.example.echopen;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.echopen.Model.BlogItemModel;
import com.example.echopen.databinding.ActivityReadMoreBinding;


public class ReadMoreActivity extends AppCompatActivity {

    private ActivityReadMoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        BlogItemModel blogs = getIntent().getParcelableExtra("blogItem");

        if (blogs != null) {
            binding.titleText.setText(blogs.getHeading());
            binding.userName.setText(blogs.getUserName());
            binding.Date.setText(blogs.getDate());
            binding.blogDescriptionTextView.setText(blogs.getPost());

            String userImageUrl = blogs.getProfileImage();
            Glide.with(this).load(userImageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImage);
        } else {
            Toast.makeText(this, "Failed to load blogs", Toast.LENGTH_SHORT).show();
        }
    }
}
