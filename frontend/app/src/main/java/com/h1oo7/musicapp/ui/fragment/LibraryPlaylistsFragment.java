package com.h1oo7.musicapp.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Playlist;
import com.h1oo7.musicapp.model.request.CreatePlaylistRequest;
import com.h1oo7.musicapp.model.response.GenericResponse;
import com.h1oo7.musicapp.model.response.PlaylistResponse;
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.ui.adapter.PlaylistAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryPlaylistsFragment extends Fragment {

    private RecyclerView recyclerPlaylists;
    private FloatingActionButton fabAdd;
    private PlaylistAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_playlists, container, false);

        recyclerPlaylists = view.findViewById(R.id.recycler_playlists);
        fabAdd = view.findViewById(R.id.fab_add_playlist);

        setupRecyclerView();
        fabAdd.setOnClickListener(v -> showCreatePlaylistDialog());
        loadMyPlaylists();

        return view;
    }

    private void setupRecyclerView() {
        recyclerPlaylists.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PlaylistAdapter(recyclerPlaylists); // TRUYỀN VÀO ĐÂY!
        recyclerPlaylists.setAdapter(adapter);

        // THÊM ĐOẠN NÀY – SIÊU QUAN TRỌNG!!!
        adapter.setOnPlaylistClickListener(playlist -> {
            Bundle args = new Bundle();
            args.putString("playlist_id", playlist.getId());
            args.putString("playlist_name", playlist.getName());

            NavHostFragment.findNavController(LibraryPlaylistsFragment.this)
                    .navigate(R.id.action_libraryFragment_to_playlistDetailFragment, args);
        });
        // XỬ LÝ NÚT XÓA
        adapter.setOnDeleteClickListener(playlist -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Xóa playlist")
                    .setMessage("Bạn có chắc muốn xóa \"" + playlist.getName() + "\"?")
                    .setPositiveButton("Xóa", (d, w) -> deletePlaylist(playlist))
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void showCreatePlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_playlist, null);
        EditText etName = dialogView.findViewById(R.id.et_playlist_name);

        builder.setView(dialogView)
                .setTitle("Tạo playlist mới")
                .setPositiveButton("Tạo", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    if (!name.isEmpty()) {
                        createPlaylist(name);
                    } else {
                        Toast.makeText(requireContext(), "Vui lòng nhập tên playlist", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void createPlaylist(String name) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        CreatePlaylistRequest request = new CreatePlaylistRequest(name);

        api.createPlaylist(request).enqueue(new Callback<PlaylistResponse>() {
            @Override
            public void onResponse(Call<PlaylistResponse> call, Response<PlaylistResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Playlist playlist = response.body().getPlaylist();
                    adapter.addPlaylist(playlist);
                    Toast.makeText(requireContext(), "Đã tạo: " + name, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlaylistResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi tạo playlist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMyPlaylists() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getMyPlaylists().enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPlaylists(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void deletePlaylist(Playlist playlist) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.deletePlaylist(playlist.getId()).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful()) {
                    adapter.removePlaylist(playlist);
                    Toast.makeText(requireContext(), "Đã xóa playlist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi xóa playlist", Toast.LENGTH_SHORT).show();
            }
        });
    }


}