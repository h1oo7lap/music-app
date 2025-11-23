package com.h1oo7.musicapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class AdminSongAdapter extends RecyclerView.Adapter<AdminSongAdapter.SongViewHolder> {

    private final List<Song> songs = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEdit(Song song);
        void onDelete(Song song);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSongs(List<Song> list) {
        songs.clear();
        if (list != null) songs.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_admin, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.tvTitle.setText(song.getTitle());
        holder.tvArtist.setText(song.getArtist());
        holder.tvGenre.setText(song.getGenre() != null ? song.getGenre().getName() : "Không rõ");

        String imageUrl = song.getImageUrl() != null ? song.getImageUrl().replace("\\", "/") : "";
        Glide.with(holder.itemView.getContext())
                .load(Constants.BASE_URL + imageUrl)
                .placeholder(R.drawable.ic_music_note)
                .into(holder.imgCover);

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(song);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(song);
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover, btnEdit, btnDelete;
        TextView tvTitle, tvArtist, tvGenre;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvArtist = itemView.findViewById(R.id.tv_artist);
            tvGenre = itemView.findViewById(R.id.tv_genre);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
