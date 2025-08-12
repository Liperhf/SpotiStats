package com.example.spotistats.util

import android.annotation.SuppressLint
import android.content.Context

object AccountPrefs {
    private val PREFS_NAME = "settings"
    private val KEY_NICKNAME = "nickname"
    private val KEY_AVATAR = "avatar"

    @SuppressLint("CommitPrefEdits")
    fun saveNickname(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_NICKNAME, name).apply()
    }

    fun saveAvatar(context: Context, imageUri: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_AVATAR, imageUri).apply()
    }

    fun getNickname(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_NICKNAME, null)
    }

    fun getAvatar(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_AVATAR, null)
    }
}
