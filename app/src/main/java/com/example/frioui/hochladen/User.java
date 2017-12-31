package com.example.frioui.hochladen;

/**
 * Created by Frioui on 30.12.2017.
 */

public class User {

    private String name;
    private String password;

    public User() {

    }
    public User(User user) {

        this.name = user.getName();
        this.password = user.getPassword();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
