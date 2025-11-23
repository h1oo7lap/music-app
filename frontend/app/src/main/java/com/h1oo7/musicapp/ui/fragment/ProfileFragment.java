package com.h1oo7.musicapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.h1oo7.musicapp.ui.activity.LoginActivity;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.utils.SharedPrefManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private CircleImageView imgAvatar;
    private TextView tvDisplayName, tvUsername, tvRole, tvUserId;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgAvatar = view.findViewById(R.id.img_avatar);
        tvDisplayName = view.findViewById(R.id.tv_display_name);
        tvUsername = view.findViewById(R.id.tv_username);
        tvRole = view.findViewById(R.id.tv_role);
        tvUserId = view.findViewById(R.id.tv_user_id);
        btnLogout = view.findViewById(R.id.btn_logout);

        loadUserInfo();

        btnLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void loadUserInfo() {
        SharedPrefManager pref = SharedPrefManager.getInstance(requireContext());

        tvDisplayName.setText(pref.getDisplayName() != null ? pref.getDisplayName() : "Người dùng");
        tvUsername.setText("@" + (pref.getUsername() != null ? pref.getUsername() : "user"));
        tvUserId.setText("ID: " + (pref.getUserId() != null ? pref.getUserId() : "N/A"));

        String role = pref.getUserRole();
        tvRole.setText("Vai trò: " + (role != null && role.equals("admin") ? "Quản trị viên" : "Người dùng"));
    }

    private void logout() {
        SharedPrefManager.getInstance(requireContext()).logout();
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}