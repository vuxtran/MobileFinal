package com.vutrannguyen_k214111980_k21411ca.model;

public class Account {
    private String userId;
    private int id;
    private String passWord;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;

    public Account() {
        // Default constructor required for calls to DataSnapshot.getValue(Account.class)
    }

    public Account(String userId, int id, String passWord, String fullName, String address, String phoneNumber, String email) {
        this.userId = userId;
        this.id = id;
        this.passWord = passWord;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // Getter and setter methods

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
