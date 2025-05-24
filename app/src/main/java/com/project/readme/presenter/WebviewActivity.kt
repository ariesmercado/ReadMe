package com.project.readme.presenter

import android.os.Build
import com.project.readme.ui.theme.ReadMeTheme
import dagger.hilt.android.AndroidEntryPoint
import android.os.Bundle
import android.view.View.OVER_SCROLL_NEVER
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import timber.log.Timber

@AndroidEntryPoint
class WebviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val link = "file:///android_asset/Lessons/Addition/Addition.html"
        setContent {
            ReadMeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LessonWithQuizScreen(link)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonWithQuizScreen(link: String) {
    // Visibility state for button
    val showButton = remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (showButton.value) {
                Button(
                    onClick = { /* Navigate to quiz or show dialog */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Take Quiz")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AssetWebView(
                fileName = link,
                modifier = Modifier.weight(1f),
                onBottomReached = {
                    showButton.value = true
                }
            )
        }
    }
}

@Composable
fun AssetWebView(
    fileName: String,
    modifier: Modifier = Modifier,
    onBottomReached: () -> Unit
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                    loadWithOverviewMode = true
                    useWideViewPort = false // prevents wide zoom layout
                }

                // Disable horizontal scrolling
                isHorizontalScrollBarEnabled = false
                setHorizontalScrollBarEnabled(false)
                isVerticalScrollBarEnabled = true
                isScrollbarFadingEnabled = true
                overScrollMode = OVER_SCROLL_NEVER

                // Only use scroll listener if supported
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    setOnScrollChangeListener { _, _, scrollY, _, _ ->
                        val contentHeightPx = contentHeight * scale
                        val viewBottom = height + scrollY
                        if (viewBottom >= contentHeightPx - 10) {
                            onBottomReached()
                        }
                    }
                }

                loadUrl(fileName)
            }
        },
        modifier = modifier
    )
}
