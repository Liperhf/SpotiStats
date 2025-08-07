package com.example.spotistats.viewModels

import android.content.Intent
import android.net.Uri
import com.example.spotistats.domain.model.AuthToken
import com.example.spotistats.domain.useCases.AuthUseCase
import com.example.spotistats.presentation.auth.AuthViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals


class AuthViewModelTest {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var authUseCase: AuthUseCase
    private lateinit var intent: Intent
    private lateinit var uri: Uri


    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    @Before
    fun setup() {
        authUseCase = mockk()
        authViewModel = AuthViewModel(authUseCase)
        intent = mockk()
        uri = mockk()
    }

    @Test
    fun `markCallbackHandled should update uiState`() = runTest {
        authViewModel.markCallbackHandled()
        val state = authViewModel.uiState.value
        assertEquals(true, state.callbackHandled)
    }

    @Test
    fun `onLoginClicked should update uiState`() = runTest {
        every { authUseCase.createAuthIntent() } returns intent
        authViewModel.onLoginClicked()
        val state = authViewModel.uiState.value
        assertEquals(intent, state.authIntent)
    }

    @Test
    fun `checkAuthStatus should update uiState from useCase when success`() = runTest {
        coEvery { authUseCase.isUserAuthorized() } returns true
        authViewModel.checkAuthStatus()
        val state = authViewModel.uiState.value
        assertEquals(true, state.isAuthenticated)
    }

    @Test
    fun `processSpotifyCallback with code should call processAuthorizationCode`() = runTest() {
        every { intent.data } returns uri
        every { uri.scheme } returns "spotistats"
        every { uri.host } returns "callback"
        every { uri.getQueryParameter("code") } returns "code"
        every { uri.getQueryParameter("error") } returns "error"
        coEvery { authViewModel.processAuthorizationCode("code") } just Runs
        authViewModel.processSpotifyCallback(intent)
        coVerify { authViewModel.processAuthorizationCode("code") }
    }

    @Test
    fun `processSpotifyCallback without code should update uiState`() = runTest {
        every { intent.data } returns uri
        every { uri.scheme } returns "spotistats"
        every { uri.host } returns "callback"
        every { uri.getQueryParameter("code") } returns null
        every { uri.getQueryParameter("error") } returns "error"
        authViewModel.processSpotifyCallback(intent)
        val state = authViewModel.uiState.value
        assertEquals("error", state.errorMessage)
    }

    @Test
    fun `processAuthorizationCode should call useCase and update uiState`() = runTest {
        val fakeCode = "code"
        val fakeAuthToken = AuthToken(
            "token",
            token_type = "type",
            expires_in = 123,
            refresh_token = "ref_token",
            scope = "scope"
        )
        coEvery { authUseCase.exchangeCodeForToken(fakeCode) } returns fakeAuthToken
        coEvery { authUseCase.saveTokens(fakeAuthToken) } just Runs
        every { authViewModel.checkAuthStatus() } just Runs
        authViewModel.processAuthorizationCode(fakeCode)
        coVerify { authUseCase.exchangeCodeForToken(fakeCode) }
        coVerify { authUseCase.saveTokens(fakeAuthToken) }
        coVerify { authViewModel.checkAuthStatus()}

    }

    @Test
    fun `processAuthorizationCode with exception should update uiState`() = runTest {
        val fakeCode = "code"
        val fakeException = Exception("error")
        coEvery { authUseCase.exchangeCodeForToken(fakeCode) } throws fakeException
        authViewModel.processAuthorizationCode(fakeCode)
        val state = authViewModel.uiState.value
        assertEquals("error",state.errorMessage)
    }

    @Test
    fun `logout without exception should update uiState`() = runTest{
        coEvery { authUseCase.clearTokens() } just Runs
        authViewModel.logout()
        val state = authViewModel.uiState.value
        assertEquals(false,state.isAuthenticated)
    }

    @Test
    fun `logout with exception should update uiState`() = runTest{
        coEvery { authUseCase.clearTokens() } throws Exception("error")
        authViewModel.logout()
        val state = authViewModel.uiState.value
        assertEquals("error",state.errorMessage)
    }
}