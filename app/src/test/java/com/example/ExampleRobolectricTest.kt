package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ActiveSegment
import com.example.data.ThemeMode
import com.example.viewmodel.BrowserViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Flow", appName)
  }

  @Test
  fun `verify browser initial state and favorites`() {
    val viewModel = BrowserViewModel()
    val state = viewModel.uiState.value

    assertEquals(1, state.tabs.size)
    assertTrue(state.activeTab.isStartPage)
    assertEquals(ActiveSegment.START_PAGE, state.currentSegment)
    assertTrue(state.favorites.isNotEmpty())
    assertTrue(state.readingList.isNotEmpty())
  }

  @Test
  fun `verify navigation url format and history`() {
    val viewModel = BrowserViewModel()
    viewModel.navigateUrl("surfacemag.com")

    val state = viewModel.uiState.value
    assertEquals("https://surfacemag.com", state.activeTab.url)
    assertFalse(state.activeTab.isStartPage)

    viewModel.onPageFinished("Surface Magazine", "https://surfacemag.com")
    val updatedState = viewModel.uiState.value
    assertTrue(updatedState.history.any { it.url == "https://surfacemag.com" })
  }

  @Test
  fun `verify theme toggling between light and dark`() {
    val viewModel = BrowserViewModel()
    val initialTheme = viewModel.uiState.value.themeMode
    assertEquals(ThemeMode.DARK, initialTheme)

    viewModel.toggleTheme()
    assertEquals(ThemeMode.LIGHT, viewModel.uiState.value.themeMode)

    viewModel.toggleTheme()
    assertEquals(ThemeMode.DARK, viewModel.uiState.value.themeMode)
  }

  @Test
  fun `verify tabs management`() {
    val viewModel = BrowserViewModel()
    assertEquals(1, viewModel.uiState.value.tabs.size)

    viewModel.openNewTab()
    assertEquals(2, viewModel.uiState.value.tabs.size)

    val currentActiveId = viewModel.uiState.value.activeTabId
    viewModel.closeTab(currentActiveId)
    assertEquals(1, viewModel.uiState.value.tabs.size)
  }
}
