package com.h1oo7.musicapp.ui.adapter;

import static com.h1oo7.musicapp.utils.Constants.BASE_URL;

import android.app.AlertDialog;
import android.content.Context;
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
import com.h1oo7.musicapp.model.request.ToggleFavoriteRequest;
import com.h1oo7.musicapp.model.response.FavoriteResponse;
import com.h1oo7.musicapp.model.response.PlaylistResponse;
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.manager.PlayerManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private final List<Song> songs = new ArrayList<>();
    private boolean isInPlaylistDetail = false;
    private String currentPlaylistId = null;

    // Local list
    private final List<String> favoriteSongIds = new ArrayList<>();

    // GLOBAL list để fragment khác đọc được
    private static final List<String> globalFavoriteSongIds = new ArrayList<>();

    public void setFavoriteSongIds(List<String> ids) {
        favoriteSongIds.clear();
        if (ids != null) favoriteSongIds.addAll(ids);
        notifyDataSetChanged();
    }

    public static void setGlobalFavoriteSongIds(List<String> ids) {
        globalFavoriteSongIds.clear();
        if (ids != null) globalFavoriteSongIds.addAll(ids);
    }

    public interface OnSongRemovedListener {
        void onSongRemoved();
    }

    public interface OnFavoriteChangedListener {
        void onFavoriteChanged();
    }

    private OnSongRemovedListener onSongRemovedListener;
    private OnFavoriteChangedListener onFavoriteChangedListener;

    public void setOnSongRemovedListener(OnSongRemovedListener listener) {
        this.onSongRemovedListener = listener;
    }

    public void setOnFavoriteChangedListener(OnFavoriteChangedListener listener) {
        this.onFavoriteChangedListener = listener;
    }

    public void setInPlaylistDetail(boolean inPlaylistDetail, String playlistId) {
        this.isInPlaylistDetail = inPlaylistDetail;
        this.currentPlaylistId = playlistId;
        notifyDataSetChanged();
    }

    public void setSongs(List<Song> songs) {
        this.songs.clear();
        if (songs != null) this.songs.addAll(songs);
        notifyDataSetChanged();
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs);
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
        holder.tvGenre.setText(song.getGenre() != null ? song.getGenre().getName() : "Không rõ");
        holder.tvPlayCount.setText(song.getPlayCount() + " lượt nghe");

        String fixedImageUrl = song.getImageUrl() != null ? song.getImageUrl().replace("\\", "/") : "";
        Glide.with(holder.itemView.getContext())
                .load(BASE_URL + fixedImageUrl)
                .placeholder(R.drawable.ic_music_note)
                .into(holder.imgCover);

        if (isInPlaylistDetail) {
            holder.btnAddToPlaylist.setVisibility(View.GONE);
            holder.btnRemoveFromPlaylist.setVisibility(View.VISIBLE);
        } else {
            holder.btnAddToPlaylist.setVisibility(View.VISIBLE);
            holder.btnRemoveFromPlaylist.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            PlayerManager.getInstance().playSong(song);

            RetrofitClient.getClient().create(ApiService.class)
                    .incrementListen(song.get_id())
                    .enqueue(new Callback<Void>() {
                        @Override public void onResponse(Call<Void> call, Response<Void> response) {}
                        @Override public void onFailure(Call<Void> call, Throwable t) {}
                    });

            if (v.getContext() instanceof MainActivity) {
                ((MainActivity) v.getContext()).showMiniPlayer();
            }
        });

        holder.btnAddToPlaylist.setOnClickListener(v -> showPlaylistSelectionDialog(v.getContext(), song));

        holder.btnRemoveFromPlaylist.setOnClickListener(v -> {
            if (currentPlaylistId != null) {
                removeSongFromPlaylist(currentPlaylistId, song.get_id(), v.getContext());
            }
        });

        // ==== FAVORITE ICON ====
        if (globalFavoriteSongIds.contains(song.get_id())) {
            holder.btnFavorite.setImageResource(R.drawable.ic_favorite_fill);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }

        // ==== TOGGLE FAVORITE ====
        holder.btnFavorite.setOnClickListener(v -> {
            ApiService api = RetrofitClient.getClient().create(ApiService.class);

            api.toggleFavorite(new ToggleFavoriteRequest(song.get_id()))
                    .enqueue(new Callback<FavoriteResponse>() {
                        @Override
                        public void onResponse(Call<FavoriteResponse> call, Response<FavoriteResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {

                                // API trả về List<Song>, không phải List<String>
                                List<String> ids = new ArrayList<>();
                                for (Song fav : response.body().getFavorites()) {
                                    ids.add(fav.get_id());
                                }

                                setGlobalFavoriteSongIds(ids);

                                if (onFavoriteChangedListener != null)
                                    onFavoriteChangedListener.onFavoriteChanged();

                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<FavoriteResponse> call, Throwable t) {}
                    });
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover, btnAddToPlaylist, btnRemoveFromPlaylist, btnFavorite;
        TextView tvTitle, tvArtist, tvGenre, tvPlayCount;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvArtist = itemView.findViewById(R.id.tv_artist);
            tvGenre = itemView.findViewById(R.id.tv_genre);
            tvPlayCount = itemView.findViewById(R.id.tv_play_count);
            btnAddToPlaylist = itemView.findViewById(R.id.btn_add_to_playlist);
            btnRemoveFromPlaylist = itemView.findViewById(R.id.btn_remove_from_playlist);
            btnFavorite = itemView.findViewById(R.id.btn_favourite);
        }
    }

    // ================== ADD / REMOVE PLAYLIST ==================
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

                    String[] names = new String[playlists.size()];
                    for (int i = 0; i < playlists.size(); i++)
                        names[i] = playlists.get(i).getName();

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
        api.addSongToPlaylist(new AddRemoveSongRequest(playlistId, songId))
                .enqueue(new Callback<PlaylistResponse>() {
                    @Override
                    public void onResponse(Call<PlaylistResponse> call, Response<PlaylistResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Đã thêm vào \"" + playlistName + "\"", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlaylistResponse> call, Throwable t) {
                        Toast.makeText(context, "Lỗi thêm bài hát", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeSongFromPlaylist(String playlistId, String songId, Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Xoá bài hát")
                .setMessage("Bạn có chắc muốn xoá bài hát này khỏi playlist?")
                .setPositiveButton("Xoá", (d, w) -> {
                    ApiService api = RetrofitClient.getClient().create(ApiService.class);
                    api.removeSongFromPlaylist(new AddRemoveSongRequest(playlistId, songId))
                            .enqueue(new Callback<PlaylistResponse>() {
                                @Override
                                public void onResponse(Call<PlaylistResponse> call, Response<PlaylistResponse> response) {
                                    if (response.isSuccessful() && onSongRemovedListener != null) {
                                        Toast.makeText(context, "Đã xoá khỏi playlist", Toast.LENGTH_SHORT).show();
                                        onSongRemovedListener.onSongRemoved();
                                    }
                                }

                                @Override
                                public void onFailure(Call<PlaylistResponse> call, Throwable t) {
                                    Toast.makeText(context, "Lỗi xoá bài hát", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
