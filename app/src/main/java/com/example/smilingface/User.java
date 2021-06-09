package com.example.smilingface;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String email;
    public int happy;
    public int sad;
    public int normal;

    public User(String email, int happy, int sad, int normal) {
        this.email = email;
        this.happy = happy;
        this.sad = sad;
        this.normal = normal;
    }

    public User() {
    }
}
