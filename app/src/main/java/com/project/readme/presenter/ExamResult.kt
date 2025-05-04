package com.project.readme.presenter

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import com.project.readme.R
import com.project.readme.common.Resource
import com.project.readme.data.UserProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamResult : ComponentActivity() {
    private val viewModel: ExamResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val score by viewModel.score.collectAsState()
            val profile by viewModel.profile.collectAsState()

            if (score is Resource.Success && profile.data != null) {
                ExamResultContent(score.data, profile.data)
            }
        }
    }
}

@Composable
fun ExamResultContent(score: Int?, profile: UserProfile?) {
    val context = LocalContext.current
    val percentage = ((score?.toDouble() ?: 0.0) / 69.0) * 100
    val isPassed = percentage >= 75
    val image = if (isPassed) R.drawable.fireworks else R.drawable.failed_exam

    var bitmapToSave by remember { mutableStateOf<Bitmap?>(null) }

    val viewRef = remember { mutableStateOf<View?>(null) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                ComposeView(ctx).apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        ResultContent(score, profile, isPassed, image, percentage)
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { viewRef.value = it }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .align(Alignment.BottomCenter)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    viewRef.value?.let { view ->
                        val bitmap = view.drawToBitmap()
                        bitmapToSave = bitmap
                        bitmapToSave?.let { saveImageToGallery(context, it) }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49AFDC)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(0.8f),
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

@Composable
fun ResultContent(
    score: Int?,
    profile: UserProfile?,
    isPassed: Boolean,
    image: Int,
    percentage: Double
) {
    Box(Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "Exam Result Image",
                modifier = Modifier.size(300.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = if (isPassed) "Congrats ${profile?.name}!" else "Sorry ${profile?.name}!",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "${score ?: 0}/69",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(android.graphics.Color.parseColor("#22bb33"))
            )

            Spacer(Modifier.height(16.dp))

            val message = if (isPassed) {
                "You passed the exam with ${"%.2f".format(percentage)}%!"
            } else {
                "You failed the exam with ${"%.2f".format(percentage)}%. Don't give up — try again!"
            }

            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }

}

fun saveImageToGallery(context: Context, bitmap: Bitmap) {
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.TITLE, "Exam Result")
        put(
            MediaStore.Images.Media.DISPLAY_NAME,
            "exam_result_${System.currentTimeMillis()}.jpg"
        )
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ExamResults")
    }

    val resolver = context.contentResolver
    val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    try {
        imageUri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                Toast.makeText(context, "Image saved to gallery!", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
    }
}
