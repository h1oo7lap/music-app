package com.h1oo7.musicapp.ui.adapter;

import static com.h1oo7.musicapp.utils.Constants.BASE_URL;

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
import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> songs = new ArrayList<>();

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
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
        holder.tvGenre.setText(song.getGenre().getName());
        holder.tvPlayCount.setText(song.getPlayCount() + " lượt nghe");

        String rawImageUrl = song.getImageUrl(); // ví dụ: uploads\images\xxx.jpg

        String fixedImageUrl = rawImageUrl.replace("\\", "/");

        String imageUrl = BASE_URL + fixedImageUrl; // thay IP của bạn vào đây

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_music_note)
                .into(holder.imgCover);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView tvTitle, tvArtist, tvGenre, tvPlayCount;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvArtist = itemView.findViewById(R.id.tv_artist);
            tvGenre = itemView.findViewById(R.id.tv_genre);
            tvPlayCount = itemView.findViewById(R.id.tv_play_count);
        }
    }
}