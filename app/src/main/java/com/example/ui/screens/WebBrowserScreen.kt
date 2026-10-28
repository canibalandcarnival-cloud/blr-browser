package com.example.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.BrowserTab

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebBrowserScreen(
  currentTab: BrowserTab,
  onUpdateTabState: (String, String, String, Boolean, Float, Boolean, Boolean) -> Unit,
  onPageFinishedNavigation: (String, String) -> Unit,
  onScrollDelta: (Int) -> Unit,
  onCloseToHome: () -> Unit,
  modifier: Modifier = Modifier
) {
  var webViewInstance by remember { mutableStateOf<WebView?>(null) }

  // System back button handling
  BackHandler(enabled = true) {
    if (webViewInstance?.canGoBack() == true) {
      webViewInstance?.goBack()
    } else {
      onCloseToHome()
    }
  }

  // Load URL when currentTab url changes
  LaunchedEffect(currentTab.url) {
    if (currentTab.url.isNotBlank() && webViewInstance?.url != currentTab.url) {
      webViewInstance?.loadUrl(currentTab.url)
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
      .testTag("webview_container")
  ) {
    AndroidView(
      factory = { context ->
        WebView(context).apply {
          layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )

          settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            userAgentString = settings.userAgentString + " FlowBrowser/1.0 Mobile"
          }

          // Track scroll delta to collapse or show bottom address bar smoothly
          setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            val delta = scrollY - oldScrollY
            onScrollDelta(delta)
          }

          webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
              val progressFraction = newProgress / 100f
              val isLoading = newProgress < 100
              onUpdateTabState(
                currentTab.id,
                view?.title ?: currentTab.title,
                view?.url ?: currentTab.url,
                isLoading,
                progressFraction,
                view?.canGoBack() ?: false,
                view?.canGoForward() ?: false
              )
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
              if (!title.isNullOrEmpty()) {
                onUpdateTabState(
                  currentTab.id,
                  title,
                  view?.url ?: currentTab.url,
                  currentTab.isLoading,
                  currentTab.progress,
                  view?.canGoBack() ?: false,
                  view?.canGoForward() ?: false
                )
              }
            }
          }

          webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
              val targetUrl = url ?: currentTab.url
              onUpdateTabState(
                currentTab.id,
                view?.title ?: "Loading...",
                targetUrl,
                true,
                0.15f,
                view?.canGoBack() ?: false,
                view?.canGoForward() ?: false
              )
            }

            override fun onPageFinished(view: WebView?, url: String?) {
              val finalUrl = url ?: currentTab.url
              val finalTitle = view?.title ?: finalUrl
              onUpdateTabState(
                currentTab.id,
                finalTitle,
                finalUrl,
                false,
                1.0f,
                view?.canGoBack() ?: false,
                view?.canGoForward() ?: false
              )
              onPageFinishedNavigation(finalTitle, finalUrl)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
              return false
            }
          }

          webViewInstance = this
          if (currentTab.url.isNotBlank()) {
            loadUrl(currentTab.url)
          }
        }
      },
      update = { webView ->
        webViewInstance = webView
      },
      modifier = Modifier.fillMaxSize()
    )
  }
}
