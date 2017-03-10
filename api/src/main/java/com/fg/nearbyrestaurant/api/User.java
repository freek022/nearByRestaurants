package com.fg.nearbyrestaurant.api;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by fred on 3/6/2017.
 */

@Entity
public class User {
    @Id String userName;
    @Index String name;
    @Index String email;
    @Index String password;

    public User(){}
    public User(String userName, String name, String email, String password){
        this.userName = userName;
        setName(name);
        setEmail(email);
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
