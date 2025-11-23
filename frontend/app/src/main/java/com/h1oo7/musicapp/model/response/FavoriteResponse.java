// model/FavoriteResponse.java
package com.h1oo7.musicapp.model.response;

import com.h1oo7.musicapp.model.Song;

import java.util.List;

public class FavoriteResponse {
    private String message;
    private List<Song> favorites;

    public List<Song> getFavorites() { return favorites; }
}