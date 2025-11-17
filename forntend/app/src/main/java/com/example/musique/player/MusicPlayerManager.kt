package com.example.musique.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.musique.data.model.Song

class MusicPlayerManager private constructor(private val context: Context) {
    
    private var musicPlayerService: MusicPlayerService? = null
    private var isBound = false
    
    companion object {
        @Volatile
        private var instance: MusicPlayerManager? = null
        
        fun getInstance(context: Context): MusicPlayerManager {
            return instance ?: synchronized(this) {
                instance ?: MusicPlayerManager(context.applicationContext).also { instance = it }
            }
        }
    }
    
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isBound = true
        }
        
        override fun onServiceDisconnected(name: ComponentName?) {
            musicPlayerService = null
            isBound = false
        }
    }
    
    fun bindService() {
        if (!isBound) {
            val intent = Intent(context, MusicPlayerService::class.java)
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            context.startService(intent)
        }
    }
    
    fun unbindService() {
        if (isBound) {
            context.unbindService(serviceConnection)
            isBound = false
        }
    }
    
    fun playSong(song: Song) {
        musicPlayerService?.playSong(song.songUrl, song.title, song.artist)
    }
    
    fun pause() {
        musicPlayerService?.pause()
    }
    
    fun resume() {
        musicPlayerService?.resume()
    }
    
    fun stop() {
        musicPlayerService?.stop()
    }
    
    fun isPlaying(): Boolean = musicPlayerService?.isPlaying() ?: false
    
    fun getCurrentPosition(): Long = musicPlayerService?.getCurrentPosition() ?: 0L
    
    fun getDuration(): Long = musicPlayerService?.getDuration() ?: 0L
    
    fun seekTo(position: Long) {
        musicPlayerService?.seekTo(position)
    }
}
