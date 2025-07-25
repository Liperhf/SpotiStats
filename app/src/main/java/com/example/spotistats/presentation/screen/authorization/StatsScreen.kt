package com.example.spotistats.presentation.screen.authorization

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.spotistats.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatsScreen(navController: NavController,
                viewModel:StatsViewModel){
    Scaffold() { paddingValues ->
        Text(text = stringResource(R.string.stats),
            modifier = Modifier.padding(paddingValues))
    }
    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }
    Scaffold(
        
    ) {paddingValues ->  
    }
}