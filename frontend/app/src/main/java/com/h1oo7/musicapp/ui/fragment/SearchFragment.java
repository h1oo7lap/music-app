package com.h1oo7.musicapp.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.h1oo7.musicapp.model.response.SongResponse;
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.ui.adapter.SongAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private TextInputEditText etSearch;
    private RecyclerView recyclerResults;
    private TextView tvNoResults;

    private SongAdapter adapter;

    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        etSearch = view.findViewById(R.id.et_search);
        recyclerResults = view.findViewById(R.id.recycler_search_results);
        tvNoResults = view.findViewById(R.id.tv_no_results);

        recyclerResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SongAdapter();
        recyclerResults.setAdapter(adapter);

        setupSearchListener();

        return view;
    }

    private void setupSearchListener() {
        searchRunnable = () -> {
            String keyword = etSearch.getText().toString().trim();
            if (keyword.isEmpty()) {
                adapter.setSongs(null);
                tvNoResults.setVisibility(View.GONE);
                return;
            }
            searchSongs(keyword);
        };

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchHandler.removeCallbacks(searchRunnable);
                searchHandler.postDelayed(searchRunnable, 400); // debounce 400ms
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchSongs(String keyword) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getAllSongs(1, 50, keyword).enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Song> results = response.body().getSongs();
                    adapter.setSongs(results);

                    if (results == null || results.isEmpty()) {
                        tvNoResults.setVisibility(View.VISIBLE);
                    } else {
                        tvNoResults.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
                tvNoResults.setVisibility(View.VISIBLE);
            }
        });
    }
}
