package com.example.spotistats.presentation.account

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotistats.domain.useCases.UserUseCase
import com.example.spotistats.util.AccountPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@SuppressLint("StaticFieldLeak")
@HiltViewModel
class AccountViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userUseCase: UserUseCase
):ViewModel(
) {
    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            val savedNickname = AccountPrefs.getNickname(context)
            val savedAvatar = AccountPrefs.getAvatar(context)
            val userProfile = if ((savedAvatar.isNullOrEmpty() || savedNickname.isNullOrEmpty())) {
                try {
                    userUseCase.getUserProfile()
                } catch (e: Exception) {
                    null
                }
            } else null

            val nickname = when {
                !savedNickname.isNullOrEmpty() -> savedNickname
                userProfile != null -> userProfile.display_name ?: ""
                else -> ""
            }

            val imageUrl = when {
                !savedAvatar.isNullOrEmpty() -> Uri.parse(savedAvatar)
                userProfile != null && !userProfile.imagesUrl.isNullOrEmpty() -> Uri.parse(
                    userProfile.imagesUrl
                )

                else -> null
            }


            _uiState.value = AccountUiState(
                imageUrl = imageUrl,
                nickname = nickname,
                profile = userProfile
            )

            if (userProfile != null) {
                if (savedNickname.isNullOrEmpty()) AccountPrefs.saveNickname(
                    context,
                    nickname,
                )
                if (savedAvatar.isNullOrEmpty()) AccountPrefs.saveAvatar(
                    context,
                    imageUrl.toString(),
                )
            }
        }
    }


        fun setNickName(nick: String) {
            _uiState.value = _uiState.value.copy(nickname = nick)
        }

        fun setAvatar(uri: Uri) {
            _uiState.value = _uiState.value.copy(imageUrl = uri)
        }

        fun saveProfile(context: Context) {
            _uiState.value.nickname?.let { AccountPrefs.saveNickname(context, it) }
            AccountPrefs.saveAvatar(context, _uiState.value.imageUrl.toString())
        }

    fun resetProfile(){
        viewModelScope.launch {
        val userProfile = userUseCase.getUserProfile()
        val newUiState = AccountUiState(
            imageUrl = userProfile.imagesUrl.let { Uri.parse(it) },
            nickname = userProfile.display_name,
            profile = userProfile
        )
            _uiState.value = newUiState
            saveProfile(context)
    }
    }

}
