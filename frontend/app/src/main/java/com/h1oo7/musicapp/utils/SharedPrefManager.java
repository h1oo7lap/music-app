// app/java/com/h1oo7/musicapp/utils/SharedPrefManager.java
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

    // Lưu khi login thành công
    public void saveLogin(String token, String userId, String role) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    // Lấy token
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // Lấy userId
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    // Lấy role (admin hoặc user) ← quan trọng nhất
    public String getUserRole() {
        return sharedPreferences.getString(KEY_ROLE, "user"); // default là user
    }

    // Kiểm tra đã login chưa
    public boolean isLoggedIn() {
        return getToken() != null;
    }

    // Đăng xuất
    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}