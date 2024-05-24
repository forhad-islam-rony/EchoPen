package com.example.echopen.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.echopen.Model.BlogItemModel;
import com.example.echopen.databinding.ArticleItemBinding;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.BlogViewHolder> {

    private final Context context;
    private List<BlogItemModel> blogList;
    private final OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onEditClick(BlogItemModel blogItem);
        void onReadMoreClick(BlogItemModel blogItem);
        void onDeleteClick(BlogItemModel blogItem);
    }

    public ArticleAdapter(Context context, List<BlogItemModel> blogList, OnItemClickListener itemClickListener) {
        this.context = context;
        this.blogList = blogList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ArticleItemBinding binding = ArticleItemBinding.inflate(inflater, parent, false);
        return new BlogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        BlogItemModel blogItem = blogList.get(position);
        holder.bind(blogItem);
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }
      public List<BlogItemModel> getBlogList() {
        return blogList;
    }

    public void setData(List<BlogItemModel> blogSavedList) {
//        blogList.clear();
//        blogList.addAll(newBlogList);
//        notifyDataSetChanged();
        this.blogList = blogSavedList;
        notifyDataSetChanged();
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder {
        private final ArticleItemBinding binding;

        public BlogViewHolder(ArticleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BlogItemModel blogItem) {
            binding.heading.setText(blogItem.getHeading());
            Glide.with(binding.profile.getContext()).load(blogItem.getProfileImage()).into(binding.profile);
            binding.userName.setText(blogItem.getUserName());
            binding.date.setText(blogItem.getDate());
            binding.post.setText(blogItem.getPost());

            binding.readMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onReadMoreClick(blogItem);
                }
            });
            binding.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onEditClick(blogItem);
                }
            });
            binding.DeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onDeleteClick(blogItem);
                }
            });
        }
    }
}


//package com.example.echopen.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.echopen.Model.BlogItemModel;
//import com.example.echopen.R;
//
//import java.util.List;
//
//public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
//
//    private final Context context;
//    private final List<BlogItemModel> blogList;
//    private final OnItemClickListener listener;
//
//    public interface OnItemClickListener {
//        void onEditClick(BlogItemModel blogItem);
//        void onReadMoreClick(BlogItemModel blogItem);
//        void onDeleteClick(BlogItemModel blogItem);
//    }
//
//    public ArticleAdapter(Context context, List<BlogItemModel> blogList, OnItemClickListener listener) {
//        this.context = context;
//        this.blogList = blogList;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.article_item, parent, false);
//        return new ArticleViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
//        BlogItemModel blogItem = blogList.get(position);
//        holder.bind(blogItem, listener);
//    }
//
//    @Override
//    public int getItemCount() {
//        return blogList.size();
//    }
//
//    public List<BlogItemModel> getBlogList() {
//        return blogList;
//    }
//
//    public void setData(List<BlogItemModel> newBlogList) {
//        blogList.clear();
//        blogList.addAll(newBlogList);
//        notifyDataSetChanged();
//    }
//
//    static class ArticleViewHolder extends RecyclerView.ViewHolder {
//        private final TextView titleTextView;
//        private final TextView descriptionTextView;
//        private final TextView dateTextView;
//        private final ImageView profileImageView;
//        private final Button editButton;
//        private final Button deleteButton;
//
//        public ArticleViewHolder(@NonNull View itemView) {
//            super(itemView);
//            titleTextView = itemView.findViewById(R.id.blogTitle);
//            descriptionTextView = itemView.findViewById(R.id.blogDescription);
//            dateTextView = itemView.findViewById(R.id.date);
//            profileImageView = itemView.findViewById(R.id.profile);
//            editButton = itemView.findViewById(R.id.editButton);
//            deleteButton = itemView.findViewById(R.id.DeleteButton);
//        }
//
//        public void bind(BlogItemModel blogItem, OnItemClickListener listener) {
//            titleTextView.setText(blogItem.getHeading());
//            descriptionTextView.setText(blogItem.getPost());
//            dateTextView.setText(blogItem.getDate());
//
//            Glide.with(profileImageView.getContext())
//                    .load(blogItem.getProfileImage())
//                    .into(profileImageView);
//
//            editButton.setOnClickListener(v -> listener.onEditClick(blogItem));
//            itemView.setOnClickListener(v -> listener.onReadMoreClick(blogItem));
//            deleteButton.setOnClickListener(v -> listener.onDeleteClick(blogItem));
//        }
//    }
//}
