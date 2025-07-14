package com.example.spotistats.util

import android.content.Context
import java.util.Locale

fun UpdateLocale(context: Context, locale:Locale){
    val resources = context.resources
    val config = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config,resources.displayMetrics)
}