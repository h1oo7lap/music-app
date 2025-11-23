package com.h1oo7.musicapp.model;

import java.util.List;

public class Playlist {
    private String _id;
    private String name;
    private boolean isPublic;
    private List<Song> songs;

    public String getId() { return _id; }
    public String getName() { return name; }
    public boolean isPublic() { return isPublic; }
    public List<Song> getSongs() { return songs; }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
