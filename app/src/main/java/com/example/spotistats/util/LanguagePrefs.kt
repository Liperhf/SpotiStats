package com.example.spotistats.util

import android.content.Context

object LanguagePrefs {
    private const val PREFS_NAME = "settings"
    private const val KEY_LANGUAGE = "language"

    fun saveLanguage(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
    }

    fun getLanguage(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, null)
    }
}