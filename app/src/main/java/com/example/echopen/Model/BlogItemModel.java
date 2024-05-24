package com.example.echopen.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class BlogItemModel implements Parcelable {
    private String heading;
    private String userName;
    private String date;
    private String userId;
    private String post;
    private int likeCount;
    private String profileImage;
    private boolean isSaved;
    private String postId;
    private List<String> likedBy;


    public BlogItemModel() {
        this.heading = "null";
        this.userName = "null";
        this.date = "null";
        this.userId = "null";
        this.post = "null";
        this.likeCount = 0;
        this.profileImage = "null";
        this.isSaved = false;
        this.postId = "null";
        this.likedBy = null;
    }

    public BlogItemModel(String heading, String userName, String date, String post, String userId, int likeCount, String profileImage) {
        this.heading = heading;
        this.userName = userName;
        this.date = date;
        this.userId = userId;
        this.post = post;
        this.likeCount = likeCount;
        this.profileImage = profileImage;

    }

    protected BlogItemModel(Parcel in) {
        heading = in.readString();
        userName = in.readString();
        date = in.readString();
        userId = in.readString();
        post = in.readString();
        likeCount = in.readInt();
        profileImage = in.readString();
        isSaved = in.readByte() != 0;
        postId = in.readString();
    }

    public static final Creator<BlogItemModel> CREATOR = new Creator<BlogItemModel>() {
        @Override
        public BlogItemModel createFromParcel(Parcel in) {
            return new BlogItemModel(in);
        }

        @Override
        public BlogItemModel[] newArray(int size) {
            return new BlogItemModel[size];
        }
    };




    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(heading);
        dest.writeString(userName);
        dest.writeString(date);
        dest.writeString(post);
        dest.writeString(userId);
        dest.writeInt(likeCount);
        dest.writeString(profileImage);
        dest.writeByte((byte) (isSaved ? 1 : 0));
        dest.writeString(postId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and setters

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
    }


}

