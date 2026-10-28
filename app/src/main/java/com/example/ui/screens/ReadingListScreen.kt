package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ReadingListItem
import com.example.ui.components.ReadingGlassesIcon
import com.example.ui.components.bouncyClick
import com.example.ui.components.glassEffect
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.EmeraldPrimary

@Composable
fun ReadingListScreen(
  readingList: List<ReadingListItem>,
  isDark: Boolean,
  onNavigateUrl: (String) -> Unit,
  onToggleRead: (String) -> Unit,
  onDeleteReadingItem: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showUnreadOnly by remember { mutableStateOf(false) }

  val headerTextColor = if (isDark) Color.White else Color(0xFF131A15)
  val subtextColor = if (isDark) Color.White.copy(alpha = 0.65f) else Color(0xFF233027).copy(alpha = 0.65f)

  val displayedList = if (showUnreadOnly) readingList.filter { !it.isRead } else readingList

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
        text = "Reading List",
        color = headerTextColor,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.testTag("reading_list_screen_title")
      )

      // Unread filter pill
      Box(
        modifier = Modifier
          .glassEffect(isDark, RoundedCornerShape(20.dp), elevation = 4.dp)
          .bouncyClick { showUnreadOnly = !showUnreadOnly }
          .padding(horizontal = 12.dp, vertical = 7.dp)
          .testTag("toggle_unread_filter"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = if (showUnreadOnly) "Showing Unread" else "Showing All",
          color = if (showUnreadOnly) (if (isDark) EmeraldAccent else EmeraldPrimary) else headerTextColor,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (displayedList.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          ReadingGlassesIcon(
            tint = subtextColor,
            modifier = Modifier.size(56.dp)
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Your reading list is empty",
            color = subtextColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(displayedList, key = { it.id }) { item ->
          val cardShape = RoundedCornerShape(22.dp)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .glassEffect(isDark, shape = cardShape, elevation = 5.dp)
              .clickable { onNavigateUrl(item.url) }
              .padding(14.dp)
              .testTag("reading_item_${item.id}")
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Image(
                painter = painterResource(id = item.thumbnailRes),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .size(68.dp)
                  .clip(RoundedCornerShape(14.dp))
                  .border(
                    0.5.dp,
                    Color.White.copy(alpha = if (isDark) 0.15f else 0.4f),
                    RoundedCornerShape(14.dp)
                  )
              )

              Spacer(modifier = Modifier.width(14.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = item.title,
                  color = headerTextColor,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                  text = item.excerpt,
                  color = subtextColor,
                  fontSize = 12.5.sp,
                  maxLines = 2,
                  overflow = TextOverflow.Ellipsis,
                  lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = item.domain,
                    color = subtextColor.copy(alpha = 0.8f),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                  )
                  if (item.isRead) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = "• Read",
                      color = if (isDark) EmeraldAccent else EmeraldPrimary,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Medium
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.width(6.dp))

              // Actions: Toggle Read / Delete
              IconButton(
                onClick = { onToggleRead(item.id) },
                modifier = Modifier.testTag("toggle_read_${item.id}")
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Mark Read",
                  tint = if (item.isRead) (if (isDark) EmeraldAccent else EmeraldPrimary) else subtextColor.copy(alpha = 0.4f),
                  modifier = Modifier.size(18.dp)
                )
              }

              IconButton(
                onClick = { onDeleteReadingItem(item.id) },
                modifier = Modifier.testTag("delete_reading_${item.id}")
              ) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Remove",
                  tint = subtextColor.copy(alpha = 0.5f),
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
