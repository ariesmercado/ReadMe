package com.project.readme.presenter

import android.content.Intent
import android.os.Bundle
import android.view.SoundEffectConstants
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.project.readme.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView

        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = true // true or false as desired.
        // And then you can set any background color to the status bar.
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            OnboardingScreen(
                ::navigateToMainScreen
            )
        }
    }

    private fun navigateToMainScreen() {
            val intent = Intent(this@OnBoardingActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
    }

}

@Composable
fun OnboardingScreen(onGetStartedClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Illustration
            Image(
                painter = painterResource(id = R.drawable.ob), // Replace with your drawable
                contentDescription = "Onboarding Illustration",
                modifier = Modifier
                    .size(300.dp)
            )

            // Header Text
            Text(
                text = "Welcome, Little Reader!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description Text
            Text(
                text = "Hi there! Ready to start your reading adventure? Discover books to read—just one page at a time!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(80.dp))

            // Get Started Button

            val view = LocalView.current
            Button(
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    onGetStartedClick()
                          },
                colors = ButtonDefaults.buttonColors(containerColor = Color(android.graphics.Color.parseColor("#66cbad"))),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
