package com.example.musique.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musique.data.network.RetrofitClient
import com.example.musique.data.repository.SongRepository
import com.example.musique.databinding.FragmentSearchBinding
import com.example.musique.player.MusicPlayerManager
import com.example.musique.ui.adapter.SongAdapter
import com.example.musique.ui.viewmodel.SongViewModel
import com.example.musique.ui.viewmodel.ViewModelFactory
import com.example.musique.utils.PreferenceManager
import com.example.musique.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: SongViewModel
    private lateinit var songAdapter: SongAdapter
    private lateinit var musicPlayerManager: MusicPlayerManager
    
    private var searchJob: Job? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViewModel()
        setupRecyclerView()
        setupObservers()
        setupSearchListener()
        
        musicPlayerManager = MusicPlayerManager.getInstance(requireContext())
        
        viewModel.getSongs(null, 1, 20)
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
        
        binding.rvSongs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = songAdapter
        }
    }
    
    private fun setupObservers() {
        viewModel.songsState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                    songAdapter.submitList(resource.data?.songs)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = resource.message
                }
            }
        }
    }
    
    private fun setupSearchListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(500)
                    val keyword = s?.toString()?.trim()
                    viewModel.getSongs(keyword?.ifEmpty { null }, 1, 20)
                }
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
        _binding = null
    }
}
