// File: app/java/com/h1oo7/musicapp/ui/activity/RegisterActivity.java
package com.h1oo7.musicapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.RegisterRequest;
import com.h1oo7.musicapp.model.RegisterResponse;
import com.h1oo7.musicapp.model.User;          // ← THÊM DÒNG NÀY!!!
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.utils.SharedPrefManager;
import com.h1oo7.musicapp.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText edtUsername, edtDisplayName, edtPassword;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsername = findViewById(R.id.edtRegUsername);
        edtDisplayName = findViewById(R.id.edtRegDisplayName);
        edtPassword = findViewById(R.id.edtRegPassword);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        MaterialButton btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> register());
    }

    private void register() {
        String username = edtUsername.getText().toString().trim();
        String displayName = edtDisplayName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || displayName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(username, password, displayName);

        apiService.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    // ← ĐÃ SỬA ĐÚNG 100%
                    User registeredUser = response.body().getUser();
                    SharedPrefManager.getInstance(RegisterActivity.this)
                            .saveLogin("temp_token_after_register",
                                    registeredUser.get_id(),
                                    registeredUser.getRole());

                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại, thử tên khác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}