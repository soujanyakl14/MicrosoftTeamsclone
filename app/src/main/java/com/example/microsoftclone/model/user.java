package com.example.microsoftclone.model;

import java.io.Serializable;

public class User {
    public String firstname,lastname,email,token,id;
    public User(){}

    public User(String firstname, String lastname, String email, String token,String id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.token = token;
        this.id=id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setUid(String id) {
        this.id = id;
    }
}
