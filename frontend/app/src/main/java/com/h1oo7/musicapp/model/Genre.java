// app/java/com/h1oo7/musicapp/model/Genre.java
package com.h1oo7.musicapp.model;

public class Genre {
    public String _id;
    public String name;
    public String slug;

    public Genre() {}

    @Override
    public String toString() {
        return name; // để sau này hiển thị "Pop", "Hip Hop"...
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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