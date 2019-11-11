package com.example.idoctor.model;

import java.io.Serializable;

public class User implements Serializable {

    private String uid;
    private String email;
    private String name;
//    private String gender;
    private String photoURL;
    private int role;

    public User() {
    }

    public User(String uid, String email, String name, String photoURL, int role) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.photoURL = photoURL;
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
