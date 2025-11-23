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
import com.h1oo7.musicapp.model.Playlist;
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.ui.adapter.PlaylistAdapter;
import com.h1oo7.musicapp.utils.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        recyclerView = view.findViewById(R.id.recycler_playlists);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PlaylistAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(playlist -> {
            // TODO: Navigate to playlist details fragment or show songs
            Toast.makeText(requireContext(), "Clicked: " + playlist.getName(), Toast.LENGTH_SHORT).show();
        });

        loadPlaylists();

        return view;
    }

    private void loadPlaylists() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        String token = "Bearer " + SharedPrefManager.getInstance(requireContext()).getToken();

        api.getMyPlaylists(token).enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPlaylists(response.body());
                } else {
                    Toast.makeText(requireContext(), "Không thể tải Playlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
