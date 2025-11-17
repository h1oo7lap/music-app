package com.example.musique.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.musique.MainActivity
import com.example.musique.R
import com.example.musique.utils.getFullUrl

class MusicPlayerService : MediaSessionService() {
    
    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer
    
    companion object {
        private const val CHANNEL_ID = "music_playback"
        private const val NOTIFICATION_ID = 1
    }
    
    override fun onCreate() {
        super.onCreate()
        
        createNotificationChannel()
        
        player = ExoPlayer.Builder(this).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
        }
        
        mediaSession = MediaSession.Builder(this, player).build()
    }
    
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
    
    fun playSong(songUrl: String, title: String, artist: String) {
        val fullUrl = getFullUrl(songUrl) ?: return
        
        val mediaItem = MediaItem.Builder()
            .setUri(fullUrl)
            .setMediaId(songUrl)
            .build()
        
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        
        showNotification(title, artist)
    }
    
    fun pause() {
        player.pause()
    }
    
    fun resume() {
        player.play()
    }
    
    fun stop() {
        player.stop()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
    
    fun isPlaying(): Boolean = player.isPlaying
    
    fun getCurrentPosition(): Long = player.currentPosition
    
    fun getDuration(): Long = player.duration
    
    fun seekTo(position: Long) {
        player.seekTo(position)
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Music player controls"
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun showNotification(title: String, artist: String) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(artist)
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        
        startForeground(NOTIFICATION_ID, notification)
    }
    
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
