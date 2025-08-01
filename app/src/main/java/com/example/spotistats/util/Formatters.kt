package com.example.spotistats.util

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun formatTime(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}