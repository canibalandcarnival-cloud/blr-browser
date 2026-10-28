package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BrowserTab
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.EmeraldPrimary

@Composable
fun BottomAddressBar(
  currentTab: BrowserTab,
  tabCount: Int,
  isDark: Boolean,
  onBackClick: () -> Unit,
  onForwardClick: () -> Unit,
  onAddressClick: () -> Unit,
  onRefreshClick: () -> Unit,
  onTabsClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isBrowsing = !currentTab.isStartPage
  val primaryText = if (isDark) Color.White else Color(0xFF141916)
  val secondaryText = if (isDark) Color.White.copy(alpha = 0.6f) else Color(0xFF141916).copy(alpha = 0.6f)
  val iconTint = if (isDark) Color.White else Color(0xFF141916)

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    // Top loading indicator when web page is loading
    AnimatedVisibility(
      visible = currentTab.isLoading && isBrowsing,
      enter = fadeIn(),
      exit = fadeOut()
    ) {
      val animatedProgress by animateFloatAsState(
        targetValue = currentTab.progress.coerceIn(0.05f, 1f),
        label = "web_progress"
      )
      LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier
          .fillMaxWidth()
          .height(2.5.dp)
          .clip(RoundedCornerShape(2.dp)),
        color = if (isDark) EmeraldAccent else EmeraldPrimary,
        trackColor = Color.Transparent
      )
      Spacer(modifier = Modifier.height(4.dp))
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // 1. Back circular button
      val backEnabled = currentTab.canGoBack || isBrowsing
      Box(
        modifier = Modifier
          .size(52.dp)
          .glassEffect(isDark = isDark, shape = CircleShape, elevation = 6.dp)
          .bouncyClick(enabled = true, onClick = onBackClick)
          .testTag("browser_back_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = if (backEnabled) iconTint else iconTint.copy(alpha = 0.4f),
          modifier = Modifier.size(20.dp)
        )
      }

      // Optional Forward button if canGoForward in webview
      if (currentTab.canGoForward && isBrowsing) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .glassEffect(isDark = isDark, shape = CircleShape, elevation = 4.dp)
            .bouncyClick(enabled = true, onClick = onForwardClick)
            .testTag("browser_forward_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Forward",
            tint = iconTint,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      // 2. Center Search / Address Bar Pill
      val pillShape = RoundedCornerShape(28.dp)
      Box(
        modifier = Modifier
          .weight(1f)
          .height(52.dp)
          .glassEffect(isDark = isDark, shape = pillShape, elevation = 6.dp)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onAddressClick
          )
          .padding(horizontal = 14.dp)
          .testTag("browser_address_bar"),
        contentAlignment = Alignment.CenterStart
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (isBrowsing) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = "Secure SSL",
              tint = if (isDark) EmeraldAccent else EmeraldPrimary,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
          } else {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = secondaryText,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
          }

          // Address text or placeholder
          val displayText = if (isBrowsing) {
            val domain = try {
              val uri = java.net.URI(currentTab.url)
              val host = uri.host ?: currentTab.url
              host.removePrefix("www.")
            } catch (e: Exception) {
              currentTab.url
            }
            domain
          } else {
            "Search or enter website"
          }

          Text(
            text = displayText,
            color = if (isBrowsing) primaryText else secondaryText,
            fontSize = 15.sp,
            fontWeight = if (isBrowsing) FontWeight.Medium else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
          )

          Spacer(modifier = Modifier.width(8.dp))

          // Trailing action: Refresh or Mic
          if (isBrowsing) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .bouncyClick(onClick = onRefreshClick)
                .testTag("browser_reload_button"),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reload",
                tint = secondaryText,
                modifier = Modifier.size(18.dp)
              )
            }
          } else {
            Icon(
              imageVector = Icons.Default.Mic,
              contentDescription = "Voice Search",
              tint = secondaryText,
              modifier = Modifier.size(19.dp)
            )
          }
        }
      }

      // 3. Right Tabs Squircle Button
      val tabShape = RoundedCornerShape(18.dp)
      Box(
        modifier = Modifier
          .size(52.dp)
          .glassEffect(isDark = isDark, shape = tabShape, elevation = 6.dp)
          .bouncyClick(onClick = onTabsClick)
          .testTag("browser_tabs_button"),
        contentAlignment = Alignment.Center
      ) {
        // Dual-card tabs icon
        Box(
          modifier = Modifier.size(24.dp),
          contentAlignment = Alignment.Center
        ) {
          // Background offset square
          Box(
            modifier = Modifier
              .size(16.dp)
              .offset(x = 3.dp, y = (-3).dp)
              .border(
                1.6.dp,
                iconTint.copy(alpha = 0.5f),
                RoundedCornerShape(4.dp)
              )
          )
          // Foreground square with tab count
          Box(
            modifier = Modifier
              .size(17.dp)
              .offset(x = (-2).dp, y = 2.dp)
              .background(if (isDark) Color(0xFF1B241E) else Color(0xFFE5EDE8), RoundedCornerShape(4.dp))
              .border(1.6.dp, iconTint, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = tabCount.toString(),
              color = primaryText,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
