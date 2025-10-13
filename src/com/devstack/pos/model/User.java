package com.devstack.pos.model;

public class User {
    String userId;
    String email;
    String displayName;
    String contactNumber;
    String password;

    public User() {
    }

    public User(String userId, String email, String displayName, String contactNumber, String password) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.contactNumber = contactNumber;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
