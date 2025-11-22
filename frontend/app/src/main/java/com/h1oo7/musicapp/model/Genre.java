// app/java/com/h1oo7/musicapp/model/Genre.java
package com.h1oo7.musicapp.model;

import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("_id")
    private String id;  // Đổi thành "id" cho chuẩn Java

    public String name;
    public String slug;

    public Genre() {}

    @Override
    public String toString() {
        return name; // để sau này hiển thị "Pop", "Hip Hop"...
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}