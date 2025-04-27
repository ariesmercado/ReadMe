package com.project.readme.presenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.readme.R
import com.project.readme.common.Resource
import com.project.readme.data.UserProfile
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ExamResult: ComponentActivity() {
    private val viewModel: ExamResultViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val score by viewModel.score.collectAsState()
            val profile by viewModel.profile.collectAsState()

            if (score is Resource.Success) {
                ExamResultContent(score.data, profile.data)
            }
        }
    }
}

@Composable
fun ExamResultContent(score: Int?, profile: UserProfile?) {
    //Color(android.graphics.Color.parseColor("#22bb33")
    Box (Modifier.fillMaxSize()
        .background(Color.White)) {
        Column (modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val percentage = ((score?.toDouble() ?: 0.0) / 69.0) * 100
            Timber.d("percentage_exam -> ${percentage}")
            val isPassed = percentage >= 75
            val image = if (isPassed) R.drawable.fireworks else R.drawable.failed_exam
            Image(
                painter = painterResource(id = image), // Replace with your drawable
                contentDescription = "Onboarding Illustration",
                modifier = Modifier
                    .size(300.dp)
            )

            Text(
                text = if (isPassed) "Congrats ${profile?.name}!" else "Sorry ${profile?.name}!",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "$score/69",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(android.graphics.Color.parseColor("#22bb33"))
            )

            Spacer(Modifier.height(16.dp))

            val message = if (isPassed) "You passed the exam with ${"%.2f".format(percentage)}%!" else "You failed the exam with ${"%.2f".format(percentage)}%. Don't give up — try again!"
            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49AFDC)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(.8f),
                contentPadding = PaddingValues(16.dp),
            ) {
                Text(
                    text = "Download the Result",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}

//@Composable
//@Preview
//fun ExamResultContentPreview() {
//    ExamResultContent(score.data, profile.data)
//}