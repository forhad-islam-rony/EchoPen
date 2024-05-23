//package com.example.echopen.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.echopen.Model.BlogItemModel;
//import com.example.echopen.R;
//import com.example.echopen.ReadMoreActivity;
//import com.example.echopen.databinding.BlogItemBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.*;
//import java.util.List;
//
//public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {
//
//    private final List<BlogItemModel> items;
//    private final FirebaseDatabase databaseReference = FirebaseDatabase.getInstance("https://echopen-1e18e-default-rtdb.firebaseio.com/");
//    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//    public BlogAdapter(List<BlogItemModel> items) {
//        this.items = items;
//    }
//
//    @NonNull
//    @Override
//    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        BlogItemBinding binding = BlogItemBinding.inflate(inflater, parent, false);
//        return new BlogViewHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
//        BlogItemModel blogItem = items.get(position);
//        holder.bind(blogItem);
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public class BlogViewHolder extends RecyclerView.ViewHolder {
//        private final BlogItemBinding binding;
//
//        public BlogViewHolder(BlogItemBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//
//        public void bind(BlogItemModel blogItemModel) {
//            String postId = blogItemModel.getPostId();
//            Context context = binding.getRoot().getContext();
//            binding.heading.setText(blogItemModel.getHeading());
//            Glide.with(binding.profile.getContext()).load(blogItemModel.getProfileImage()).into(binding.profile);
//            binding.userName.setText(blogItemModel.getUserName());
//            binding.date.setText(blogItemModel.getDate());
//            binding.post.setText(blogItemModel.getPost());
//            binding.likeCount.setText(String.valueOf(blogItemModel.getLikeCount()));
//
//            // Set onClickListener for the item
//            binding.getRoot().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Context context = binding.getRoot().getContext();
//                    Intent intent = new Intent(context, ReadMoreActivity.class);
//                    intent.putExtra("blogItem", blogItemModel);
//                    context.startActivity(intent);
//                }
//            });
//
//            DatabaseReference postLikeReference = postId != null ? databaseReference.getReference().child("blogs").child(postId).child("likes") : null;
//
//            if (currentUser != null && postLikeReference != null) {
//                postLikeReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            binding.likeButton.setImageResource(R.drawable.btn_star);
//                        } else {
//                            binding.likeButton.setImageResource(R.drawable.heart);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                    }
//                });
//            }
//
//            binding.likeButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (currentUser != null && postId != null) {
//                        handleLikedButtonClicked(postId, blogItemModel, binding);
//                    } else {
//                        Toast.makeText(context, "You have to login first", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//            DatabaseReference userReference = databaseReference.getReference().child("users").child(currentUser != null ? currentUser.getUid() : "");
//            DatabaseReference postSaveReference = postId != null ? userReference.child("saveBlogPosts").child(postId) : null;
//
//            if (postSaveReference != null) {
//                postSaveReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            binding.postSaveButton.setImageResource(R.drawable.save);
//                        } else {
//                            binding.postSaveButton.setImageResource(R.drawable.ic_menu_save);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                    }
//                });
//            }
//
//            binding.postSaveButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (currentUser != null && postId != null) {
//                        handleSaveButtonClicked(postId, blogItemModel, binding);
//                    } else {
//                        Toast.makeText(context, "You have to login first", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }
//
//    private void handleLikedButtonClicked(String postId, BlogItemModel blogItemModel, BlogItemBinding binding) {
//        DatabaseReference userReference = databaseReference.getReference().child("users").child(currentUser.getUid());
//        DatabaseReference postLikeReference = databaseReference.getReference().child("blogs").child(postId).child("likes");
//
//        postLikeReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    userReference.child("likes").child(postId).removeValue().addOnSuccessListener(aVoid -> {
//                        postLikeReference.child(currentUser.getUid()).removeValue();
//                        if (blogItemModel.getLikedBy() != null) {
//                            blogItemModel.getLikedBy().remove(currentUser.getUid());
//                        }
//                        updateLikeButtonImage(binding, false);
//
//                        int newLikeCount = blogItemModel.getLikeCount() - 1;
//                        blogItemModel.setLikeCount(newLikeCount);
//                        databaseReference.getReference().child("blogs").child(postId).child("likeCount").setValue(newLikeCount);
//                        notifyDataSetChanged();
//                    }).addOnFailureListener(e -> Log.e("LikedClicked", "onDataChange: Failed to unlike the blog " + e));
//                } else {
//                    userReference.child("likes").child(postId).setValue(true).addOnSuccessListener(aVoid -> {
//                        postLikeReference.child(currentUser.getUid()).setValue(true);
//                        if (blogItemModel.getLikedBy() != null) {
//                            blogItemModel.getLikedBy().add(currentUser.getUid());
//                        }
//                        updateLikeButtonImage(binding, true);
//
//                        int newLikeCount = blogItemModel.getLikeCount() + 1;
//                        blogItemModel.setLikeCount(newLikeCount);
//                        databaseReference.getReference().child("blogs").child(postId).child("likeCount").setValue(newLikeCount);
//                        notifyDataSetChanged();
//                    }).addOnFailureListener(e -> Log.e("LikedClicked", "onDataChange: Failed to like the blog " + e));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
//
//    private void updateLikeButtonImage(BlogItemBinding binding, boolean liked) {
//        if (liked) {
//            binding.likeButton.setImageResource(android.R.drawable.btn_star);
//        } else {
//            binding.likeButton.setImageResource(R.drawable.heart);
//        }
//    }
//
//    private void handleSaveButtonClicked(String postId, BlogItemModel blogItemModel, BlogItemBinding binding) {
//        DatabaseReference userReference = databaseReference.getReference().child("users").child(currentUser.getUid());
//        userReference.child("saveBlogPosts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    userReference.child("saveBlogPosts").child(postId).removeValue().addOnSuccessListener(aVoid -> {
//                        BlogItemModel clickedBlogItem = null;
//                        for (BlogItemModel item : items) {
//                            if (item.getPostId().equals(postId)) {
//                                clickedBlogItem = item;
//                                break;
//                            }
//                        }
//                        if (clickedBlogItem != null) {
//                            clickedBlogItem.setSaved(false);
//                        }
//                        notifyDataSetChanged();
//                        Context context = binding.getRoot().getContext();
//                        Toast.makeText(context, "Blog Unsaved!", Toast.LENGTH_SHORT).show();
//                    }).addOnFailureListener(e -> {
//                        Context context = binding.getRoot().getContext();
//                        Toast.makeText(context, "Failed to unSave The Blog", Toast.LENGTH_SHORT).show();
//                    });
//                    binding.postSaveButton.setImageResource(R.drawable.save);
//                } else {
//                    userReference.child("saveBlogPosts").child(postId).setValue(true).addOnSuccessListener(aVoid -> {
//                        BlogItemModel clickedBlogItem = null;
//                        for (BlogItemModel item : items) {
//                            if (item.getPostId().equals(postId)) {
//                                clickedBlogItem = item;
//                                break;
//                            }
//                        }
//                        if (clickedBlogItem != null) {
//                            clickedBlogItem.setSaved(true);
//                        }
//                        notifyDataSetChanged();
//                        Context context = binding.getRoot().getContext();
//                        Toast.makeText(context, "Blog Saved!", Toast.LENGTH_SHORT).show();
//                    }).addOnFailureListener(e -> {
//                        Context context = binding.getRoot().getContext();
//                        Toast.makeText(context, "Failed to save Blog", Toast.LENGTH_SHORT).show();
//                    });
//                    binding.postSaveButton.setImageResource(R.drawable.save);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
//
//    public void updateData(List<BlogItemModel> savedBlogsArticles) {
//        items.clear();
//        items.addAll(savedBlogsArticles);
//        notifyDataSetChanged();
//    }
//}


package com.example.echopen.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.echopen.Model.BlogItemModel;
import com.example.echopen.R;
import com.example.echopen.ReadMoreActivity;
import com.example.echopen.databinding.BlogItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private final List<BlogItemModel> items;
    private final FirebaseDatabase databaseReference = FirebaseDatabase.getInstance("https://echopen-1e18e-default-rtdb.firebaseio.com/");
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public BlogAdapter(List<BlogItemModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        BlogItemBinding binding = BlogItemBinding.inflate(inflater, parent, false);
        return new BlogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        BlogItemModel blogItem = items.get(position);
        holder.bind(blogItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder {
        private final BlogItemBinding binding;

        public BlogViewHolder(BlogItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BlogItemModel blogItemModel) {
            Context context = binding.getRoot().getContext();
            String postId = blogItemModel.getPostId();
            binding.heading.setText(blogItemModel.getHeading());
            Glide.with(context).load(blogItemModel.getProfileImage()).into(binding.profile);
            binding.userName.setText(blogItemModel.getUserName());
            binding.date.setText(blogItemModel.getDate());
            binding.post.setText(blogItemModel.getPost());
            binding.likeCount.setText(String.valueOf(blogItemModel.getLikeCount()));

            // Set onClickListener for the item
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(context, ReadMoreActivity.class);
                intent.putExtra("blogItem", blogItemModel);
                context.startActivity(intent);
            });

            DatabaseReference postLikeReference = postId != null ? databaseReference.getReference().child("blogs").child(postId).child("likes") : null;

            if (currentUser != null && postLikeReference != null) {
                postLikeReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            binding.likeButton.setImageResource(R.drawable.btn_star);
                        } else {
                            binding.likeButton.setImageResource(R.drawable.heart);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            binding.likeButton.setOnClickListener(v -> {
                if (currentUser != null && postId != null) {
                    handleLikedButtonClicked(postId, blogItemModel);
                } else {
                    Toast.makeText(context, "You have to login first", Toast.LENGTH_SHORT).show();
                }
            });

            DatabaseReference userReference = databaseReference.getReference().child("users").child(currentUser != null ? currentUser.getUid() : "");
            DatabaseReference postSaveReference = postId != null ? userReference.child("saveBlogPosts").child(postId) : null;

            if (postSaveReference != null) {
                postSaveReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            binding.postSaveButton.setImageResource(R.drawable.save);
                        } else {
                            binding.postSaveButton.setImageResource(R.drawable.ic_menu_save);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            binding.postSaveButton.setOnClickListener(v -> {
                if (currentUser != null && postId != null) {
                    handleSaveButtonClicked(postId, blogItemModel);
                } else {
                    Toast.makeText(context, "You have to login first", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleLikedButtonClicked(String postId, BlogItemModel blogItemModel) {
        DatabaseReference userReference = databaseReference.getReference().child("users").child(currentUser.getUid());
        DatabaseReference postLikeReference = databaseReference.getReference().child("blogs").child(postId).child("likes");

        postLikeReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userReference.child("likes").child(postId).removeValue().addOnSuccessListener(aVoid -> {
                        postLikeReference.child(currentUser.getUid()).removeValue();
                        if (blogItemModel.getLikedBy() != null) {
                            blogItemModel.getLikedBy().remove(currentUser.getUid());
                        }
                        updateLikeButtonImage(binding, false);
                        int newLikeCount = blogItemModel.getLikeCount() - 1;
                        blogItemModel.setLikeCount(newLikeCount);
                        databaseReference.getReference().child("blogs").child(postId).child("likeCount").setValue(newLikeCount);
                        notifyDataSetChanged();
                    }).addOnFailureListener(e -> Log.e("LikedClicked", "onDataChange: Failed to unlike the blog " + e));
                } else {
                    userReference.child("likes").child(postId).setValue(true).addOnSuccessListener(aVoid -> {
                        postLikeReference.child(currentUser.getUid()).setValue(true);
                        if (blogItemModel.getLikedBy() != null) {
                            blogItemModel.getLikedBy().add(currentUser.getUid());
                        }
                        updateLikeButtonImage(binding, true);
                        int newLikeCount = blogItemModel.getLikeCount() + 1;
                        blogItemModel.setLikeCount(newLikeCount);
                        databaseReference.getReference().child("blogs").child(postId).child("likeCount").setValue(newLikeCount);
                        notifyDataSetChanged();
                    }).addOnFailureListener(e -> Log.e("LikedClicked", "onDataChange: Failed to like the blog " + e));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateLikeButtonImage(BlogItemBinding binding, boolean liked) {
        if (liked) {
            binding.likeButton.setImageResource(android.R.drawable.btn_star);
        } else {
            binding.likeButton.setImageResource(R.drawable.heart);
        }
    }

    private void handleSaveButtonClicked(String postId, BlogItemModel blogItemModel) {
        DatabaseReference userReference = databaseReference.getReference().child("users").child(currentUser.getUid());
        userReference.child("saveBlogPosts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

