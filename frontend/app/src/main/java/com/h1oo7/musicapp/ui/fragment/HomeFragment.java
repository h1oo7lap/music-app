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
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.model.SongResponse;
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.ui.adapter.SongAdapter;
import com.h1oo7.musicapp.utils.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerSongs;
    private RecyclerView recyclerTopSongs;
    private SongAdapter adapter;
    private SongAdapter topAdapter;

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

        loadTopSongs();
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

        api.getAllSongs().enqueue(new Callback<SongResponse>() {
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

    private void loadTopSongs() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getTopSongs().enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Giới hạn top 5 bài
                    List<Song> top5 = response.body().size() > 5 ? response.body().subList(0,5) : response.body();
                    topAdapter.setSongs(top5);
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