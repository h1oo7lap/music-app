package com.h1oo7.musicapp.player;

import android.content.Context;
import android.net.Uri;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private static PlayerManager instance;
    private ExoPlayer exoPlayer;
    private Song currentSong;
    private List<Runnable> listeners = new ArrayList<>();

    private PlayerManager() {}

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public void init(Context context) {
        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(context).build();
        }
    }

    public void playSong(Song song) {
        if (exoPlayer == null) return;

        currentSong = song;
        String fixedUrl = (Constants.BASE_URL + song.getSongUrl().replace("\\", "/")).trim();
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(fixedUrl));

        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();

        notifyListeners();

        // Tăng lượt nghe
        // (gọi API ở đây hoặc ở Adapter đều được, mình để cả 2 chỗ cho chắc)
    }

    public ExoPlayer getExoPlayer() { return exoPlayer; }
    public Song getCurrentSong() { return currentSong; }
    public boolean isPlaying() { return exoPlayer != null && exoPlayer.isPlaying(); }

    public void playOrPause() {
        if (exoPlayer == null) return;
        if (exoPlayer.isPlaying()) {
            exoPlayer.pause();
        } else {
            exoPlayer.play();
        }
        notifyListeners();
    }

    public void addListener(Runnable listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(Runnable listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    public void release() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        instance = null;
    }
}