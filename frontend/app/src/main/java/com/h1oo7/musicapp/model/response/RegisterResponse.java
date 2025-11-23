// File: app/java/com/h1oo7/musicapp/model/RegisterResponse.java
package com.h1oo7.musicapp.model.response;

import com.google.gson.annotations.SerializedName;
import com.h1oo7.musicapp.model.User;

public class RegisterResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user;

    // Constructor rỗng (Gson cần)
    public RegisterResponse() {}

    // Getter
    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    // Setter (nếu cần)
    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(User user) {
        this.user = user;
    }
}