package com.h1oo7.musicapp.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.manager.PlayerManager;
import com.h1oo7.musicapp.utils.Constants;

//public class PlayerFragment extends Fragment {
//
//    private ImageView imgCover;
//    private TextView tvTitle, tvArtist;
//    private ImageButton btnPlayPause;
//
//    private final Runnable updateUIRunnable = this::updateUI;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_player, container, false);
//
//        imgCover = view.findViewById(R.id.img_cover_big);
//        tvTitle = view.findViewById(R.id.tv_title_big);
//        tvArtist = view.findViewById(R.id.tv_artist_big);
//        btnPlayPause = view.findViewById(R.id.btn_play_pause_big);
//
//        btnPlayPause.setOnClickListener(v -> PlayerManager.getInstance().playOrPause());
//        PlayerManager.getInstance().addListener(updateUIRunnable);
//
//        view.post(() -> {
//            updateUI();
//            view.postDelayed(this::updateUI, 300);
//        });
//
//        return view;
//    }
//
//    private void updateUI() {
//        Song song = PlayerManager.getInstance().getCurrentSong();
//        if (song == null || getView() == null) return;
//
//        tvTitle.setText(song.getTitle());
//        tvArtist.setText(song.getArtist());
//
//        String imageUrl = Constants.BASE_URL + song.getImageUrl().replace("\\", "/");
//        Glide.with(this)
//                .load(imageUrl)
//                .placeholder(R.drawable.ic_music_note)
//                .into(imgCover);
//
//        btnPlayPause.setImageResource(
//                PlayerManager.getInstance().isPlaying()
//                        ? R.drawable.ic_pause_circle_48
//                        : R.drawable.ic_play_circle_48
//        );
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        PlayerManager.getInstance().removeListener(updateUIRunnable);
//    }
//}

public class PlayerFragment extends Fragment {

    private ImageView imgCover, imgDisc;
    private TextView tvTitle, tvArtist, tvCurrentTime, tvTotalTime;
    private SeekBar seekBar;
    private ImageButton btnPlayPause, btnNext, btnPrevious, btnRepeat, btnShuffle;
    private View discContainer; // Thêm biến
    private final Runnable updateUIRunnable = this::updateUI;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private RotateAnimation rotateAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        discContainer = view.findViewById(R.id.disc_container); // Thêm dòng này
        imgCover = view.findViewById(R.id.img_cover_big);
        imgDisc = view.findViewById(R.id.img_disc);
        tvTitle = view.findViewById(R.id.tv_title_big);
        tvArtist = view.findViewById(R.id.tv_artist_big);
        tvCurrentTime = view.findViewById(R.id.tv_current_time);
        tvTotalTime = view.findViewById(R.id.tv_total_time);
        seekBar = view.findViewById(R.id.seekbar);
        btnPlayPause = view.findViewById(R.id.btn_play_pause_big);
        btnNext = view.findViewById(R.id.btn_next);
        btnPrevious = view.findViewById(R.id.btn_previous);

        btnPlayPause.setOnClickListener(v -> PlayerManager.getInstance().playOrPause());
        PlayerManager.getInstance().addListener(updateUIRunnable);

        startDiscRotation();
        updateUI();

        handler.post(updateSeekbarRunnable);

        return view;
    }

    private void updateUI() {
        Song song = PlayerManager.getInstance().getCurrentSong();
        if (song == null) return;

        tvTitle.setText(song.getTitle());
        tvArtist.setText(song.getArtist());

        String url = Constants.BASE_URL + song.getImageUrl().replace("\\", "/");
        Glide.with(this).load(url).placeholder(R.drawable.ic_music_note).into(imgCover);

        btnPlayPause.setImageResource(
                PlayerManager.getInstance().isPlaying()
                        ? R.drawable.ic_pause_circle_48
                        : R.drawable.ic_play_circle_48
        );

        if (PlayerManager.getInstance().isPlaying()) {
            resumeDiscRotation();
        } else {
            pauseDiscRotation();
        }

        tvTotalTime.setText(formatDuration(PlayerManager.getInstance().getExoPlayer().getDuration()));
    }

    private final Runnable updateSeekbarRunnable = new Runnable() {
        @Override
        public void run() {
            if (PlayerManager.getInstance().getExoPlayer() != null) {
                long current = PlayerManager.getInstance().getExoPlayer().getCurrentPosition();
                long total = PlayerManager.getInstance().getExoPlayer().getDuration();
                if (total > 0) {
                    seekBar.setProgress((int) (current * 100 / total));
                    tvCurrentTime.setText(formatDuration(current));
                }
            }
            handler.postDelayed(this, 500);
        }
    };

    private String formatDuration(long millis) {
        if (millis <= 0) return "0:00";
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PlayerManager.getInstance().removeListener(updateUIRunnable);
        handler.removeCallbacks(updateSeekbarRunnable);
        pauseDiscRotation();
    }

    private void startDiscRotation() {
        rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(20000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setInterpolator(new LinearInterpolator());

        discContainer.startAnimation(rotateAnimation); // QUAN TRỌNG: DÙNG discContainer!
    }

    private void pauseDiscRotation() {
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
            discContainer.clearAnimation(); // Thêm dòng này
        }
    }

    private void resumeDiscRotation() {
        if (rotateAnimation != null) {
            discContainer.startAnimation(rotateAnimation);
        }
    }
}