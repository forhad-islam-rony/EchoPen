package com.example.echopen.Model;

public class UserData {
    private String email;
    private String name;
    private String profileImage;

    // Default constructor
    public UserData() {
        this.email = "";
        this.name = "";
        this.profileImage = "";
    }

    // Constructor with parameters
    public UserData(String email, String name, String profileImage) {
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
