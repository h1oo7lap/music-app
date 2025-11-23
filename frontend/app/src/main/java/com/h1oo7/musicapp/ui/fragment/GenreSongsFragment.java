
package com.h1oo7.musicapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.model.response.SongResponse;
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.ui.adapter.SongAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreSongsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private TextView tvTitle;
    private String genreName;
    private String genreId;

    public static GenreSongsFragment newInstance(String genreId, String genreName) {
        GenreSongsFragment fragment = new GenreSongsFragment();
        Bundle args = new Bundle();
        args.putString("genreId", genreId);
        args.putString("genreName", genreName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre_songs, container, false);

        tvTitle = view.findViewById(R.id.tv_genre_title);
        recyclerView = view.findViewById(R.id.recycler_genre_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SongAdapter();
        recyclerView.setAdapter(adapter);

        if (getArguments() != null) {
            genreId = getArguments().getString("genreId");
            genreName = getArguments().getString("genreName");
            tvTitle.setText(genreName);
        }

        loadSongsByGenre();

        return view;
    }

    private void loadSongsByGenre() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getAllSongs(1, 200, "").enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Song> allSongs = response.body().getSongs();
                    List<Song> filteredSongs = new ArrayList<>();

                    // LỌC CHẤM HẾT – SIÊU CHUẨN VỚI MODEL HIỆN TẠI!
                    for (Song song : allSongs) {
                        if (song.getGenre() != null &&
                                genreId.equals(song.getGenre().getId())) {
                            filteredSongs.add(song);
                        }
                    }

                    adapter.setSongs(filteredSongs);
                    adapter.notifyDataSetChanged();

                    // Hiển thị thông báo nếu không có bài hát
                    if (filteredSongs.isEmpty()) {
                        tvTitle.setText(genreName + " (Chưa có bài hát)");
                    }
                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}