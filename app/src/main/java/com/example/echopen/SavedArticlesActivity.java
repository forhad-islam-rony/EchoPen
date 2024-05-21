package com.example.echopen;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echopen.Model.BlogItemModel;
import com.example.echopen.SingleTon.FirebaseDatabaseSingleton;
import com.example.echopen.adapter.BlogAdapter;
import com.example.echopen.databinding.ActivitySavedArticlesBinding;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class SavedArticlesActivity extends AppCompatActivity {

    private ActivitySavedArticlesBinding binding;
    private final List<BlogItemModel> savedBlogsArticles = new ArrayList<>();
    private BlogAdapter blogAdapter;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final DatabaseReference blogReference = FirebaseDatabaseSingleton.getInstance().getDatabaseReference();
    private final DatabaseReference userReference = FirebaseDatabaseSingleton.getInstance().getUserReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedArticlesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        blogAdapter = new BlogAdapter(savedBlogsArticles.stream().filter(BlogItemModel::isSaved).collect(Collectors.toList()));

        RecyclerView recyclerView = binding.savedArticleRecyclerView;
        recyclerView.setAdapter(blogAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (userId != null) {
            DatabaseReference userSavedPostsReference = userReference.child(userId).child("saveBlogPosts");

            userSavedPostsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        String postId = postSnapshot.getKey();
                        boolean isSaved = Boolean.TRUE.equals(postSnapshot.getValue(Boolean.class));
                        if (postId != null && isSaved) {
                            Future<?> future = executor.submit(() -> {
                                BlogItemModel blogItem = fetchBlogItem(postId);
                                if (blogItem != null) {
                                    savedBlogsArticles.add(blogItem);
                                    runOnUiThread(() -> blogAdapter.updateData(new ArrayList<>(savedBlogsArticles)));
                                }
                            });
                            try {
                                future.get(); // Wait for the task to complete
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle the error
                }
            });
        }

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private BlogItemModel fetchBlogItem(String postId) {
        try {
            DataSnapshot dataSnapshot = Tasks.await(blogReference.child(postId).get());
            return dataSnapshot.getValue(BlogItemModel.class);
        } catch (Exception e) {
            return null;
        }
    }
}
