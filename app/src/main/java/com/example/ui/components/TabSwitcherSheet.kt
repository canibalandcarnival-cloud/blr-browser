package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.BrowserTab
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.EmeraldPrimary

@Composable
fun TabSwitcherSheet(
  tabs: List<BrowserTab>,
  activeTabId: String,
  isDark: Boolean,
  onTabSelected: (String) -> Unit,
  onCloseTab: (String) -> Unit,
  onNewTab: (Boolean) -> Unit,
  onDismiss: () -> Unit
) {
  val primaryText = if (isDark) Color.White else Color(0xFF141916)
  val secondaryText = if (isDark) Color.White.copy(alpha = 0.6f) else Color(0xFF141916).copy(alpha = 0.6f)

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(if (isDark) Color(0xF2080D0A) else Color(0xF5EAF0EB))
        .statusBarsPadding()
        .navigationBarsPadding()
        .padding(16.dp)
        .testTag("tab_switcher_dialog")
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // Top action bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = { onNewTab(false) },
            modifier = Modifier
              .size(44.dp)
              .glassEffect(isDark, CircleShape, elevation = 4.dp)
              .testTag("new_tab_button")
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "New Tab",
              tint = if (isDark) EmeraldAccent else EmeraldPrimary
            )
          }

          Text(
            text = "${tabs.size} Tabs",
            color = primaryText,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )

          TextButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("done_tabs_button")
          ) {
            Text(
              text = "Done",
              color = if (isDark) EmeraldAccent else EmeraldPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid of tab preview cards
        LazyVerticalGrid(
          columns = GridCells.Fixed(2),
          modifier = Modifier.weight(1f),
          horizontalArrangement = Arrangement.spacedBy(14.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(tabs, key = { it.id }) { tab ->
            val isActive = tab.id == activeTabId
            val cardShape = RoundedCornerShape(20.dp)

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(cardShape)
                .background(if (isDark) Color(0xFF152019) else Color.White)
                .border(
                  width = if (isActive) 2.5.dp else 1.dp,
                  color = if (isActive) {
                    if (isDark) EmeraldAccent else EmeraldPrimary
                  } else {
                    if (isDark) Color.White.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.08f)
                  },
                  shape = cardShape
                )
                .clickable {
                  onTabSelected(tab.id)
                  onDismiss()
                }
                .testTag("tab_card_${tab.id}")
            ) {
              Column(modifier = Modifier.fillMaxSize()) {
                // Header of tab card
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = if (tab.isPrivate) Icons.Default.Lock else Icons.Default.Public,
                    contentDescription = null,
                    tint = if (isDark) EmeraldAccent else EmeraldPrimary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = if (tab.isStartPage) "Start Page" else tab.title.ifEmpty { tab.url },
                    color = primaryText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                  )
                  // Close tab button
                  Box(
                    modifier = Modifier
                      .size(24.dp)
                      .clip(CircleShape)
                      .background(if (isDark) Color(0x33FFFFFF) else Color(0x1A000000))
                      .clickable { onCloseTab(tab.id) }
                      .testTag("close_tab_${tab.id}"),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Close,
                      contentDescription = "Close Tab",
                      tint = secondaryText,
                      modifier = Modifier.size(14.dp)
                    )
                  }
                }

                // Tab preview body
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(tab.previewColor).copy(alpha = if (isDark) 0.35f else 0.15f))
                    .padding(12.dp),
                  contentAlignment = Alignment.Center
                ) {
                  if (tab.isStartPage) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                      Box(
                        modifier = Modifier
                          .size(36.dp)
                          .glassEffect(isDark, CircleShape, 2.dp),
                        contentAlignment = Alignment.Center
                      ) {
                        Text(
                          text = "🌿",
                          fontSize = 18.sp
                        )
                      }
                      Spacer(modifier = Modifier.height(6.dp))
                      Text(
                        text = "Flow Start",
                        color = secondaryText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                      )
                    }
                  } else {
                    Text(
                      text = tab.url.removePrefix("https://").removePrefix("http://"),
                      color = secondaryText,
                      fontSize = 11.sp,
                      maxLines = 2,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
