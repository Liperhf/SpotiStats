package com.example.spotistats.presentation.language.components

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.spotistats.R
import com.example.spotistats.domain.model.AppLanguage
import com.example.spotistats.presentation.language.LanguageViewModel
import com.example.spotistats.util.LanguagePrefs
import com.example.spotistats.util.UpdateLocale
import java.util.Locale

@Composable
fun LanguageContent(paddingValues: PaddingValues,
                    context: Context,
                    onSetLanguageClick:(AppLanguage) -> Unit
){
    LazyColumn(modifier = Modifier.padding(paddingValues)){
        item{
            LanguageListItem(title = stringResource(R.string.english,), onClick = {
                LanguagePrefs.saveLanguage(context,"en")
                onSetLanguageClick(AppLanguage.ENGLISH)
                UpdateLocale(context, Locale("en"))
                (context as? Activity)?.recreate()
            })
        }
        item { LanguageListItem(title = stringResource(R.string.russian), onClick = {
            LanguagePrefs.saveLanguage(context,"ru")
            onSetLanguageClick(AppLanguage.RUSSIAN)
            UpdateLocale(context, Locale("ru"))
            (context as? Activity)?.recreate()
        })
        }
    }
}