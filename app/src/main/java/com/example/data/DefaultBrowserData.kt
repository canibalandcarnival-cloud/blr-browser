package com.example.data

import com.example.R

object DefaultBrowserData {

  fun getDefaultFavorites(): List<FavoriteItem> = listOf(
    FavoriteItem(
      id = "fav-1",
      title = "Curious and Wondrous T...",
      url = "https://en.wikipedia.org/wiki/Cabinet_of_curiosities",
      iconType = "rings",
      bgColor = 0xFF2D2319,
      fgColor = 0xFFE4BC84
    ),
    FavoriteItem(
      id = "fav-2",
      title = "Beti-Jai: an historical fr...",
      url = "https://en.wikipedia.org/wiki/Front%C3%B3n_Beti_Jai",
      iconType = "cc_gradient",
      bgColor = 0xFFFFCFC7,
      fgColor = 0xFF261D1A,
      badgeText = "CC/"
    ),
    FavoriteItem(
      id = "fav-3",
      title = "Five Design-Driven Port...",
      url = "https://www.surfacemag.com",
      iconType = "serif_s",
      bgColor = 0xFF0D0D0E,
      fgColor = 0xFFFFFFFF,
      badgeText = "S"
    ),
    FavoriteItem(
      id = "fav-4",
      title = "Mush Studios | Home Dec...",
      url = "https://mushstudios.co",
      iconType = "monogram_m",
      bgColor = 0xFF767A7E,
      fgColor = 0xFFFFFFFF,
      badgeText = "M"
    ),
    FavoriteItem(
      id = "fav-5",
      title = "Rit Dye – Fabric Dye...",
      url = "https://www.ritdye.com",
      iconType = "droplet",
      bgColor = 0xFFFCD7D3,
      fgColor = 0xFF1C1917
    ),
    FavoriteItem(
      id = "fav-6",
      title = "Pinterest",
      url = "https://www.pinterest.com",
      iconType = "pinterest",
      bgColor = 0xFFFFFFFF,
      fgColor = 0xFFE60023
    ),
    FavoriteItem(
      id = "fav-7",
      title = "KOBU | Creative St...",
      url = "https://kobu.agency",
      iconType = "monogram_k",
      bgColor = 0xFFCBD0D4,
      fgColor = 0xFFFFFFFF,
      badgeText = "K"
    ),
    FavoriteItem(
      id = "fav-8",
      title = "Process / Era Ceramics",
      url = "https://eraceramics.com",
      iconType = "era_ceramic",
      bgColor = 0xFF161719,
      fgColor = 0xFFFFFFFF
    )
  )

  fun getDefaultReadingList(): List<ReadingListItem> = listOf(
    ReadingListItem(
      id = "read-1",
      title = "Process / Era Ceramics",
      excerpt = "Ceramics and dinnerware for the modern home and studio...",
      domain = "eraceramics.com",
      url = "https://eraceramics.com",
      thumbnailRes = R.drawable.thumb_ceramics,
      timestamp = System.currentTimeMillis() - 3600_000 * 2
    ),
    ReadingListItem(
      id = "read-2",
      title = "Wallflower | San Francisco Floral Design Studio – wallflo...",
      excerpt = "San Francisco's favorite floral studio, weddings and bespoke floral installations...",
      domain = "wallflowersanfrancisco.com",
      url = "https://wallflowersanfrancisco.com",
      thumbnailRes = R.drawable.thumb_floral,
      timestamp = System.currentTimeMillis() - 3600_000 * 5
    ),
    ReadingListItem(
      id = "read-3",
      title = "Jean-Michel Othoniel Explains the Hypnotic Potential of Flow...",
      excerpt = "The French artist recasts the Brooklyn Botanic Garden with shimmering gold sculptures...",
      domain = "surfacemag.com",
      url = "https://www.surfacemag.com",
      thumbnailRes = R.drawable.thumb_surface,
      timestamp = System.currentTimeMillis() - 3600_000 * 24
    ),
    ReadingListItem(
      id = "read-4",
      title = "A Kinfolk Guide to Mindful Architecture",
      excerpt = "Spaces sculpted for quiet contemplation, acoustic calmness, and tactile warmth...",
      domain = "kinfolk.com",
      url = "https://kinfolk.com",
      thumbnailRes = R.drawable.thumb_arch,
      timestamp = System.currentTimeMillis() - 3600_000 * 48
    )
  )

  fun getDefaultBookmarks(): List<BookmarkItem> = listOf(
    BookmarkItem(
      id = "bm-1",
      title = "Surface Magazine - Design, Architecture, Art",
      url = "https://www.surfacemag.com",
      folder = "Design"
    ),
    BookmarkItem(
      id = "bm-2",
      title = "Era Ceramics - Handcrafted Tableware",
      url = "https://eraceramics.com",
      folder = "Design"
    ),
    BookmarkItem(
      id = "bm-3",
      title = "Wikipedia - The Free Encyclopedia",
      url = "https://en.wikipedia.org",
      folder = "Reference"
    ),
    BookmarkItem(
      id = "bm-4",
      title = "Pinterest - Visual Inspiration",
      url = "https://www.pinterest.com",
      folder = "Inspiration"
    )
  )

  fun getDefaultHistory(): List<HistoryItem> = listOf(
    HistoryItem(
      id = "hist-1",
      title = "Curious and Wondrous Things - Cabinet of curiosities",
      url = "https://en.wikipedia.org/wiki/Cabinet_of_curiosities",
      timestamp = System.currentTimeMillis() - 1000 * 60 * 15
    ),
    HistoryItem(
      id = "hist-2",
      title = "Beti-Jai Fronton - Madrid Architecture Heritage",
      url = "https://en.wikipedia.org/wiki/Front%C3%B3n_Beti_Jai",
      timestamp = System.currentTimeMillis() - 1000 * 60 * 50
    ),
    HistoryItem(
      id = "hist-3",
      title = "Surface Magazine | Five Design-Driven Portfolios",
      url = "https://www.surfacemag.com",
      timestamp = System.currentTimeMillis() - 1000 * 3600 * 3
    ),
    HistoryItem(
      id = "hist-4",
      title = "Era Ceramics - Process and Studio Journal",
      url = "https://eraceramics.com",
      timestamp = System.currentTimeMillis() - 1000 * 3600 * 8
    )
  )
}
