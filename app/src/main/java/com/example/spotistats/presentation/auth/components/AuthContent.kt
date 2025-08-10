package com.example.spotistats.presentation.auth.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotistats.R

@Composable
fun AuthContent(
    onLoginClicked:() -> Unit
    ){
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center){
        Column(modifier = Modifier) {
            Text(stringResource(R.string.welcome),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 7.dp))
            Spacer(modifier = Modifier.height(3.dp))
            Text(stringResource(R.string.welcom_text),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 24.dp))
            Spacer(modifier = Modifier.height(14.dp))
            Button(onClick = {
                onLoginClicked()
            },
                colors = ButtonColors(containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = MaterialTheme.colorScheme.background),
                modifier = Modifier
                    .width(250.dp)
                    .height(50.dp)
            )
            {
                Row(modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(top = 4.dp)) {
                    Text(stringResource(R.string.login),fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    AsyncImage(model = R.drawable.spotifyblack,
                        contentDescription = stringResource(R.string.spotify),
                        modifier = Modifier
                            .size(26.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(stringResource(R.string.spotify),fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

            }
        }
    }
}