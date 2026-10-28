package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookmarkItem
import com.example.ui.components.bouncyClick
import com.example.ui.components.glassEffect
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.EmeraldPrimary

@Composable
fun BookmarksScreen(
  bookmarks: List<BookmarkItem>,
  isDark: Boolean,
  onNavigateUrl: (String) -> Unit,
  onDeleteBookmark: (String) -> Unit,
  onAddBookmark: (String, String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var showAddDialog by remember { mutableStateOf(false) }

  val headerTextColor = if (isDark) Color.White else Color(0xFF131A15)
  val subtextColor = if (isDark) Color.White.copy(alpha = 0.65f) else Color(0xFF233027).copy(alpha = 0.65f)

  val filteredBookmarks = bookmarks.filter {
    searchQuery.isEmpty() || it.title.contains(searchQuery, ignoreCase = true) || it.url.contains(searchQuery, ignoreCase = true)
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp)
      .padding(top = 16.dp, bottom = 96.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Bookmarks",
        color = headerTextColor,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.testTag("bookmarks_title")
      )

      Box(
        modifier = Modifier
          .glassEffect(isDark, RoundedCornerShape(20.dp), elevation = 4.dp)
          .bouncyClick { showAddDialog = true }
          .padding(horizontal = 12.dp, vertical = 7.dp)
          .testTag("add_bookmark_button"),
        contentAlignment = Alignment.Center
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Bookmark",
            tint = if (isDark) EmeraldAccent else EmeraldPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Add",
            color = headerTextColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Search bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      placeholder = { Text("Search bookmarks...", color = subtextColor) },
      leadingIcon = {
        Icon(Icons.Default.Search, contentDescription = null, tint = subtextColor)
      },
      singleLine = true,
      shape = RoundedCornerShape(20.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = if (isDark) Color(0x6618221C) else Color(0x99FFFFFF),
        unfocusedContainerColor = if (isDark) Color(0x4418221C) else Color(0x66FFFFFF),
        focusedBorderColor = if (isDark) EmeraldAccent else EmeraldPrimary,
        unfocusedBorderColor = Color.White.copy(alpha = if (isDark) 0.2f else 0.4f),
        focusedTextColor = headerTextColor,
        unfocusedTextColor = headerTextColor
      ),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("bookmarks_search_field")
    )

    Spacer(modifier = Modifier.height(14.dp))

    if (filteredBookmarks.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.MenuBook,
            contentDescription = null,
            tint = subtextColor,
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "No bookmarks found",
            color = subtextColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(filteredBookmarks, key = { it.id }) { item ->
          val cardShape = RoundedCornerShape(20.dp)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .glassEffect(isDark, shape = cardShape, elevation = 4.dp)
              .clickable { onNavigateUrl(item.url) }
              .padding(14.dp)
              .testTag("bookmark_item_${item.id}")
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(if (isDark) Color(0xFF1F2D24) else Color(0xFFE4EDE7)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Language,
                  contentDescription = null,
                  tint = if (isDark) EmeraldAccent else EmeraldPrimary,
                  modifier = Modifier.size(20.dp)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = item.title,
                  color = headerTextColor,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.SemiBold,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = null,
                    tint = subtextColor,
                    modifier = Modifier.size(12.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = item.folder,
                    color = subtextColor,
                    fontSize = 11.5.sp
                  )
                  Text(
                    text = " • ${item.url}",
                    color = subtextColor,
                    fontSize = 11.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }

              IconButton(
                onClick = { onDeleteBookmark(item.id) },
                modifier = Modifier.testTag("delete_bookmark_${item.id}")
              ) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Delete",
                  tint = subtextColor.copy(alpha = 0.6f),
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }
  }

  if (showAddDialog) {
    var title by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var folder by remember { mutableStateOf("Favorites") }

    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      title = { Text("New Bookmark") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("URL") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = folder,
            onValueChange = { folder = it },
            label = { Text("Folder") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank() && url.isNotBlank()) {
              onAddBookmark(title.trim(), url.trim(), folder.trim())
              showAddDialog = false
            }
          }
        ) {
          Text("Save")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
