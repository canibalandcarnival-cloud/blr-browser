package com.example.data

import java.util.UUID

enum class ThemeMode(val label: String) {
  SYSTEM("System"),
  LIGHT("Light"),
  DARK("Dark")
}

enum class WallpaperPreset(val label: String, val drawableResName: String) {
  FOREST("Pine Forest", "wallpaper_forest"),
  DARK("Obsidian", "wallpaper_dark"),
  SUNSET("Sunset Dusk", "wallpaper_sunset"),
  MINT_FROST("Mint Glass", "")
}

enum class ActiveSegment {
  START_PAGE,
  BOOKMARKS,
  READING_LIST,
  HISTORY
}

data class BrowserTab(
  val id: String = UUID.randomUUID().toString(),
  val title: String = "Start Page",
  val url: String = "",
  val faviconUrl: String? = null,
  val isLoading: Boolean = false,
  val progress: Float = 0f,
  val canGoBack: Boolean = false,
  val canGoForward: Boolean = false,
  val isPrivate: Boolean = false,
  val previewColor: Long = 0xFF143D28
) {
  val isStartPage: Boolean get() = url.isEmpty() || url == "about:blank"
}

data class FavoriteItem(
  val id: String = UUID.randomUUID().toString(),
  val title: String,
  val url: String,
  val iconType: String, // "rings", "cc_gradient", "serif_s", "monogram_m", "droplet", "pinterest", "monogram_k", "era_ceramic", "generic"
  val bgColor: Long = 0xFF2A2A2A,
  val fgColor: Long = 0xFFFFFFFF,
  val badgeText: String? = null
)

data class ReadingListItem(
  val id: String = UUID.randomUUID().toString(),
  val title: String,
  val excerpt: String,
  val domain: String,
  val url: String,
  val thumbnailRes: Int,
  val timestamp: Long = System.currentTimeMillis(),
  val isRead: Boolean = false
)

data class HistoryItem(
  val id: String = UUID.randomUUID().toString(),
  val title: String,
  val url: String,
  val timestamp: Long = System.currentTimeMillis()
)

data class BookmarkItem(
  val id: String = UUID.randomUUID().toString(),
  val title: String,
  val url: String,
  val folder: String = "Favorites",
  val timestamp: Long = System.currentTimeMillis()
)
