// app/java/com/h1oo7/musicapp/model/RegisterRequest.java
package com.h1oo7.musicapp.model.request;

public class RegisterRequest {
    public String username;
    public String password;
    public String displayName;

    // Constructor rỗng – Retrofit và Gson cần cái này
    public RegisterRequest() {}

    // Constructor đầy đủ – dùng khi register
    public RegisterRequest(String username, String password, String displayName) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}