package com.h1oo7.musicapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Playlist;
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.player.PlayerManager;
import com.h1oo7.musicapp.ui.adapter.SongAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistDetailFragment extends Fragment {

    private static final String ARG_PLAYLIST_ID = "playlist_id";
    private static final String ARG_PLAYLIST_NAME = "playlist_name";

    private ImageView imgCover;
    private TextView tvName, tvCount, tvCreatedBy;
    private MaterialButton btnPlayAll;
    private RecyclerView recyclerSongs;
    private SongAdapter adapter;
    private String currentPlaylistId; // Lưu lại ID để dùng khi xoá

    public static PlaylistDetailFragment newInstance(String playlistId, String playlistName) {
        PlaylistDetailFragment fragment = new PlaylistDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYLIST_ID, playlistId);
        args.putString(ARG_PLAYLIST_NAME, playlistName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_detail, container, false);

        imgCover = view.findViewById(R.id.img_playlist_cover);
        tvName = view.findViewById(R.id.tv_playlist_name);
        tvCount = view.findViewById(R.id.tv_song_count);
        tvCreatedBy = view.findViewById(R.id.tv_created_by);
        btnPlayAll = view.findViewById(R.id.btn_play_all);
        recyclerSongs = view.findViewById(R.id.recycler_songs);

        setupRecyclerView();
        btnPlayAll.setOnClickListener(v -> playAllSongs());

        if (getArguments() != null) {
            currentPlaylistId = getArguments().getString(ARG_PLAYLIST_ID);
            String playlistName = getArguments().getString(ARG_PLAYLIST_NAME);
            tvName.setText(playlistName);
            loadPlaylistSongs(currentPlaylistId);
        }

        adapter.setInPlaylistDetail(true, currentPlaylistId);
        return view;
    }

    private void setupRecyclerView() {
        recyclerSongs.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SongAdapter();

        adapter.setOnSongRemovedListener(() -> loadPlaylistSongs(currentPlaylistId));

        recyclerSongs.setAdapter(adapter);
    }


    public void refreshPlaylist() {
        if (currentPlaylistId != null) {
            loadPlaylistSongs(currentPlaylistId);
        }
    }

    private void loadPlaylistSongs(String playlistId) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        // Lấy danh sách playlist để tìm playlist hiện tại
        api.getMyPlaylists().enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Playlist p : response.body()) {
                        if (p.getId().equals(playlistId)) {
                            List<String> songIds = new ArrayList<>();
                            for (Song s : p.getSongs()) {
                                songIds.add(s.get_id());
                            }
                            fetchSongsByIds(api, songIds);
                            tvCount.setText(p.getSongs().size() + " bài hát");
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi tải playlist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Gọi từng bài 1 vì backend chưa có batch API
    private void fetchSongsByIds(ApiService api, List<String> songIds) {
        List<Song> detailedSongs = new ArrayList<>();
        for (String id : songIds) {
            api.getSongById(id).enqueue(new Callback<Song>() {
                @Override
                public void onResponse(Call<Song> call, Response<Song> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        detailedSongs.add(response.body());
                        adapter.setSongs(detailedSongs); // cập nhật adapter liên tục
                    }
                }

                @Override
                public void onFailure(Call<Song> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void playAllSongs() {
        List<Song> songs = adapter.getSongs();
        if (!songs.isEmpty()) {
            PlayerManager.getInstance().playSong(songs.get(0));
        }
    }
}
