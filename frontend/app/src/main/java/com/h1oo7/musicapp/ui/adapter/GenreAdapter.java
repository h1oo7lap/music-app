// File: GenreAdapter.java
package com.h1oo7.musicapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Genre;
import com.h1oo7.musicapp.ui.fragment.GenreSongsFragment;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private List<Genre> genres = new ArrayList<>();
    private final Fragment fragment; // Bắt buộc phải có để navigate

    // Constructor bắt buộc nhận Fragment
    public GenreAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres != null ? genres : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_genre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.tvGenreName.setText(genre.getName());

        // Click vào thể loại → chuyển sang màn hình danh sách bài hát
        holder.itemView.setOnClickListener(v -> {
            NavHostFragment.findNavController(fragment)
                    .navigate(
                            R.id.action_homeFragment_to_genreSongsFragment,
                            GenreSongsFragment.newInstance(genre.getId(), genre.getName()).getArguments()
                    );
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    // ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGenreName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenreName = itemView.findViewById(R.id.tv_genre_name);
        }
    }
}