package com.project.readme.presenter

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import com.project.readme.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComprehensionCover: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 (API 30) and above
            decorView.windowInsetsController?.let { controller ->
                controller.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // For Android 10 (API 29) and below
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }


        setContent {
            CoverAndOption()
        }
    }

}

@Composable
fun CoverAndOption() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.readme_bg), // Replace with your background image resource
            contentDescription = null,
            contentScale = ContentScale.FillHeight, // Adjusts how the image fits the background
            modifier = Modifier.fillMaxSize() // Make the image fill the entire background
                .alpha(.8f)
        )
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.story_cover),
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentDescription = null
                )
            }

            Spacer(Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF49AFDC)),
                modifier = Modifier
                    .clickable {
                    }
                    .fillMaxWidth(.8f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Take Comprehension Test",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7C700)),
                modifier = Modifier
                    .clickable {
                    }
                    .fillMaxWidth(.8f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "See Last Result",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}