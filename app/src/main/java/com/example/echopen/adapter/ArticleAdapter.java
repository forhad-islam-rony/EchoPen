//package com.example.echopen.adapter;
//
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.echopen.Model.BlogItemModel;
//import com.example.echopen.databinding.ArticleItemBinding;
//
//import java.util.List;
//
//public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.BlogViewHolder> {
//
//    private final Context context;
//    private List<BlogItemModel> blogList;
//    private final OnItemClickListener itemClickListener;
//
//    public interface OnItemClickListener {
//        void onEditClick(BlogItemModel blogItem);
//        void onReadMoreClick(BlogItemModel blogItem);
//        void onDeleteClick(BlogItemModel blogItem);
//    }
//
//    public ArticleAdapter(Context context, List<BlogItemModel> blogList, OnItemClickListener itemClickListener) {
//        this.context = context;
//        this.blogList = blogList;
//        this.itemClickListener = itemClickListener;
//    }
//
//    @NonNull
//    @Override
//    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        ArticleItemBinding binding = ArticleItemBinding.inflate(inflater, parent, false);
//        return new BlogViewHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
//        BlogItemModel blogItem = blogList.get(position);
//        holder.bind(blogItem);
//    }
//
//    @Override
//    public int getItemCount() {
//        return blogList.size();
//    }
//
//    public void setData(List<BlogItemModel> blogSavedList) {
//        this.blogList = blogSavedList;
//        notifyDataSetChanged();
//    }
//
//    public class BlogViewHolder extends RecyclerView.ViewHolder {
//        private final ArticleItemBinding binding;
//
//        public BlogViewHolder(ArticleItemBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//
//        public void bind(BlogItemModel blogItem) {
//            binding.heading.setText(blogItem.getHeading());
//            Glide.with(binding.profile.getContext()).load(blogItem.getProfileImage()).into(binding.profile);
//            binding.userName.setText(blogItem.getUserName());
//            binding.date.setText(blogItem.getDate());
//            binding.post.setText(blogItem.getPost());
//
//            binding.readMoreButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    itemClickListener.onReadMoreClick(blogItem);
//                }
//            });
//            binding.editButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    itemClickListener.onEditClick(blogItem);
//                }
//            });
//            binding.DeleteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    itemClickListener.onDeleteClick(blogItem);
//                }
//            });
//        }
//    }
//}


package com.example.echopen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.echopen.Model.BlogItemModel;
import com.example.echopen.R;
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
        holder.bind(blogList.get(position));
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public void setData(List<BlogItemModel> blogSavedList) {
        this.blogList = blogSavedList;
        notifyDataSetChanged();
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ArticleItemBinding binding;

        public BlogViewHolder(ArticleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.readMoreButton.setOnClickListener(this);
            binding.editButton.setOnClickListener(this);
            binding.deleteButton.setOnClickListener(this);
        }

        public void bind(BlogItemModel blogItem) {
            binding.heading.setText(blogItem.getHeading());
            Glide.with(binding.profile.getContext()).load(blogItem.getProfileImage()).into(binding.profile);
            binding.userName.setText(blogItem.getUserName());
            binding.date.setText(blogItem.getDate());
            binding.post.setText(blogItem.getPost());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                BlogItemModel blogItem = blogList.get(position);
                switch (v.getId()) {
                    case R.id.read_more_button:
                        itemClickListener.onReadMoreClick(blogItem);
                        break;
                    case R.id.edit_button:
                        itemClickListener.onEditClick(blogItem);
                        break;
                    case R.id.delete_button:
                        itemClickListener.onDeleteClick(blogItem);
                        break;
                }
            }
        }
    }
}
