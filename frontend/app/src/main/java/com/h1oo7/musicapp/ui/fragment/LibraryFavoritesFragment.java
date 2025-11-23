package com.h1oo7.musicapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryFavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_favorites, container, false);

        recyclerView = view.findViewById(R.id.recycler_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new SongAdapter();
        recyclerView.setAdapter(adapter);

        loadFavoriteSongs();

        return view;
    }

    private void loadFavoriteSongs() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getFavoriteSongs().enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getContext(), "Không tải được yêu thích", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Song> favorites = response.body();

                // Lấy danh sách ID yêu thích để adapter vẽ icon trái tim
                List<String> ids = new ArrayList<>();
                for (Song s : favorites) ids.add(s.get_id());
                SongAdapter.setGlobalFavoriteSongIds(ids);

                // Hiển thị danh sách lên RecyclerView
                adapter.setSongs(favorites);
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavoriteSongs(); // refresh khi quay lại tab
    }
}
