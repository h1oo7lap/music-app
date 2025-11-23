package com.h1oo7.musicapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager instance;
    private static final String SHARED_PREF_NAME = "musicapp_prefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROLE = "role";

    private SharedPreferences sharedPreferences;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveLogin(String token, String userId, String role) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public String getDisplayName() {
        return sharedPreferences.getString("display_name", null);
    }

    public String getUsername() {
        return sharedPreferences.getString("username", null);
    }

    // Trong saveLogin() – lưu thêm
    public void saveLogin(String token, String userId, String role, String username, String displayName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_ROLE, role);
        editor.putString("username", username);
        editor.putString("display_name", displayName);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public String getUserRole() {
        return sharedPreferences.getString

                (KEY_ROLE, "user");
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    // HÀM MỚI – SIÊU QUAN TRỌNG!
    public boolean isAdmin() {
        String role = getUserRole();
        return role != null && role.equalsIgnoreCase("admin");
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}