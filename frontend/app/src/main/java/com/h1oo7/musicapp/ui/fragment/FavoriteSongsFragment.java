package com.h1oo7.musicapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.ui.adapter.SongAdapter;

import java.util.List;

public class FavoriteSongsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_songs, container, false);

        recyclerView = view.findViewById(R.id.recycler_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new SongAdapter();
        recyclerView.setAdapter(adapter);

        loadFavoriteSongs();

        return view;
    }

    private void loadFavoriteSongs() {
        // TODO: Gọi API / lưu local DB để load danh sách yêu thích
        // List<Song> favoriteSongs = ...
        // adapter.setSongs(favoriteSongs);
        // adapter.notifyDataSetChanged();
    }
}
