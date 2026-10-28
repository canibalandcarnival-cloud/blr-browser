package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ActiveSegment
import com.example.data.ThemeMode
import com.example.data.WallpaperPreset
import com.example.ui.components.BottomAddressBar
import com.example.ui.components.SearchDialog
import com.example.ui.components.TabSwitcherSheet
import com.example.ui.components.TopSegmentedBar
import com.example.ui.screens.*
import com.example.ui.theme.FlowBrowserTheme
import com.example.viewmodel.BrowserViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val browserViewModel: BrowserViewModel = viewModel()
      val uiState by browserViewModel.uiState.collectAsStateWithLifecycle()

      val systemDark = isSystemInDarkTheme()
      val isDark = when (uiState.themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> systemDark
      }

      FlowBrowserTheme(darkTheme = isDark) {
        BrowserApp(
          viewModel = browserViewModel,
          isDark = isDark
        )
      }
    }
  }
}

@Composable
fun BrowserApp(
  viewModel: BrowserViewModel,
  isDark: Boolean
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val activeTab = uiState.activeTab
  val isStartPage = activeTab.isStartPage

  Box(
    modifier = Modifier
      .fillMaxSize()
      .testTag("browser_app_root")
  ) {
    // 1. Wallpaper / Background Layer
    if (isStartPage) {
      WallpaperBackground(
        preset = uiState.wallpaperPreset,
        isDark = isDark
      )
    }

    // 2. Main Content Layer
    Box(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
      if (isStartPage) {
        Column(modifier = Modifier.fillMaxSize()) {
          // Top Segmented Pill Bar (as in screenshot)
          TopSegmentedBar(
            currentSegment = uiState.currentSegment,
            onSegmentSelected = { viewModel.setSegment(it) },
            isDark = isDark,
            modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
          )

          // Segment Views with smooth crossfade
          AnimatedContent(
            targetState = uiState.currentSegment,
            transitionSpec = {
              fadeIn(animationSpec = spring(stiffness = 400f)) togetherWith
                  fadeOut(animationSpec = spring(stiffness = 400f))
            },
            label = "segment_crossfade",
            modifier = Modifier.weight(1f)
          ) { segment ->
            when (segment) {
              ActiveSegment.START_PAGE -> {
                StartPageScreen(
                  favorites = uiState.favorites,
                  readingList = uiState.readingList,
                  themeMode = uiState.themeMode,
                  wallpaperPreset = uiState.wallpaperPreset,
                  isDark = isDark,
                  onNavigateUrl = { viewModel.navigateUrl(it) },
                  onToggleTheme = { viewModel.toggleTheme() },
                  onSelectWallpaper = { viewModel.setWallpaper(it) },
                  onAddFavorite = { title, url -> viewModel.addFavorite(title, url) },
                  onShowAllFavorites = { viewModel.setSegment(ActiveSegment.BOOKMARKS) },
                  onShowAllReadingList = { viewModel.setSegment(ActiveSegment.READING_LIST) }
                )
              }
              ActiveSegment.BOOKMARKS -> {
                BookmarksScreen(
                  bookmarks = uiState.bookmarks,
                  isDark = isDark,
                  onNavigateUrl = { viewModel.navigateUrl(it) },
                  onDeleteBookmark = { viewModel.deleteBookmark(it) },
                  onAddBookmark = { title, url, folder -> viewModel.addBookmark(title, url, folder) }
                )
              }
              ActiveSegment.READING_LIST -> {
                ReadingListScreen(
                  readingList = uiState.readingList,
                  isDark = isDark,
                  onNavigateUrl = { viewModel.navigateUrl(it) },
                  onToggleRead = { viewModel.toggleReadingItemRead(it) },
                  onDeleteReadingItem = { viewModel.deleteReadingItem(it) }
                )
              }
              ActiveSegment.HISTORY -> {
                HistoryScreen(
                  history = uiState.history,
                  isDark = isDark,
                  onNavigateUrl = { viewModel.navigateUrl(it) },
                  onClearHistory = { viewModel.clearHistory() },
                  onDeleteHistoryItem = { viewModel.deleteHistoryItem(it) }
                )
              }
            }
          }
        }
      } else {
        // Active Full Web Browser Screen
        WebBrowserScreen(
          currentTab = activeTab,
          onUpdateTabState = { tabId, title, url, isLoading, progress, canGoBack, canGoForward ->
            viewModel.updateTabState(tabId, title, url, isLoading, progress, canGoBack, canGoForward)
          },
          onPageFinishedNavigation = { title, url ->
            viewModel.onPageFinished(title, url)
          },
          onScrollDelta = { delta ->
            viewModel.onScrollDelta(delta)
          },
          onCloseToHome = {
            viewModel.closeToStartPage()
          }
        )
      }
    }

    // 3. Floating Bottom Dock (Address Bar + Navigation + Tabs)
    val bottomOffset by animateDpAsState(
      targetValue = if (uiState.isBottomBarCollapsed && !isStartPage) 90.dp else 0.dp,
      animationSpec = spring(stiffness = 350f),
      label = "bottom_bar_offset"
    )

    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .offset(y = bottomOffset)
        .navigationBarsPadding()
    ) {
      BottomAddressBar(
        currentTab = activeTab,
        tabCount = uiState.tabs.size,
        isDark = isDark,
        onBackClick = {
          if (!isStartPage) {
            viewModel.closeToStartPage()
          }
        },
        onForwardClick = { /* Handled via webview navigation */ },
        onAddressClick = { viewModel.setSearchDialogOpen(true) },
        onRefreshClick = {
          if (!isStartPage) {
            viewModel.navigateUrl(activeTab.url)
          }
        },
        onTabsClick = { viewModel.setTabSwitcherOpen(true) }
      )
    }

    // 4. Overlays & Dialogs
    if (uiState.isSearchDialogOpen) {
      SearchDialog(
        initialQuery = if (activeTab.isStartPage) "" else activeTab.url,
        isDark = isDark,
        onDismiss = { viewModel.setSearchDialogOpen(false) },
        onSearch = { query -> viewModel.navigateUrl(query) }
      )
    }

    if (uiState.isTabSwitcherOpen) {
      TabSwitcherSheet(
        tabs = uiState.tabs,
        activeTabId = uiState.activeTabId,
        isDark = isDark,
        onTabSelected = { tabId -> viewModel.selectTab(tabId) },
        onCloseTab = { tabId -> viewModel.closeTab(tabId) },
        onNewTab = { isPrivate -> viewModel.openNewTab(isPrivate) },
        onDismiss = { viewModel.setTabSwitcherOpen(false) }
      )
    }
  }
}

@Composable
fun WallpaperBackground(
  preset: WallpaperPreset,
  isDark: Boolean,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Box(modifier = modifier.fillMaxSize()) {
    when (preset) {
      WallpaperPreset.FOREST -> {
        Image(
          painter = painterResource(id = R.drawable.wallpaper_forest),
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }
      WallpaperPreset.DARK -> {
        Image(
          painter = painterResource(id = R.drawable.wallpaper_dark),
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }
      WallpaperPreset.SUNSET -> {
        Image(
          painter = painterResource(id = R.drawable.wallpaper_sunset),
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }
      WallpaperPreset.MINT_FROST -> {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = if (isDark) {
                  listOf(Color(0xFF0F261B), Color(0xFF133624), Color(0xFF0B1B13))
                } else {
                  listOf(Color(0xFFE2F0E7), Color(0xFFC7E6D3), Color(0xFFD6EDE0))
                }
              )
            )
        )
      }
    }

    // Scrim overlay for light / dark balance
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          if (isDark) {
            Color.Black.copy(alpha = 0.20f)
          } else {
            Color.White.copy(alpha = 0.35f)
          }
        )
    )
  }
}
