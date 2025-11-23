package com.h1oo7.musicapp.ui.adapter;

import static com.h1oo7.musicapp.utils.Constants.BASE_URL;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.h1oo7.musicapp.MainActivity;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Playlist;
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.model.request.AddRemoveSongRequest;
import com.h1oo7.musicapp.model.response.PlaylistResponse;
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.player.PlayerManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private final List<Song> songs = new ArrayList<>();

    public void setSongs(List<Song> songs) {
        this.songs.clear();
        if (songs != null) this.songs.addAll(songs);
        notifyDataSetChanged();
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs); // Trả về bản sao để an toàn
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.tvTitle.setText(song.getTitle());
        holder.tvArtist.setText(song.getArtist());
        holder.tvGenre.setText(song.getGenre() != null ? song.getGenre().getName() : "Unknown");
        holder.tvPlayCount.setText(song.getPlayCount() + " lượt nghe");

        // Load ảnh bìa bài hát
        String fixedImageUrl = song.getImageUrl() != null ? song.getImageUrl().replace("\\", "/") : "";
        Glide.with(holder.itemView.getContext())
                .load(BASE_URL + fixedImageUrl)
                .placeholder(R.drawable.ic_music_note)
                .into(holder.imgCover);

        // Khi click bài hát → phát nhạc + tăng lượt nghe + show mini-player
        holder.itemView.setOnClickListener(v -> {
            // 1. Phát nhạc
            PlayerManager.getInstance().playSong(song);

            // 2. Gọi API tăng lượt nghe, không cần xử lý response
            RetrofitClient.getClient()
                    .create(ApiService.class)
                    .incrementListen(song.get_id())
                    .enqueue(new Callback<Void>() {
                        @Override public void onResponse(Call<Void> call, Response<Void> response) {}
                        @Override public void onFailure(Call<Void> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

            // 3. Hiển thị MiniPlayer
            if (v.getContext() instanceof MainActivity) {
                ((MainActivity) v.getContext()).showMiniPlayer();
            }
        });

        holder.btnAddToPlaylist.setOnClickListener(v -> {
            showPlaylistSelectionDialog(v.getContext(), song);
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;

        TextView tvTitle, tvArtist, tvGenre, tvPlayCount;

        ImageView btnAddToPlaylist;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvArtist = itemView.findViewById(R.id.tv_artist);
            tvGenre = itemView.findViewById(R.id.tv_genre);
            tvPlayCount = itemView.findViewById(R.id.tv_play_count);
            btnAddToPlaylist = itemView.findViewById(R.id.btn_add_to_playlist);
        }
    }

    private void showPlaylistSelectionDialog(Context context, Song song) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getMyPlaylists().enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Playlist> playlists = response.body();

                    if (playlists.isEmpty()) {
                        Toast.makeText(context, "Bạn chưa có playlist nào", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String[] names = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        names = playlists.stream()
                                .map(Playlist::getName)
                                .toArray(String[]::new);
                    }

                    new AlertDialog.Builder(context)
                            .setTitle("Thêm vào playlist")
                            .setItems(names, (dialog, which) -> {
                                Playlist selected = playlists.get(which);
                                addSongToPlaylist(selected.getId(), song.get_id(), selected.getName(), context);
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Toast.makeText(context, "Lỗi tải playlist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addSongToPlaylist(String playlistId, String songId, String playlistName, Context context) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        AddRemoveSongRequest request = new AddRemoveSongRequest(playlistId, songId);

        api.addSongToPlaylist(request).enqueue(new Callback<PlaylistResponse>() {
            @Override
            public void onResponse(Call<PlaylistResponse> call, Response<PlaylistResponse> response) {
                if (response.isSuccessful()) {
                    // TOAST ĐẸP NHƯ SPOTIFY
                    Toast.makeText(context, "Đã thêm vào \"" + playlistName + "\"", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlaylistResponse> call, Throwable t) {
                Toast.makeText(context, "Lỗi thêm bài hát", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
