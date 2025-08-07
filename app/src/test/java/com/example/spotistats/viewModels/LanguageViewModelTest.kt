package com.example.spotistats.viewModels

import com.example.spotistats.domain.model.AppLanguage
import com.example.spotistats.presentation.language.LanguageViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class LanguageViewModelTest {
    private lateinit var languageViewModel: LanguageViewModel

    @Before
    fun setup(){
        languageViewModel = LanguageViewModel()
    }

    @Test
    fun `setLanguage should update language`() = runTest {
        val fakeLanguage = AppLanguage.ENGLISH
        languageViewModel.setLanguage(fakeLanguage)
        assertEquals(AppLanguage.ENGLISH,languageViewModel.language.value)
    }

}