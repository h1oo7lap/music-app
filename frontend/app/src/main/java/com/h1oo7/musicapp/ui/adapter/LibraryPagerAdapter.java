package com.h1oo7.musicapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.h1oo7.musicapp.ui.fragment.LibraryFavoritesFragment;
import com.h1oo7.musicapp.ui.fragment.LibraryPlaylistsFragment;

public class LibraryPagerAdapter extends FragmentStateAdapter {

    public LibraryPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new LibraryPlaylistsFragment() : new LibraryFavoritesFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}