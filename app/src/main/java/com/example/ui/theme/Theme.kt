package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = EmeraldAccent,
    onPrimary = Color(0xFF00391E),
    primaryContainer = EmeraldDark,
    onPrimaryContainer = EmeraldLight,
    secondary = EmeraldLight,
    onSecondary = Color(0xFF003822),
    background = Color(0xFF0A0F0D),
    onBackground = TextPrimaryDark,
    surface = Color(0xFF111815),
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF1E2822),
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0x33FFFFFF)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = EmeraldLight,
    onPrimaryContainer = Color(0xFF002111),
    secondary = EmeraldDark,
    onSecondary = Color.White,
    background = Color(0xFFF2F5F3),
    onBackground = TextPrimaryLight,
    surface = Color(0xFFFCFDFC),
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFE2EBE5),
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0x26000000)
  )

@Composable
fun FlowBrowserTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Set false to preserve our tailored frosted emerald identity
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
