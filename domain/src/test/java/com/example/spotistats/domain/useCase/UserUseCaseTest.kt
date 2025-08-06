package com.example.spotistats.domain.useCase

import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.repository.UserRepository
import com.example.spotistats.domain.useCases.UserUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UserUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var userUseCase: UserUseCase

    @Before
    fun setup(){
        userRepository = mockk()
        userUseCase = UserUseCase(userRepository)
    }


    @Test
    fun `getUserProfile should return userProfile when repository returns success`() = runTest {
        val fakeProfile = UserProfile(
            display_name = "name",
            imagesUrl = "image"
        )
        coEvery { userRepository.getUserProfile() } returns fakeProfile
        val result = userUseCase.getUserProfile()
        assertEquals(fakeProfile,result)
    }

    @Test
    fun `getUserProfile should return exception when repository returns error`() = runTest {
        coEvery { userRepository.getUserProfile() } throws RuntimeException("Network error")
        assertFailsWith<RuntimeException> {
            userUseCase.getUserProfile()

        }
    }

}