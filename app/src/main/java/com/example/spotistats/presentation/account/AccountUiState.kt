package com.example.spotistats.presentation.account

import android.net.Uri
import com.example.spotistats.domain.model.UserProfile

data class AccountUiState (
    val imageUrl: Uri? = null,
    val nickname: String? = null,
    val profile: UserProfile? = null
)