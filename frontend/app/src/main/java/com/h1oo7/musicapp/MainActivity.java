package com.h1oo7.musicapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.h1oo7.musicapp.databinding.ActivityMainBinding;
import com.h1oo7.musicapp.manager.PlayerManager;
import com.h1oo7.musicapp.ui.fragment.MiniPlayerFragment;
import com.h1oo7.musicapp.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Ẩn tab Quản trị nếu không phải Admin
        Menu menu = bottomNav.getMenu();
        MenuItem adminItem = menu.findItem(R.id.adminFragment);

        if (!SharedPrefManager.getInstance(this).isAdmin()) {
            adminItem.setVisible(false);
        }

        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Init Player
        PlayerManager.getInstance().init(this);

        // KHÔNG ADD MINI PLAYER Ở ĐÂY NỮA

        // Setup Navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        BottomNavigationView bottomNav = binding.bottomNavigation;

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.homeFragment) {
                // Reset HomeFragment
                navController.popBackStack(R.id.homeFragment, true);
                navController.navigate(R.id.homeFragment);
                return true;
            }

            if (id == R.id.searchFragment) {
                // Reset SearchFragment
                navController.popBackStack(R.id.searchFragment, true);
                navController.navigate(R.id.searchFragment);
                return true;
            }
            if (id == R.id.libraryFragment) {
                // Reset LibraryFragment
                navController.popBackStack(R.id.libraryFragment, true);
                navController.navigate(R.id.libraryFragment);
                return true;
            }

            if (id == R.id.profileFragment) {
                // Reset ProfileFragment
                navController.popBackStack(R.id.profileFragment, true);
                navController.navigate(R.id.profileFragment);
                return true;
            }

            if (id == R.id.adminFragment) {
                // Reset AdminFragment
                navController.popBackStack(R.id.adminFragment, true);
                navController.navigate(R.id.adminFragment);
                return true;
            }

            // Các tab khác giữ behavior mặc định
            return NavigationUI.onNavDestinationSelected(item, navController);
        });


        // ẨN/HIỆN TAB QUẢN TRỊ THEO ROLE
        String role = SharedPrefManager.getInstance(this).getUserRole();
        if (!"admin".equals(role)) {
            bottomNav.getMenu().removeItem(R.id.adminFragment);
        }
    }

    // Hàm hiển thị MiniPlayer
    public void showMiniPlayer() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mini_player_container, new MiniPlayerFragment())
                .commitAllowingStateLoss();
    }
}
