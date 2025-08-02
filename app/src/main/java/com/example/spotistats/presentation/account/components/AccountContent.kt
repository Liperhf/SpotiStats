package com.example.spotistats.presentation.account.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.spotistats.R
import com.example.spotistats.presentation.account.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountContent(
    paddingValues: PaddingValues,
    pickImageLauncher: ManagedActivityResultLauncher<String, Uri?>,
    imageUri: Uri?,
    nickname: String?,
    context: Context,
    onSetNickNameClick:(String) -> Unit,
    onSaveProfileClick:(Context) -> Unit,
    onResetProfileClick:() -> Unit
)
{
    Column(modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {


        Box(
            modifier = Modifier
                .size(170.dp)
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        ) {
            AsyncImage(
                model = imageUri,
                contentDescription = stringResource(R.string.avatar),
                modifier = Modifier
                    .size(170.dp)
                    .clip(CircleShape)
                    .clickable {
                        pickImageLauncher.launch("image/*")
                    }
            )
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.BottomEnd)
            ){
                Icon(imageVector = Icons.Default.Create,
                    contentDescription = stringResource(R.string.change),
                    modifier = Modifier.clickable {
                        pickImageLauncher.launch("image/*")
                    },
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        nickname.let { it ->
            if (it != null) {
                TextField(value = it,
                    onValueChange = {
                        onSetNickNameClick(it)
                    },
                    label = { Text(stringResource(R.string.name)) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        cursorColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {onSaveProfileClick(context)},
            modifier = Modifier
                .width(250.dp)
                .height(50.dp)) {
            Text(
                stringResource(R.string.save_changes),
                color = MaterialTheme.colorScheme.onBackground)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {onResetProfileClick() },
            modifier = Modifier
                .width(250.dp)
                .height(50.dp)
        ){
            Text(
                stringResource(R.string.reset_changes),
                color = MaterialTheme.colorScheme.onBackground)
        }
    }
}