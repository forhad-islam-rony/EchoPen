package com.example.echopen;

public class UserProfileHelper {

    private String profileImageUrl;
    private String username,email;

    public void setProfileImageUrl(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void set_name(String name){
        this.username=name;

    }

    public String getUsername(){
        return username;
    }

    public void set_email(String email){
        this.email=email;

    }

    public String getEmail(){
        return email;
    }

}
