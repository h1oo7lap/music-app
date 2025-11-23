package com.h1oo7.musicapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.ui.adapter.SongAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryFavoritesFragment extends Fragment {

    private RecyclerView recyclerFavorites;
    private TextView tvEmpty;
    private SongAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_favorites, container, false);

        recyclerFavorites = view.findViewById(R.id.recycler_favorites);
        tvEmpty = view.findViewById(R.id.tv_empty_favorites);

        recyclerFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SongAdapter();
        recyclerFavorites.setAdapter(adapter);

        loadFavoriteSongs();

        return view;
    }

    private void loadFavoriteSongs() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getFavoriteSongs().enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Song> songs = response.body();
                    adapter.setSongs(songs);

                    if (songs.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        recyclerFavorites.setVisibility(View.GONE);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                        recyclerFavorites.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("Lỗi tải danh sách yêu thích");
                recyclerFavorites.setVisibility(View.GONE);
            }
        });
    }
}