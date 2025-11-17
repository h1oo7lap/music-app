package com.example.musique.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.musique.MainActivity
import com.example.musique.data.network.RetrofitClient
import com.example.musique.data.repository.AuthRepository
import com.example.musique.databinding.ActivityRegisterBinding
import com.example.musique.ui.viewmodel.AuthViewModel
import com.example.musique.ui.viewmodel.ViewModelFactory
import com.example.musique.utils.PreferenceManager
import com.example.musique.utils.Resource

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AuthViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViewModel()
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupViewModel() {
        val preferenceManager = PreferenceManager(this)
        val apiService = RetrofitClient.getApiService(preferenceManager)
        val repository = AuthRepository(apiService, preferenceManager)
        val factory = ViewModelFactory(authRepository = repository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }
    
    private fun setupObservers() {
        viewModel.registerState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnRegister.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(this, resource.message ?: "Registration failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val displayName = binding.etDisplayName.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            if (validateInput(username, displayName, password)) {
                viewModel.register(username, displayName, password)
            }
        }
        
        binding.tvLogin.setOnClickListener {
            finish()
        }
    }
    
    private fun validateInput(username: String, displayName: String, password: String): Boolean {
        if (username.isEmpty()) {
            binding.etUsername.error = "Username is required"
            return false
        }
        if (displayName.isEmpty()) {
            binding.etDisplayName.error = "Display name is required"
            return false
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            return false
        }
        if (password.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters"
            return false
        }
        return true
    }
    
    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
