package com.h1oo7.musicapp.manager;

import android.content.Context;
import android.net.Uri;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
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


            exoPlayer.addListener(new Player.Listener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    notifyListeners();
                }

                @Override
                public void onPlaybackStateChanged(int state) {
                    if (state == Player.STATE_READY) {
                        notifyListeners();
                    }
                }
            });

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

    public void notifyListeners() {
        for (Runnable listener : new ArrayList<>(listeners)) { // tránh ConcurrentModification
            listener.run();
        }
    }

    public void release() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        listeners.clear();
        instance = null;
    }
}