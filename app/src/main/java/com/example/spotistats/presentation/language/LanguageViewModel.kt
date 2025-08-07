package com.example.spotistats.presentation.language

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.spotistats.domain.model.AppLanguage
import com.example.spotistats.util.LanguagePrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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

