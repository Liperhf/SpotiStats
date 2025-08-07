package com.example.spotistats.viewModels

import android.content.Context
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.useCases.UserUseCase
import com.example.spotistats.presentation.account.AccountViewModel
import com.example.spotistats.util.AccountPrefs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import android.net.Uri
import com.example.spotistats.util.MainCoroutineRule
import io.mockk.Runs
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AccountViewModelTest {
    private lateinit var viewModel: AccountViewModel
    private lateinit var userUseCase: UserUseCase
    private lateinit var context: Context

    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    @Before
    fun setup(){
        mockkStatic(Uri::class)
        userUseCase = mockk()
        context = mockk(relaxed = true)
        mockkObject(AccountPrefs)
        viewModel = AccountViewModel(context, userUseCase)
    }

    @After
    fun tearDown() {
        unmockkObject(AccountPrefs)
        unmockkStatic(Uri::class)
    }


    @Test
    fun `loadProfile should load profile from pref when preferences is not empty`() = runTest() {
        val mockUri:Uri = mockk()
        every { AccountPrefs.getNickname(context) } returns "TestUser"
        every { AccountPrefs.getAvatar(context) } returns "https://example.com/avatar.jpg"
        every { Uri.parse("https://example.com/avatar.jpg") } returns mockUri
        every { mockUri.toString() } returns "https://example.com/avatar.jpg"

        viewModel.loadProfile()

        val state = viewModel.uiState.value
        println("DEBUG: Final state: $state")
        assertEquals("TestUser", state.nickname)
        assertEquals("https://example.com/avatar.jpg", state.imageUrl.toString())
    }

    @Test
    fun `loadProfile should load profile from API when pref is empty`() = runTest {
        val mockUri:Uri = mockk()
        val fakeUserProfile = UserProfile(
            display_name = "name",
            imagesUrl = "image"
        )
        every { AccountPrefs.getNickname(context) } returns null
        every { AccountPrefs.getAvatar(context) } returns null
        coEvery { userUseCase.getUserProfile() } returns fakeUserProfile
        every {Uri.parse("image")  } returns mockUri
        every { mockUri.toString() } returns "image"
        every { AccountPrefs.saveNickname(context,"name") } just Runs
        every { AccountPrefs.saveAvatar(context,"image") } just Runs
        viewModel.loadProfile()
        val state = viewModel.uiState.value
        assertEquals("name",state.nickname)
        assertEquals("image",state.imageUrl.toString())
    }

    @Test
    fun `setNickname should update uiState`() = runTest {
        viewModel.setNickName("name")
        val state = viewModel.uiState.value
        assertEquals("name",state.nickname)
    }

    @Test
    fun `setAvatar should update uiState`() = runTest{
        val mockUri:Uri = mockk()
        every { Uri.parse("image")  } returns mockUri
        every { mockUri.toString() } returns "image"
        viewModel.setAvatar(mockUri)
        val state = viewModel.uiState.value
        assertEquals("image",state.imageUrl.toString())
    }

    @Test
    fun `resetUserProfile should update uiState from rep`() = runTest{
        val mockUri:Uri = mockk()
        val fakeProfile = UserProfile(
            display_name = "name",
            imagesUrl = "image"
        )
        coEvery { userUseCase.getUserProfile() } returns fakeProfile
        every { Uri.parse("image")  } returns mockUri
        every { mockUri.toString() } returns "image"

        viewModel.resetProfile()
        val stats = viewModel.uiState.value
        assertEquals("name",stats.nickname)
        assertEquals("image",stats.imageUrl.toString())
    }
}