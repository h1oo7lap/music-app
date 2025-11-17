package com.example.musique.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musique.R

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context)
        .load(getFullUrl(url))
        .placeholder(R.drawable.ic_music_note)
        .error(R.drawable.ic_music_note)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun getFullUrl(path: String?): String? {
    if (path.isNullOrEmpty()) return null
    return if (path.startsWith("http")) {
        path
    } else {
        "http://10.0.2.2:5000/$path"
    }
}

fun Int.formatDuration(): String {
    val minutes = this / 60
    val seconds = this % 60
    return String.format("%d:%02d", minutes, seconds)
}
