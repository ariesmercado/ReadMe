package com.project.readme.presenter

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.project.readme.R
import com.project.readme.common.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComprehensionCover: ComponentActivity() {
    private val viewModel: CoverViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView

        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = true // true or false as desired.
        // And then you can set any background color to the status bar.
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {
            val score by viewModel.score.collectAsState()
            CoverAndOption(:: navigateToStoryAndQuizzes, ::navigateToResult, score)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getScore()
    }

    private fun navigateToStoryAndQuizzes() {
        val level = viewModel.level
        val intent = Intent(this, StoryAndQuiz::class.java)
        intent.putExtra("level", level)
        startActivity(intent)
        finish()
    }

    private fun navigateToResult() {
        val level = viewModel.level
        val intent = Intent(this, ExamResult::class.java)
        intent.putExtra("level", level)
        startActivity(intent)
        finish()
    }

}

@Composable
fun CoverAndOption(onTake: () -> Unit, onResult: () -> Unit, score: Resource<Int?>) {
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

            Spacer(Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = "Instructions:",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "1. Choose the word or sentence that fits in the blank.",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Text(
                        text = "2. Select the correct option from the choices provided.",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "3. Review your answers before submitting.",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF49AFDC)),
                modifier = Modifier
                    .clickable {
                        onTake.invoke()
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

            if (score is Resource.Success && score.data != null) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7C700)),
                    modifier = Modifier
                        .clickable {
                            onResult.invoke()
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
}