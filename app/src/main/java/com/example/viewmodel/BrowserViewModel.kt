package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class BrowserUiState(
  val tabs: List<BrowserTab> = listOf(BrowserTab(id = "tab-default")),
  val activeTabId: String = "tab-default",
  val currentSegment: ActiveSegment = ActiveSegment.START_PAGE,
  val favorites: List<FavoriteItem> = DefaultBrowserData.getDefaultFavorites(),
  val readingList: List<ReadingListItem> = DefaultBrowserData.getDefaultReadingList(),
  val bookmarks: List<BookmarkItem> = DefaultBrowserData.getDefaultBookmarks(),
  val history: List<HistoryItem> = DefaultBrowserData.getDefaultHistory(),
  val themeMode: ThemeMode = ThemeMode.DARK, // Dark mode default matches screenshot
  val wallpaperPreset: WallpaperPreset = WallpaperPreset.FOREST, // Pine forest matches screenshot
  val isSearchDialogOpen: Boolean = false,
  val isTabSwitcherOpen: Boolean = false,
  val isBottomBarCollapsed: Boolean = false
) {
  val activeTab: BrowserTab
    get() = tabs.find { it.id == activeTabId } ?: tabs.firstOrNull() ?: BrowserTab()
}

class BrowserViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(BrowserUiState())
  val uiState: StateFlow<BrowserUiState> = _uiState.asStateFlow()

  fun navigateUrl(queryOrUrl: String) {
    val clean = queryOrUrl.trim()
    if (clean.isEmpty()) return

    val targetUrl = when {
      clean.startsWith("http://", ignoreCase = true) || clean.startsWith("https://", ignoreCase = true) -> clean
      clean.contains(".") && !clean.contains(" ") -> "https://$clean"
      else -> "https://www.google.com/search?q=" + java.net.URLEncoder.encode(clean, "UTF-8")
    }

    _uiState.update { state ->
      val updatedTabs = state.tabs.map { tab ->
        if (tab.id == state.activeTabId) {
          tab.copy(
            url = targetUrl,
            title = clean,
            isLoading = true,
            progress = 0.1f
          )
        } else tab
      }
      state.copy(
        tabs = updatedTabs,
        isSearchDialogOpen = false,
        isTabSwitcherOpen = false,
        isBottomBarCollapsed = false
      )
    }
  }

  fun closeToStartPage() {
    _uiState.update { state ->
      val updatedTabs = state.tabs.map { tab ->
        if (tab.id == state.activeTabId) {
          tab.copy(url = "", title = "Start Page", isLoading = false, progress = 0f)
        } else tab
      }
      state.copy(tabs = updatedTabs, currentSegment = ActiveSegment.START_PAGE)
    }
  }

  fun updateTabState(
    tabId: String,
    title: String,
    url: String,
    isLoading: Boolean,
    progress: Float,
    canGoBack: Boolean,
    canGoForward: Boolean
  ) {
    _uiState.update { state ->
      val updatedTabs = state.tabs.map { tab ->
        if (tab.id == tabId) {
          tab.copy(
            title = if (title.isNotBlank()) title else tab.title,
            url = url,
            isLoading = isLoading,
            progress = progress,
            canGoBack = canGoBack,
            canGoForward = canGoForward
          )
        } else tab
      }
      state.copy(tabs = updatedTabs)
    }
  }

  fun onPageFinished(title: String, url: String) {
    if (url.isNotBlank() && url != "about:blank") {
      val newItem = HistoryItem(
        title = title.ifBlank { url },
        url = url,
        timestamp = System.currentTimeMillis()
      )
      _uiState.update { state ->
        val existingFiltered = state.history.filterNot { it.url == url }
        state.copy(history = listOf(newItem) + existingFiltered)
      }
    }
  }

  fun openNewTab(isPrivate: Boolean = false) {
    val newId = UUID.randomUUID().toString()
    val newTab = BrowserTab(
      id = newId,
      title = "Start Page",
      url = "",
      isPrivate = isPrivate
    )
    _uiState.update { state ->
      state.copy(
        tabs = state.tabs + newTab,
        activeTabId = newId,
        currentSegment = ActiveSegment.START_PAGE,
        isTabSwitcherOpen = false
      )
    }
  }

  fun closeTab(tabId: String) {
    _uiState.update { state ->
      val remainingTabs = state.tabs.filterNot { it.id == tabId }
      if (remainingTabs.isEmpty()) {
        val freshId = UUID.randomUUID().toString()
        val freshTab = BrowserTab(id = freshId, title = "Start Page", url = "")
        state.copy(tabs = listOf(freshTab), activeTabId = freshId, currentSegment = ActiveSegment.START_PAGE)
      } else {
        val nextActiveId = if (state.activeTabId == tabId) {
          remainingTabs.last().id
        } else {
          state.activeTabId
        }
        state.copy(tabs = remainingTabs, activeTabId = nextActiveId)
      }
    }
  }

  fun selectTab(tabId: String) {
    _uiState.update { it.copy(activeTabId = tabId, isTabSwitcherOpen = false) }
  }

  fun setSegment(segment: ActiveSegment) {
    _uiState.update { it.copy(currentSegment = segment) }
  }

  fun toggleTheme() {
    _uiState.update { state ->
      val nextTheme = when (state.themeMode) {
        ThemeMode.DARK -> ThemeMode.LIGHT
        ThemeMode.LIGHT -> ThemeMode.DARK
        ThemeMode.SYSTEM -> ThemeMode.LIGHT
      }
      state.copy(themeMode = nextTheme)
    }
  }

  fun setWallpaper(preset: WallpaperPreset) {
    _uiState.update { it.copy(wallpaperPreset = preset) }
  }

  fun setSearchDialogOpen(open: Boolean) {
    _uiState.update { it.copy(isSearchDialogOpen = open) }
  }

  fun setTabSwitcherOpen(open: Boolean) {
    _uiState.update { it.copy(isTabSwitcherOpen = open) }
  }

  fun onScrollDelta(delta: Int) {
    if (delta > 20) {
      _uiState.update { it.copy(isBottomBarCollapsed = true) }
    } else if (delta < -20) {
      _uiState.update { it.copy(isBottomBarCollapsed = false) }
    }
  }

  fun addFavorite(title: String, url: String) {
    val cleanUrl = if (!url.startsWith("http")) "https://$url" else url
    val newFav = FavoriteItem(
      title = title,
      url = cleanUrl,
      iconType = "generic",
      badgeText = title.take(2).uppercase()
    )
    _uiState.update { it.copy(favorites = it.favorites + newFav) }
  }

  fun addBookmark(title: String, url: String, folder: String) {
    val cleanUrl = if (!url.startsWith("http")) "https://$url" else url
    val newBm = BookmarkItem(title = title, url = cleanUrl, folder = folder)
    _uiState.update { it.copy(bookmarks = listOf(newBm) + it.bookmarks) }
  }

  fun deleteBookmark(id: String) {
    _uiState.update { it.copy(bookmarks = it.bookmarks.filterNot { bm -> bm.id == id }) }
  }

  fun toggleReadingItemRead(id: String) {
    _uiState.update { state ->
      val updated = state.readingList.map { item ->
        if (item.id == id) item.copy(isRead = !item.isRead) else item
      }
      state.copy(readingList = updated)
    }
  }

  fun deleteReadingItem(id: String) {
    _uiState.update { it.copy(readingList = it.readingList.filterNot { item -> item.id == id }) }
  }

  fun clearHistory() {
    _uiState.update { it.copy(history = emptyList()) }
  }

  fun deleteHistoryItem(id: String) {
    _uiState.update { it.copy(history = it.history.filterNot { it.id == id }) }
  }
}
