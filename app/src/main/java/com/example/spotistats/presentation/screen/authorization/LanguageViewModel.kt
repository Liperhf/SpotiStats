package com.example.spotistats.presentation.screen.authorization

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.spotistats.domain.model.AppLanguage

class LanguageViewModel:ViewModel(
) {
    private val _language = mutableStateOf(AppLanguage.ENGLISH)
    val language: State<AppLanguage> = _language

    fun setLanguage(language: AppLanguage){
        _language.value = language
    }
}