package com.example.tracknowdemo.models;

public class Users {
    String profilePic,fullName,email,password,userId,phoneNumber;

    public Users(String profilePic, String fullName,String phoneNumber, String email, String password, String userId) {
        this.profilePic = profilePic;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
    }

    public Users() {
    }
    //SignUp Constructor
    public Users(String userId,String fullName, String email,String phoneNumber, String password) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userId = userId;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
