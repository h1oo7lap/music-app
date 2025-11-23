package com.h1oo7.musicapp.model.request;

public class AddRemoveSongRequest {
    private String playlistId;
    private String songId;
    public AddRemoveSongRequest(String playlistId, String songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }
}