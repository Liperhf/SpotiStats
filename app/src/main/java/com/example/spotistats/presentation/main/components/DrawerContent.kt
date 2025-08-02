package com.example.spotistats.presentation.main.components

import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotistats.R

@Composable
fun DrawerContent(
    currentlyUserAvatar: Uri?,
    currentlyUserName: String?,
    onSettingsClick:() -> Unit,
    onLogoutClick:() -> Unit,
){
    ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = currentlyUserAvatar,
                contentDescription = stringResource(R.string.avatar),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(45.dp)
                    .clip(CircleShape)
            )
            currentlyUserName.let {
                if (it != null) {
                    Text(
                        it,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.primary)
        DrawerItem(text = stringResource(R.string.settings), onClick = {
            onSettingsClick()
        }, icon = Icons.Default.Settings)
        DrawerItem(
            text = stringResource(R.string.logout),
            onClick = { onLogoutClick() },
            icon = Icons.Default.Delete
        )
    }
}
