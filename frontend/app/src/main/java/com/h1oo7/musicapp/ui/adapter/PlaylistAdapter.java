package com.h1oo7.musicapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private List<Playlist> playlists = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(Playlist playlist);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setPlaylists(List<Playlist> data) {
        this.playlists = data != null ? data : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.tvTitle.setText(playlist.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(playlist);
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_playlist_name);
        }
    }
}
