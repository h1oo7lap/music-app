package com.example.musique.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.musique.data.network.RetrofitClient
import com.example.musique.data.repository.AuthRepository
import com.example.musique.databinding.FragmentProfileBinding
import com.example.musique.ui.activities.LoginActivity
import com.example.musique.ui.viewmodel.AuthViewModel
import com.example.musique.ui.viewmodel.ViewModelFactory
import com.example.musique.utils.PreferenceManager

class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: AuthViewModel
    private lateinit var preferenceManager: PreferenceManager
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViewModel()
        loadUserData()
        setupClickListeners()
    }
    
    private fun setupViewModel() {
        preferenceManager = PreferenceManager(requireContext())
        val apiService = RetrofitClient.getApiService(preferenceManager)
        val repository = AuthRepository(apiService, preferenceManager)
        val factory = ViewModelFactory(authRepository = repository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }
    
    private fun loadUserData() {
        binding.tvDisplayName.text = preferenceManager.getDisplayName()
        binding.tvUsername.text = "@${preferenceManager.getUsername()}"
    }
    
    private fun setupClickListeners() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            navigateToLogin()
        }
    }
    
    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
