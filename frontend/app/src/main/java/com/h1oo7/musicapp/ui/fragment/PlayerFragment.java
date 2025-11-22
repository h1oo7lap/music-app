package com.h1oo7.musicapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.player.PlayerManager;
import com.h1oo7.musicapp.utils.Constants;

public class PlayerFragment extends Fragment {

    private ImageView imgCover;
    private TextView tvTitle, tvArtist;
    private ImageButton btnPlayPause;

    private final Runnable updateUIRunnable = this::updateUI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        imgCover = view.findViewById(R.id.img_cover_big);
        tvTitle = view.findViewById(R.id.tv_title_big);
        tvArtist = view.findViewById(R.id.tv_artist_big);
        btnPlayPause = view.findViewById(R.id.btn_play_pause_big);

        btnPlayPause.setOnClickListener(v -> PlayerManager.getInstance().playOrPause());
        PlayerManager.getInstance().addListener(updateUIRunnable);

        view.post(() -> {
            updateUI();
            view.postDelayed(this::updateUI, 300);
        });

        return view;
    }

    private void updateUI() {
        Song song = PlayerManager.getInstance().getCurrentSong();
        if (song == null || getView() == null) return;

        tvTitle.setText(song.getTitle());
        tvArtist.setText(song.getArtist());

        String imageUrl = Constants.BASE_URL + song.getImageUrl().replace("\\", "/");
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_music_note)
                .into(imgCover);

        btnPlayPause.setImageResource(
                PlayerManager.getInstance().isPlaying()
                        ? R.drawable.ic_pause_circle_48
                        : R.drawable.ic_play_circle_48
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PlayerManager.getInstance().removeListener(updateUIRunnable);
    }
}