// File: app/java/com/h1oo7/musicapp/ui/fragment/HomeFragment.java
package com.h1oo7.musicapp.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Genre;
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.model.response.SongResponse;
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.ui.adapter.GenreAdapter;
import com.h1oo7.musicapp.ui.adapter.SongAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerSongs;
    private RecyclerView recyclerTopSongs;
    private SongAdapter adapter;
    private SongAdapter topAdapter;
    // Thêm biến
    private RecyclerView recyclerGenres;
    private GenreAdapter genreAdapter;

    private int topSongPosition = 0;
    private Handler autoScrollHandler = new Handler();
    private Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (topAdapter.getItemCount() == 0) return;

            topSongPosition++;
            if (topSongPosition >= topAdapter.getItemCount()) {
                topSongPosition = 0; // quay vòng về đầu
            }

            recyclerTopSongs.smoothScrollToPosition(topSongPosition);

            autoScrollHandler.postDelayed(this, 3000); // lặp lại sau 3 giây
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo và thiết lập RecyclerView
        recyclerSongs = view.findViewById(R.id.recycler_songs);
        recyclerSongs.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SongAdapter();
        recyclerSongs.setAdapter(adapter);

        // Recycler danh sách Top 5 bài hát
        recyclerTopSongs = view.findViewById(R.id.recycler_top_songs);
        recyclerTopSongs.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        topAdapter = new SongAdapter();
        recyclerTopSongs.setAdapter(topAdapter);

        recyclerGenres = view.findViewById(R.id.recycler_genres);
        recyclerGenres.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        genreAdapter = new GenreAdapter(this); // this = HomeFragment
        recyclerGenres.setAdapter(genreAdapter);

        loadTopSongs();
        loadGenres();
        loadAllSongs();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }
    private void loadAllSongs() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        // Lấy 50 bài đầu tiên (page 1, limit 50) – đủ dùng cho Home
        api.getAllSongs(1, 50, "").enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setSongs(response.body().getSongs());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadGenres() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getAllGenres().enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    genreAdapter.setGenres(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                t.printStackTrace();
                // Có thể thêm Toast lỗi nếu muốn
            }
        });
    }
    private void loadTopSongs() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        // Lấy đúng 5 bài top
        api.getTopSongs(5).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topAdapter.setSongs(response.body());
                    topAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}