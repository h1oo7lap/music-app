package com.h1oo7.musicapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.h1oo7.musicapp.databinding.ActivityMainBinding;
import com.h1oo7.musicapp.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        BottomNavigationView bottomNav = binding.bottomNavigation;
        NavigationUI.setupWithNavController(bottomNav, navController);

        // ẨN/HIỆN TAB QUẢN TRỊ THEO ROLE
        String role = SharedPrefManager.getInstance(this).getUserRole();
        if (!"admin".equals(role)) {
            bottomNav.getMenu().removeItem(R.id.adminFragment);
        }
    }
}