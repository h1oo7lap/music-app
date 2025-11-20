// File: app/java/com/h1oo7/musicapp/model/User.java
package com.h1oo7.musicapp.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("_id")
    private String _id;

    @SerializedName("username")
    private String username;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("role")
    private String role;

    // Constructor rỗng
    public User() {}

    // Getter
    public String get_id() { return _id; }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public String getRole() { return role; }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setRole(String role) {
        this.role = role;
    }
}