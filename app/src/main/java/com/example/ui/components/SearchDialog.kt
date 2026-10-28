package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.EmeraldPrimary
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDialog(
  initialQuery: String,
  isDark: Boolean,
  onDismiss: () -> Unit,
  onSearch: (String) -> Unit
) {
  var query by remember { mutableStateOf(initialQuery) }
  val focusRequester = remember { FocusRequester() }
  val keyboardController = LocalSoftwareKeyboardController.current

  val primaryText = if (isDark) Color.White else Color(0xFF141916)
  val secondaryText = if (isDark) Color.White.copy(alpha = 0.6f) else Color(0xFF141916).copy(alpha = 0.6f)

  val quickSites = remember {
    listOf(
      "google.com" to "Google Search",
      "wikipedia.org" to "Wikipedia Encyclopedia",
      "pinterest.com" to "Pinterest Visual Board",
      "surfacemag.com" to "Surface Design Mag",
      "eraceramics.com" to "Era Ceramics Studio",
      "kinfolk.com" to "Kinfolk Culture"
    )
  }

  LaunchedEffect(Unit) {
    delay(100)
    focusRequester.requestFocus()
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(if (isDark) Color(0xDD0A0F0D) else Color(0xEEF3F6F4))
        .statusBarsPadding()
        .navigationBarsPadding()
        .padding(16.dp)
        .testTag("search_overlay_dialog")
    ) {
      Column(
        modifier = Modifier.fillMaxSize()
      ) {
        // Top row with Cancel button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Search or Enter Website",
            color = primaryText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )

          TextButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("search_cancel_button")
          ) {
            Text(
              text = "Cancel",
              color = if (isDark) EmeraldAccent else EmeraldPrimary,
              fontWeight = FontWeight.SemiBold,
              fontSize = 15.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search text input bar
        OutlinedTextField(
          value = query,
          onValueChange = { query = it },
          placeholder = { Text("Search with Google or type URL", color = secondaryText) },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = secondaryText)
          },
          trailingIcon = {
            if (query.isNotEmpty()) {
              IconButton(onClick = { query = "" }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = secondaryText)
              }
            }
          },
          singleLine = true,
          keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Go,
            keyboardType = KeyboardType.Uri
          ),
          keyboardActions = KeyboardActions(
            onGo = {
              if (query.isNotBlank()) {
                keyboardController?.hide()
                onSearch(query.trim())
              }
            }
          ),
          shape = RoundedCornerShape(24.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = if (isDark) Color(0xFF17221C) else Color.White,
            unfocusedContainerColor = if (isDark) Color(0xFF131D17) else Color(0xFFF9FAF9),
            focusedBorderColor = if (isDark) EmeraldAccent else EmeraldPrimary,
            unfocusedBorderColor = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.15f),
            focusedTextColor = primaryText,
            unfocusedTextColor = primaryText
          ),
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .testTag("search_input_field")
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Suggestions / Quick links list
        Text(
          text = if (query.isEmpty()) "Suggested Sites" else "Search for \"$query\"",
          color = secondaryText,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        LazyColumn(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (query.isNotBlank()) {
            item {
              Surface(
                onClick = { onSearch(query.trim()) },
                shape = RoundedCornerShape(16.dp),
                color = if (isDark) Color(0xFF16201B) else Color.White,
                modifier = Modifier.fillMaxWidth().testTag("search_submit_row")
              ) {
                Row(
                  modifier = Modifier.padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(if (isDark) Color(0xFF22332A) else Color(0xFFE4F0E8)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Search,
                      contentDescription = null,
                      tint = if (isDark) EmeraldAccent else EmeraldPrimary,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                  Spacer(modifier = Modifier.width(12.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = "Search Google",
                      color = primaryText,
                      fontWeight = FontWeight.Medium,
                      fontSize = 15.sp
                    )
                    Text(
                      text = query,
                      color = secondaryText,
                      fontSize = 13.sp
                    )
                  }
                  Icon(
                    imageVector = Icons.Default.ArrowOutward,
                    contentDescription = null,
                    tint = secondaryText,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }
          }

          val filtered = quickSites.filter {
            query.isEmpty() || it.first.contains(query, ignoreCase = true) || it.second.contains(query, ignoreCase = true)
          }

          items(filtered) { (domain, title) ->
            Surface(
              onClick = { onSearch(domain) },
              shape = RoundedCornerShape(16.dp),
              color = if (isDark) Color(0xFF131D17) else Color.White,
              modifier = Modifier.fillMaxWidth().testTag("search_site_$domain")
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isDark) Color(0xFF1E2C24) else Color(0xFFEAF1EC)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = if (isDark) EmeraldAccent else EmeraldPrimary,
                    modifier = Modifier.size(18.dp)
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = title,
                    color = primaryText,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                  )
                  Text(
                    text = domain,
                    color = secondaryText,
                    fontSize = 12.sp
                  )
                }
                Icon(
                  imageVector = Icons.Default.ArrowOutward,
                  contentDescription = null,
                  tint = secondaryText.copy(alpha = 0.5f),
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
