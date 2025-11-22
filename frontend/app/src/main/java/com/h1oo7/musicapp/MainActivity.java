package com.h1oo7.musicapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.h1oo7.musicapp.databinding.ActivityMainBinding;
import com.h1oo7.musicapp.player.PlayerManager;
import com.h1oo7.musicapp.ui.fragment.MiniPlayerFragment;
import com.h1oo7.musicapp.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

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

        // --- Thay đổi: lắng nghe click thủ công ---
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeFragment) {
                // Luôn navigate về HomeFragment
                navController.navigate(R.id.homeFragment);
                return true;
            } else {
                // Các nút khác vẫn giữ hành vi cũ
                return NavigationUI.onNavDestinationSelected(item, navController);
            }
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
