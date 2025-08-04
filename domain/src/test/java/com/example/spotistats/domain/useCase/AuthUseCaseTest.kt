package com.example.spotistats.domain.useCase

import com.example.spotistats.domain.model.AuthToken
import com.example.spotistats.domain.repository.AuthRepository
import com.example.spotistats.domain.useCases.AuthUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

class AuthUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var authUseCase: AuthUseCase

    @Before
    fun setup(){
        authRepository = mockk()
        authUseCase = AuthUseCase(authRepository)
    }


    @Test
    fun `refreshTokenIfNeeded should return true if token is not expired`() = runTest {
        coEvery { authUseCase.isTokenExpired() } returns false
        val result = authUseCase.refreshTokenIfNeeded()
        assertEquals(true,result)
        coVerify (exactly = 0){authRepository.refreshToken(any())}
    }

    @Test
    fun `refreshTokenIfNeeded should return false if token is expired`() = runTest {
        val fakeToken = AuthToken(
            access_token = "access_token",
            token_type = "token_type",
            expires_in = 123,
            refresh_token = "refresh_token",
            scope = "scope"
        )

        coEvery { authUseCase.isTokenExpired() } returns true
        coEvery {  authUseCase.getRefreshToken()  } returns "refresh_token"
        coEvery { authRepository.refreshToken("refresh_token") } returns fakeToken
        coEvery { authUseCase.saveTokens(fakeToken) } returns Unit
        val result = authUseCase.refreshTokenIfNeeded()
        assertEquals(true,result)
        coVerify (exactly = 1){authUseCase.getRefreshToken()}
        coVerify (exactly = 1){authRepository.refreshToken("refresh_token")}
        coVerify (exactly = 1){authRepository.saveTokens(fakeToken)}}


    @Test
    fun `refreshTokenIfNeeded should return false when token expired and no refresh token`() = runTest{

    coEvery { authUseCase.isTokenExpired() } returns true
        coEvery { authUseCase.getRefreshToken() } returns null
        val result = authUseCase.refreshTokenIfNeeded()
        coVerify(exactly = 1) { authUseCase.getRefreshToken() }
        coVerify(exactly = 0) { authRepository.refreshToken(any()) }
        coVerify(exactly = 0) { authUseCase.saveTokens(any()) }
        assertEquals(false,result)
    }


    @Test
    fun `isUserAuthorized should return false when token is null`() = runTest {
        coEvery { authRepository.getAccessToken() } returns null
        val result = authUseCase.isUserAuthorized()
        coVerify(exactly = 1) { authRepository.getAccessToken() }
        assertEquals(false,result)
    }

    @Test
    fun `isUserAuthorized should return true when token is not null and not expired`() = runTest {
    coEvery { authRepository.getAccessToken() } returns "access_token"
        coEvery { authRepository.isTokenExpired() } returns false
        val result = authUseCase.isUserAuthorized()
        coVerify(exactly = 1) { authRepository.getAccessToken() }
        assertEquals(true,result)
    }

    @Test
    fun `isUserAuthorized should return true when token is not null,expired and refreshTokenIfNeeded is true`() = runTest {
        val fakeToken = AuthToken(
            access_token = "access_token",
            token_type = "token_type",
            expires_in = 123,
            refresh_token = "refresh_token",
            scope = "scope"
        )
        coEvery { authRepository.getAccessToken() } returns "access_token"
        coEvery { authRepository.isTokenExpired() } returns true
        coEvery { authRepository.getRefreshToken() } returns "refresh_token"
        coEvery { authRepository.refreshToken("refresh_token") } returns fakeToken
        coEvery { authRepository.saveTokens(fakeToken) } returns Unit
        val result = authUseCase.isUserAuthorized()
        coVerify(exactly = 1) { authRepository.getAccessToken() }
        coVerify(exactly = 1) { authRepository.getRefreshToken() }
        coVerify(exactly = 1) { authRepository.refreshToken("refresh_token") }
        coVerify(exactly = 1){authRepository.saveTokens(fakeToken)}
        assertEquals(true,result)
    }

    @Test
    fun `isUserAuthorized should return false when token is not null,expired and refreshTokenIfNeeded is false`() = runTest {
        coEvery { authRepository.getAccessToken() } returns "access_token"
        coEvery { authRepository.isTokenExpired() } returns true
        coEvery { authRepository.getRefreshToken() } returns null
        val result = authUseCase.isUserAuthorized()
        coVerify(exactly = 1) { authRepository.getAccessToken() }
        coVerify(exactly = 1) { authRepository.getRefreshToken() }
        assertEquals(false,result)
    }

    @Test
    fun `isUserAuthorized should return false when refreshToken throws exception`() = runTest {
        coEvery { authRepository.getAccessToken() } returns "access_token"
        coEvery { authRepository.isTokenExpired() } returns true
        coEvery { authRepository.getRefreshToken() } returns "refresh_token"
        coEvery { authRepository.refreshToken("refresh_token") } throws RuntimeException("Network error")
        val result = authUseCase.isUserAuthorized()
        coVerify(exactly = 1) { authRepository.getAccessToken() }
        coVerify(exactly = 1) { authRepository.getRefreshToken() }
        coVerify(exactly = 1) { authRepository.refreshToken("refresh_token") }
        assertEquals(false, result)
    }
    }


