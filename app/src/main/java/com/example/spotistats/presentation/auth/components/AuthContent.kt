package com.example.spotistats.presentation.auth.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.spotistats.R
import com.example.spotistats.presentation.auth.AuthViewModel

@Composable
fun AuthContent(
    viewModel: AuthViewModel
    ){
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center){
        Button(onClick = {
            viewModel.onLoginClicked()
        },
            colors = ButtonColors(containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.background,
                disabledContentColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor = MaterialTheme.colorScheme.background)
        )
        {
            Text(stringResource(R.string.login))
        }
    }
}