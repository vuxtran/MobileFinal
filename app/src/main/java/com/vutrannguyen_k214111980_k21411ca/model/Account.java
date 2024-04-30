package com.vutrannguyen_k214111980_k21411ca.model;

import java.io.Serializable;

public class Account implements Serializable {
    private int Id;
    private String userName;
    private String passWord;

    public Account() {
    }

    public Account(int id, String userName, String passWord) {
        Id = id;
        this.userName = userName;
        this.passWord = passWord;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}

