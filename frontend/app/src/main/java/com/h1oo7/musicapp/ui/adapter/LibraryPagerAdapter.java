package com.h1oo7.musicapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.h1oo7.musicapp.ui.fragment.FavoriteSongsFragment;
import com.h1oo7.musicapp.ui.fragment.LibraryFragment;
import com.h1oo7.musicapp.ui.fragment.PlaylistFragment;

public class LibraryPagerAdapter extends FragmentStateAdapter {

    public LibraryPagerAdapter(@NonNull LibraryFragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new PlaylistFragment();
        else return new FavoriteSongsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
