package com.h1oo7.musicapp.model;

import java.util.List;

public class SongResponse {
    private List<Song> songs;
    private int page;
    private int limit;
    private int totalSongs;
    private int totalPages;

    public List<Song> getSongs() { return songs; }
    public void setSongs(List<Song> songs) { this.songs = songs; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }

    public int getTotalSongs() { return totalSongs; }
    public void setTotalSongs(int totalSongs) { this.totalSongs = totalSongs; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
}
