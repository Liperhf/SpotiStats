package com.example.spotistats.presentation.language.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.spotistats.ui.theme.SpotiStatsTheme

@Composable
fun LanguageListItem(title: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(title, fontSize = 18.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            headlineColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Preview
@Composable
private fun LanguageListItemPreview() {
    SpotiStatsTheme {
        LanguageListItem(title = "English", onClick = {})
    }
}