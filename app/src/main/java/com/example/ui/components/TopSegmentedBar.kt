package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.ActiveSegment
import com.example.ui.theme.GlassPillDark
import com.example.ui.theme.GlassPillLight

@Composable
fun TopSegmentedBar(
  currentSegment: ActiveSegment,
  onSegmentSelected: (ActiveSegment) -> Unit,
  isDark: Boolean,
  modifier: Modifier = Modifier
) {
  val segments = listOf(
    ActiveSegment.START_PAGE,
    ActiveSegment.BOOKMARKS,
    ActiveSegment.READING_LIST,
    ActiveSegment.HISTORY
  )

  val activeIndex = segments.indexOf(currentSegment).coerceAtLeast(0)

  val animatedIndex by animateFloatAsState(
    targetValue = activeIndex.toFloat(),
    animationSpec = spring(
      dampingRatio = 0.82f,
      stiffness = Spring.StiffnessMediumLow
    ),
    label = "pill_position"
  )

  val barShape = RoundedCornerShape(30.dp)
  val pillShape = RoundedCornerShape(22.dp)

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(56.dp)
      .padding(horizontal = 16.dp)
      .glassEffect(isDark = isDark, shape = barShape, elevation = 6.dp)
      .testTag("top_segmented_bar"),
    contentAlignment = Alignment.CenterStart
  ) {
    // Sliding active pill background
    BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(4.dp)) {
      val segmentWidth = maxWidth / 4

      Box(
        modifier = Modifier
          .offset(x = segmentWidth * animatedIndex)
          .width(segmentWidth)
          .fillMaxHeight()
          .clip(pillShape)
          .background(if (isDark) GlassPillDark else GlassPillLight)
          .border(
            width = 0.5.dp,
            color = if (isDark) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.6f),
            shape = pillShape
          )
      )

      // The 4 interactive icons
      Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        segments.forEachIndexed { index, segment ->
          val isSelected = segment == currentSegment
          val iconTint = if (isSelected) {
            if (isDark) Color.White else Color(0xFF141A16)
          } else {
            if (isDark) Color.White.copy(alpha = 0.55f) else Color(0xFF141A16).copy(alpha = 0.55f)
          }

          Box(
            modifier = Modifier
              .weight(1f)
              .fillMaxHeight()
              .clip(pillShape)
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onSegmentSelected(segment) }
              )
              .testTag("segment_tab_${segment.name.lowercase()}"),
            contentAlignment = Alignment.Center
          ) {
            when (segment) {
              ActiveSegment.START_PAGE -> {
                Icon(
                  imageVector = Icons.Default.GridView,
                  contentDescription = "Start Page & Favorites",
                  tint = iconTint,
                  modifier = Modifier.size(22.dp)
                )
              }
              ActiveSegment.BOOKMARKS -> {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.MenuBook,
                  contentDescription = "Bookmarks",
                  tint = iconTint,
                  modifier = Modifier.size(22.dp)
                )
              }
              ActiveSegment.READING_LIST -> {
                // Custom Reading Glasses icon matching the screenshot exactly
                ReadingGlassesIcon(tint = iconTint, modifier = Modifier.size(24.dp))
              }
              ActiveSegment.HISTORY -> {
                Icon(
                  imageVector = Icons.Default.Schedule,
                  contentDescription = "History",
                  tint = iconTint,
                  modifier = Modifier.size(22.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}

/**
 * Authentic spectacles/reading glasses icon matching iOS Safari reading list icon.
 */
@Composable
fun ReadingGlassesIcon(
  tint: Color,
  modifier: Modifier = Modifier
) {
  Canvas(modifier = modifier) {
    val strokeWidth = 2.2.dp.toPx()
    val w = size.width
    val h = size.height
    val lensRadius = w * 0.18f
    val centerY = h * 0.52f

    // Left lens
    drawCircle(
      color = tint,
      radius = lensRadius,
      center = Offset(w * 0.30f, centerY),
      style = Stroke(strokeWidth)
    )

    // Right lens
    drawCircle(
      color = tint,
      radius = lensRadius,
      center = Offset(w * 0.70f, centerY),
      style = Stroke(strokeWidth)
    )

    // Bridge between lenses
    drawLine(
      color = tint,
      start = Offset(w * 0.30f + lensRadius, centerY - 1.dp.toPx()),
      end = Offset(w * 0.70f - lensRadius, centerY - 1.dp.toPx()),
      strokeWidth = strokeWidth
    )

    // Left arm
    drawLine(
      color = tint,
      start = Offset(w * 0.30f - lensRadius, centerY - 2.dp.toPx()),
      end = Offset(w * 0.08f, centerY - 5.dp.toPx()),
      strokeWidth = strokeWidth
    )

    // Right arm
    drawLine(
      color = tint,
      start = Offset(w * 0.70f + lensRadius, centerY - 2.dp.toPx()),
      end = Offset(w * 0.92f, centerY - 5.dp.toPx()),
      strokeWidth = strokeWidth
    )
  }
}
