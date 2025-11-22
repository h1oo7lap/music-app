//package com.h1oo7.musicapp.ui.fragment;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.fragment.NavHostFragment;
//import com.bumptech.glide.Glide;
//import com.h1oo7.musicapp.R;
//import com.h1oo7.musicapp.model.Song;
//import com.h1oo7.musicapp.player.PlayerManager;
//import com.h1oo7.musicapp.utils.Constants;
//
//public class MiniPlayerFragment extends Fragment {
//
//    private ImageView imgCover;
//    private TextView tvTitle, tvArtist;
//    private ImageButton btnPlayPause;
//
//    private final Runnable updateUI = this::updateUI;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_mini_player, container, false);
//
//        imgCover = view.findViewById(R.id.img_cover);
//        tvTitle = view.findViewById(R.id.tv_song_title);
//        tvArtist = view.findViewById(R.id.tv_artist);
//        btnPlayPause = view.findViewById(R.id.btn_play_pause);
//
//        view.setOnClickListener(v ->
//                NavHostFragment.findNavController(this).navigate(R.id.action_global_playerFragment));
//
//        btnPlayPause.setOnClickListener(v -> PlayerManager.getInstance().playOrPause());
//
//        PlayerManager.getInstance().addListener(updateUI);
//        updateUI();
//
//        return view;
//    }
//
//    private void updateUI() {
//        Song song = PlayerManager.getInstance().getCurrentSong();
//        if (song == null || song.getTitle() == null) {
//            requireView().setVisibility(View.GONE);
//            return;
//        }
//
//        requireView().setVisibility(View.VISIBLE);
//        tvTitle.setText(song.getTitle());
//        tvArtist.setText(song.getArtist());
//
//        String imageUrl = Constants.BASE_URL + song.getImageUrl().replace("\\", "/");
//        Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_music_note).into(imgCover);
//
//        btnPlayPause.setImageResource(
//                PlayerManager.getInstance().isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play
//        );
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        PlayerManager.getInstance().removeListener(updateUI);
//    }
//}

package com.h1oo7.musicapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.player.PlayerManager;
import com.h1oo7.musicapp.utils.Constants;

public class MiniPlayerFragment extends Fragment {

    private ImageView imgCover;
    private TextView tvTitle, tvArtist;
    private ImageButton btnPlayPause;

    private final Runnable updateUIRunnable = this::safeUpdateUI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mini_player, container, false);

        imgCover = view.findViewById(R.id.img_cover);
        tvTitle = view.findViewById(R.id.tv_song_title);
        tvArtist = view.findViewById(R.id.tv_artist);
        btnPlayPause = view.findViewById(R.id.btn_play_pause);

        view.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_global_playerFragment)
        );

        btnPlayPause.setOnClickListener(v ->
                PlayerManager.getInstance().playOrPause()
        );

        // Chỉ add listener, KHÔNG gọi updateUI tại đây vì view chưa sẵn sàng
        PlayerManager.getInstance().addListener(updateUIRunnable);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        safeUpdateUI();  // gọi khi view chắc chắn đã tạo xong
    }

    private void safeUpdateUI() {
        if (getView() == null) return;  // tránh crash
        updateUI();
    }

    private void updateUI() {
        View root = getView();
        if (root == null) return;

        Song song = PlayerManager.getInstance().getCurrentSong();

        if (song == null) {
            root.setVisibility(View.GONE);
            return;
        }

        root.setVisibility(View.VISIBLE);

        tvTitle.setText(song.getTitle());
        tvArtist.setText(song.getArtist());

        String imageUrl = Constants.BASE_URL + song.getImageUrl().replace("\\", "/");

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_music_note)
                .into(imgCover);

        btnPlayPause.setImageResource(
                PlayerManager.getInstance().isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PlayerManager.getInstance().removeListener(updateUIRunnable);
    }
}
