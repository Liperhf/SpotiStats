package com.example.spotistats.presentation.language

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.spotistats.domain.model.AppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LanguageViewModel @Inject constructor(
): ViewModel() {
    private val _language = mutableStateOf(AppLanguage.ENGLISH)
    val language: State<AppLanguage> = _language


    fun setLanguage(language: AppLanguage) {
        _language.value = language
    }
}

