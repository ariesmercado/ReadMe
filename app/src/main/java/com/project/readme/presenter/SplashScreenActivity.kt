package com.project.readme.presenter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.project.readme.R
import com.project.readme.common.Resource
import com.project.readme.data.UserProfile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity() {
    private val viewModel: RegisterViewModel by viewModels()
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
            val profile by viewModel.profile.collectAsState()
            SplashScreen {
                if (profile is Resource.Success) {
                    navigateToMainScreen(profile.data)
                }
            }
        }
    }

    private fun navigateToMainScreen(profile: UserProfile?) {
        lifecycleScope.launch {
            delay(2000)
            if (profile == null) {
                val intent = Intent(this@SplashScreenActivity, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashScreenActivity, BookAppActivity::class.java)
                intent.putExtra("USER", profile)
                startActivity(intent)
                finish()
            }

        }
    }

    @Composable
    fun SplashScreen(onTimeout: () -> Unit) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            // Vertically centered content
            Box (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.readme_bg), // Replace with your background image resource
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight, // Adjusts how the image fits the background
                    modifier = Modifier
                        .fillMaxSize() // Make the image fill the entire background
                )

                Image(
                    painter = painterResource(id = R.drawable.readme_brand_logo), // Replace with your background image resource
                    contentDescription = null,
                    contentScale = ContentScale.Inside, // Adjusts how the image fits the background
                    modifier = Modifier.size(300.dp).align(Alignment.Center)
                )


            }
        }

        onTimeout()

    }

    @Preview(showBackground = true)
    @Composable
    fun SplashScreenPreview() {
        SplashScreen(onTimeout = {})
    }
}
