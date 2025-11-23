package com.h1oo7.musicapp.manager;

import java.util.ArrayList;
import java.util.List;

public class FavoriteManager {

    private static final List<String> favoriteIds = new ArrayList<>();

    public static List<String> getFavoriteIds() {
        return favoriteIds;
    }

    public static void setFavoriteIds(List<String> ids) {
        favoriteIds.clear();
        if (ids != null) favoriteIds.addAll(ids);
    }

    public static boolean isFavorite(String songId) {
        return favoriteIds.contains(songId);
    }

    public static void toggle(String songId) {
        if (favoriteIds.contains(songId)) favoriteIds.remove(songId);
        else favoriteIds.add(songId);
    }
}
