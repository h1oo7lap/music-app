package com.h1oo7.musicapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private List<Playlist> playlists = new ArrayList<>();
    private RecyclerView recyclerView; // Thêm biến này

    public PlaylistAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public PlaylistAdapter() {
        this.recyclerView = null;
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(0, playlist);
        notifyItemInserted(0);
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    public void setPlaylists(List<Playlist> newPlaylists) {
        playlists = new ArrayList<>(newPlaylists);
        notifyDataSetChanged();
    }

    public void removePlaylist(Playlist playlist) {
        int position = playlists.indexOf(playlist);
        if (position != -1) {
            playlists.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.tvName.setText(playlist.getName());
        holder.tvCount.setText(playlist.getSongCount() + " bài hát");

        // 1. BẤM VÀO TOÀN BỘ ITEM → CHUYỂN QUA CHI TIẾT PLAYLIST
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onPlaylistClick(playlist);
            }
        });

        // 2. BẤM VÀO NÚT 3 CHẤM → HIỆN MENU XÓA
        holder.btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), holder.btnMenu);
            popup.inflate(R.menu.menu_playlist_item);
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_delete) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDelete(playlist);
                    }
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    // Interface để xử lý xóa
    public interface OnDeleteClickListener {
        void onDelete(Playlist playlist);
    }

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCount;
        ImageView imgCover;
        ImageView btnMenu; // THÊM DÒNG NÀY!!!

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_playlist_name);
            tvCount = itemView.findViewById(R.id.tv_song_count);
            imgCover = itemView.findViewById(R.id.img_playlist_cover);
            btnMenu = itemView.findViewById(R.id.btn_menu); // THÊM DÒNG NÀY!!!
        }
    }

    public void setOnPlaylistClickListener(OnPlaylistClickListener listener) {
        this.clickListener = listener;
    }

    public interface OnPlaylistClickListener {
        void onPlaylistClick(Playlist playlist);
    }

    private OnPlaylistClickListener clickListener;

}