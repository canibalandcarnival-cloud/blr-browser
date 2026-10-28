package com.example.ui.components

import android.os.Build
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Applies a frosted glass styling with specular edge highlight, translucent tint,
 * and backdrop blur where supported.
 */
fun Modifier.glassEffect(
  isDark: Boolean,
  shape: Shape = RoundedCornerShape(20.dp),
  elevation: Dp = 8.dp,
  customAlpha: Float? = null
): Modifier = this
  .shadow(
    elevation = elevation,
    shape = shape,
    ambientColor = if (isDark) Color(0x66000000) else Color(0x33000000),
    spotColor = if (isDark) Color(0x80000000) else Color(0x26000000)
  )
  .clip(shape)
  .background(
    if (isDark) {
      val alpha = customAlpha ?: 0.60f
      Color(0xFF131D17).copy(alpha = alpha)
    } else {
      val alpha = customAlpha ?: 0.72f
      Color(0xFFF6F8F7).copy(alpha = alpha)
    }
  )
  .border(
    width = 0.8.dp,
    brush = Brush.linearGradient(
      colors = if (isDark) {
        listOf(
          Color.White.copy(alpha = 0.35f),
          Color.White.copy(alpha = 0.08f),
          Color.White.copy(alpha = 0.18f)
        )
      } else {
        listOf(
          Color.White.copy(alpha = 0.80f),
          Color.White.copy(alpha = 0.25f),
          Color.White.copy(alpha = 0.60f)
        )
      },
      start = Offset(0f, 0f),
      end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    ),
    shape = shape
  )

/**
 * Smooth bouncy micro-interaction for tactile button feedback.
 */
@Composable
fun Modifier.bouncyClick(
  enabled: Boolean = true,
  onClick: () -> Unit
): Modifier {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.93f else 1f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "bouncy_scale"
  )

  return this
    .scale(scale)
    .clickable(
      interactionSource = interactionSource,
      indication = null,
      enabled = enabled,
      onClick = onClick
    )
}

/**
 * Beautiful squircle icon representing the browser favorites as shown in the screenshot.
 */
@Composable
fun SquircleIcon(
  iconType: String,
  bgColor: Long,
  fgColor: Long,
  badgeText: String?,
  modifier: Modifier = Modifier,
  iconSize: Dp = 64.dp
) {
  val shape = RoundedCornerShape(18.dp)

  Box(
    modifier = modifier
      .size(iconSize)
      .shadow(6.dp, shape, ambientColor = Color(0x33000000), spotColor = Color(0x40000000))
      .clip(shape)
      .then(
        if (iconType == "cc_gradient") {
          Modifier.background(
            Brush.linearGradient(
              colors = listOf(
                Color(0xFFFFD1C7),
                Color(0xFFE8D0FF),
                Color(0xFFC0EBF0)
              )
            )
          )
        } else {
          Modifier.background(Color(bgColor))
        }
      )
      .border(0.5.dp, Color.White.copy(alpha = 0.25f), shape),
    contentAlignment = Alignment.Center
  ) {
    when (iconType) {
      "rings" -> {
        // Interlocking geometric rings (Curious and Wondrous Things)
        Canvas(modifier = Modifier.fillMaxSize(0.65f)) {
          val stroke = Stroke(width = 3.5.dp.toPx())
          val color = Color(fgColor)
          val w = size.width
          val h = size.height
          // Center ring
          drawCircle(color = color, radius = w * 0.38f, center = Offset(w * 0.5f, h * 0.5f), style = stroke)
          // Top inner ring
          drawCircle(color = color, radius = w * 0.22f, center = Offset(w * 0.5f, h * 0.38f), style = stroke)
          // Lower small ring
          drawCircle(color = color, radius = w * 0.14f, center = Offset(w * 0.5f, h * 0.65f), style = stroke)
        }
      }
      "droplet" -> {
        // Stylized water droplet (Rit Dye)
        Canvas(modifier = Modifier.fillMaxSize(0.55f)) {
          val stroke = Stroke(width = 3.5.dp.toPx())
          val path = Path().apply {
            val w = size.width
            val h = size.height
            moveTo(w * 0.5f, h * 0.1f)
            cubicTo(w * 0.85f, h * 0.55f, w * 0.95f, h * 0.85f, w * 0.5f, h * 0.95f)
            cubicTo(w * 0.05f, h * 0.85f, w * 0.15f, h * 0.55f, w * 0.5f, h * 0.1f)
            close()
          }
          drawPath(path = path, color = Color(fgColor), style = stroke)
        }
      }
      "pinterest" -> {
        // Red circle with white cursive 'P'
        Box(
          modifier = Modifier
            .fillMaxSize(0.72f)
            .clip(RoundedCornerShape(percent = 50))
            .background(Color(0xFFE60023)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "P",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 24.sp,
            fontFamily = FontFamily.Serif
          )
        }
      }
      "era_ceramic" -> {
        // Geometric modernist logo
        Canvas(modifier = Modifier.fillMaxSize(0.6f)) {
          val stroke = Stroke(width = 4.dp.toPx())
          val color = Color(fgColor)
          val w = size.width
          val h = size.height
          // Horizontal axis
          drawLine(color = color, start = Offset(w * 0.15f, h * 0.5f), end = Offset(w * 0.85f, h * 0.5f), strokeWidth = 4.dp.toPx())
          // Arch left
          drawArc(
            color = color,
            startAngle = 90f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.25f, h * 0.2f),
            size = androidx.compose.ui.geometry.Size(w * 0.35f, h * 0.6f),
            style = stroke
          )
          // Arch right
          drawArc(
            color = color,
            startAngle = 270f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.5f, h * 0.2f),
            size = androidx.compose.ui.geometry.Size(w * 0.35f, h * 0.6f),
            style = stroke
          )
        }
      }
      else -> {
        // Bold typographic monogram (CC/, S, M, K, etc.)
        Text(
          text = badgeText ?: "?",
          color = Color(fgColor),
          fontSize = if ((badgeText ?: "").length > 1) 22.sp else 30.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = if (iconType == "serif_s") FontFamily.Serif else FontFamily.SansSerif
        )
      }
    }
  }
}
