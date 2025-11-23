// model/PlaylistResponse.java
package com.h1oo7.musicapp.model.response;

import com.h1oo7.musicapp.model.Playlist;

public class PlaylistResponse {
    private String message;
    private Playlist playlist;

    public Playlist getPlaylist() { return playlist; }
    public String getMessage() { return message; }
}