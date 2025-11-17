package com.example.musique.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musique.data.network.RetrofitClient
import com.example.musique.data.repository.SongRepository
import com.example.musique.databinding.FragmentHomeBinding
import com.example.musique.player.MusicPlayerManager
import com.example.musique.ui.adapter.SongAdapter
import com.example.musique.ui.viewmodel.SongViewModel
import com.example.musique.ui.viewmodel.ViewModelFactory
import com.example.musique.utils.PreferenceManager
import com.example.musique.utils.Resource

class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: SongViewModel
    private lateinit var songAdapter: SongAdapter
    private lateinit var musicPlayerManager: MusicPlayerManager
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViewModel()
        setupRecyclerView()
        setupObservers()
        
        musicPlayerManager = MusicPlayerManager.getInstance(requireContext())
        musicPlayerManager.bindService()
        
        viewModel.getTopSongs(10)
    }
    
    private fun setupViewModel() {
        val preferenceManager = PreferenceManager(requireContext())
        val apiService = RetrofitClient.getApiService(preferenceManager)
        val repository = SongRepository(apiService)
        val factory = ViewModelFactory(songRepository = repository)
        viewModel = ViewModelProvider(this, factory)[SongViewModel::class.java]
    }
    
    private fun setupRecyclerView() {
        songAdapter = SongAdapter(
            onSongClick = { song ->
                viewModel.setCurrentSong(song)
                musicPlayerManager.playSong(song)
                viewModel.incrementPlayCount(song._id)
            },
            onPlayClick = { song ->
                viewModel.setCurrentSong(song)
                musicPlayerManager.playSong(song)
                viewModel.incrementPlayCount(song._id)
            }
        )
        
        binding.rvTopSongs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = songAdapter
        }
    }
    
    private fun setupObservers() {
        viewModel.topSongsState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                    songAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = resource.message
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
