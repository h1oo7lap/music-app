package com.h1oo7.musicapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.response.LoginResponse;
import com.h1oo7.musicapp.model.request.RegisterRequest;
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.utils.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.h1oo7.musicapp.MainActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edtUsername, edtPassword;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // KIỂM TRA ĐÃ ĐĂNG NHẬP CHƯA?
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            // Đã có token → nhảy thẳng vào Main
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        // Chưa login → mới hiện màn hình login
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        findViewById(R.id.btnLogin).setOnClickListener(v -> login());
        findViewById(R.id.tvRegister).setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void login() {
        String username = Objects.requireNonNull(edtUsername.getText()).toString().trim();
        String password = Objects.requireNonNull(edtPassword.getText()).toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest();
        request.username = username;
        request.password = password;

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();

                    SharedPrefManager.getInstance(LoginActivity.this)
                            .saveLogin(res.token, res._id, res.role);



                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    SharedPrefManager.getInstance(LoginActivity.this)
                            .saveLogin(res.token, res._id, res.role, res.username, res.displayName);

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}