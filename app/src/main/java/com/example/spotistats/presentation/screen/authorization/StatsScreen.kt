package com.example.spotistats.presentation.screen.authorization

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.spotistats.R

@Composable
fun StatsScreen(navController: NavController){
    Scaffold() { paddingValues ->
        Text(text = stringResource(R.string.stats),
            modifier = Modifier.padding(paddingValues))
    }
}