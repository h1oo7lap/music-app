// model/Playlist.java
package com.h1oo7.musicapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Playlist {
    @SerializedName("_id")
    private String id;
    private String name;
    private String user;
    private List<Song> songs;
    private boolean isPublic;
    private String createdAt;

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Song> getSongs() { return songs != null ? songs : new ArrayList<>(); }
    public int getSongCount() { return getSongs().size(); }
}