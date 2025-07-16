package com.example.spotistats.presentation.screen.authorization

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spotistats.domain.model.AppLanguage
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.useCases.SpotifyAuthUseCase
import com.example.spotistats.util.AccountPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log


@SuppressLint("StaticFieldLeak")
@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val spotifyAuthUseCase: SpotifyAuthUseCase
):ViewModel(
) {
    private val _language = mutableStateOf(AppLanguage.ENGLISH)
    val language: State<AppLanguage> = _language
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri
    private val _nickname = MutableStateFlow<String?>(null)
    val nickname: StateFlow<String?> = _nickname
    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile


    init {
        viewModelScope.launch {
            val savedNickname = AccountPrefs.getNickname(context)
            val savedAvatar = AccountPrefs.getAvatar(context)
            val userProfile = if ((savedAvatar.isNullOrEmpty() || savedNickname.isNullOrEmpty())) {
                try {
                    spotifyAuthUseCase.getUserProfile()
                } catch (e: Exception) {
                    null
                }
            } else null

            _nickname.value = when {
                !savedNickname.isNullOrEmpty() -> savedNickname
                userProfile != null -> userProfile.display_name ?: ""
                else -> ""
            }

            _imageUri.value = when {
                !savedAvatar.isNullOrEmpty() -> Uri.parse(savedAvatar)
                userProfile != null && !userProfile.imagesUrl.isNullOrEmpty() -> Uri.parse(userProfile.imagesUrl)
                else -> null
            }

            if (userProfile != null) {
                _profile.value = userProfile
                if (savedNickname.isNullOrEmpty()) AccountPrefs.saveNickname(context, userProfile.display_name ?: "")
                if (savedAvatar.isNullOrEmpty()) AccountPrefs.saveAvatar(context, userProfile.imagesUrl ?: "")
            }
        }
    }

        fun setNickName(nick: String) {
            _nickname.value = nick
        }

        fun setAvatar(uri: Uri) {
            _imageUri.value = uri
        }


        fun setLanguage(language: AppLanguage) {
            _language.value = language
        }

        fun saveProfile(context: Context) {
            _nickname.value?.let { AccountPrefs.saveNickname(context, it) }
            AccountPrefs.saveAvatar(context, _imageUri.value.toString())
        }


}

