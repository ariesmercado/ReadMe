package com.project.readme.presenter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.view.SoundEffectConstants
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import coil.compose.rememberAsyncImagePainter
import com.project.readme.R
import com.project.readme.data.Book
import com.project.readme.data.Page
import com.project.readme.data.genarator.LessonUtil
import com.project.readme.ui.theme.ReadMeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

@AndroidEntryPoint
class MainActivity : FragmentActivity(), EventHandler {
    private val viewModel: BookViewModel by viewModels()
    private lateinit var textToSpeech: TextToSpeech

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


        viewModel.loadLessons(this)

        textToSpeech = TextToSpeech(this, OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Set the language to US English
                textToSpeech.language = java.util.Locale.US

                // Set the speech rate (tempo), e.g., slower speed (value between 0.1 to 2.0)
                textToSpeech.setSpeechRate(0.6f)  // 1.0 is the default rate, lower value is slower

                // Set the voice, e.g., using a female voice
                val availableVoices = textToSpeech.voices
                Timber.d("availableVoices: $availableVoices")

                for (voice in availableVoices) {
                    if (voice.name.contains("female", ignoreCase = true)) {
                        textToSpeech.voice = voice
                        break
                    }
                }
            }
        })

        enableEdgeToEdge()
        setContent {
            val selectedLesson by viewModel.selectedBook.collectAsState()
            val isSuccessState by viewModel.sttResult.collectAsState()
            val favorites by viewModel.favorites.collectAsState()

            ReadMeTheme {
                BookApp(selectedLesson, this@MainActivity, favorites)

                isSuccessState?.let {
                    ResultDialog(
                        isSuccessState = it,
                        onDismiss = { viewModel.shutDownStt() }
                    )
                }
            }
        }
    }

    // Function to read aloud the page's text
    override fun speak(text: String) {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }

        // Use coroutines for delay between lines
        CoroutineScope(Dispatchers.Main).launch {
            text.split("++").forEach { line ->
                if (line.isNotBlank()) {
                    textToSpeech.speak(line, TextToSpeech.QUEUE_FLUSH, null, null)

                    // Wait until TTS finishes the current line
                    while (textToSpeech.isSpeaking) {
                        delay(100) // Check periodically if TTS is done speaking
                    }

                    // Add a 2-second delay before the next line
                    delay(1000)
                }
            }
        }

    }

    override fun toggleFavorite(title: String, isFavorite: Boolean) {
        viewModel.toggleFavorite(title, isFavorite)
    }

    // Function to start speech recognition
    override fun startListening(result: Boolean) {
        viewModel.updateSttResult(result)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.exitBook()
        textToSpeech.shutdown()
    }
}

interface EventHandler {
    fun speak(text: String)
    fun startListening(result: Boolean)
    fun toggleFavorite(title: String, isFavorite: Boolean)
}

@Composable
fun BookApp(
    selectedBook: Book?,
    eventHandler: EventHandler,
    favorites: List<String>
) {
    val books = selectedBook?.pages ?: emptyList()
    var currentBookIndex by remember { mutableIntStateOf(0) }

    BookPager(pages = books, eventHandler, favorites, selectedBook?.name.orEmpty()) { index -> currentBookIndex = index }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookPager(
    pages: List<Page>,
    eventHandler: EventHandler,
    favorites: List<String>,
    title: String,
    onBookChange: (Int) -> Unit
) {
    val view = LocalView.current
    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()
    var selectedPage by remember { mutableStateOf(pages.first()) }

    val recordIntent =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val searchString =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (searchString != null) {
                    val text = searchString[0]
                    val textResult = text.trim().lowercase(Locale.getDefault()).startsWith(selectedPage.text.split("-")[0].trim().lowercase(Locale.getDefault())) &&
                            text.trim().lowercase(Locale.getDefault()).contains(selectedPage.text.split("-")[1].trim().lowercase(Locale.getDefault()))

                    Timber.d("textResult -> ${text.trim().lowercase(Locale.getDefault())}")
                    Timber.d("textResult -> ${selectedPage.text.split("-")[0].trim().lowercase(Locale.getDefault())}")
                    eventHandler.startListening(textResult)
                }
            }
        }

    // Box to allow layering
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.y3ok_q74u_220311), // Replace with your background image resource
            contentDescription = null,
            contentScale = ContentScale.Crop, // Adjusts how the image fits the background
            modifier = Modifier
                .fillMaxSize() // Make the image fill the entire background
                .alpha(.2f)
        )

        val isFavorite = favorites.any { it.contains(title) }
        IconButton(onClick = { eventHandler.toggleFavorite(title, isFavorite) }, modifier = Modifier.padding(16.dp).align(Alignment.TopEnd)) {

            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isFavorite) "remove" else "add",
                tint = if (isFavorite) Color.Red else Color.DarkGray,
                modifier = Modifier.size(48.dp)
            )
        }

        // Foreground content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            // Top Section: ViewPager with Arrows
            Row(
                modifier = Modifier
                    .weight(1f) // Allow this section to take remaining space
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // Left Arrow
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CuteArrowButton(
                        icon = painterResource(id = R.drawable.previous),
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            if (pagerState.currentPage > 0) {
                                scope.launch {
                                    pagerState.scrollToPage(pagerState.currentPage - 1)
                                    onBookChange(pagerState.currentPage)
                                }
                            }
                        }
                    )
                }

                // ViewPager
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f), // Take remaining horizontal space
                    userScrollEnabled = false
                ) { page ->
                    val book = pages[page]
                    selectedPage = book
                    BookViewer(page = book, eventHandler)
                }

                // Right Arrow
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CuteArrowButton(
                        icon = painterResource(id = R.drawable.next),
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            if (pagerState.currentPage < pages.size - 1) {
                                scope.launch {
                                    pagerState.scrollToPage(pagerState.currentPage + 1)
                                    onBookChange(pagerState.currentPage)
                                }
                            }
                        }
                    )
                }
            }

            // Bottom Section: Card with Microphone and Speaker
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                // "Listen" Button Container
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF49AFDC)), // Megaphone background color
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            if (selectedPage.text.contains("-")) {
                                val splitWords = selectedPage.text.split("-")
                                scope.launch {
                                    splitWords.forEach { text ->
                                        delay(1000)
                                        eventHandler.speak(text)
                                    }
                                }

                            } else {
                                eventHandler.speak(selectedPage.text)
                            }
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CuteArrowButton(
                            icon = painterResource(id = R.drawable.megaphone),
                            onClick = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                if (selectedPage.text.contains("-")) {
                                    val splitWords = selectedPage.text.split("-")
                                    scope.launch {
                                        splitWords.forEach { text ->
                                            eventHandler.speak(text)
                                            delay(1000)
                                        }
                                    }
                                } else {
                                    eventHandler.speak(selectedPage.text)
                                }
                            },
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Listen",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary, // Ensure text contrast
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                }
                // "Speak" Button Container

                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault()
                    )
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
                }

                val context = LocalContext.current
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7C700)), // Microphone background color
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            if (LessonUtil.isAllowedToSpeak(title)) {
                                recordIntent.launch(intent)
                            } else {
                                Toast.makeText(context,"Speak features is not available", Toast.LENGTH_SHORT).show()
                            }
                        }

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CuteArrowButton(
                            icon = painterResource(id = R.drawable.microphone),
                            onClick = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)

                                if (LessonUtil.isAllowedToSpeak(title)) {
                                    recordIntent.launch(intent)
                                } else {
                                    Toast.makeText(context,"Speak features is not available", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Speak",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary, // Ensure text contrast
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BookViewer(page: Page, eventHandler: EventHandler) {
    Column(modifier = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(16.dp)), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = rememberAsyncImagePainter(page.image),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentDescription = null
        )

//        LaunchedEffect(page) {  //used chartDataPackage as Key
//            if (page.text.contains("-")) {
//                val splitWords = page.text.split("-")
//                splitWords.forEach { text ->
//                    eventHandler.speak(text)
//                    delay(1000)
//                }
//            } else {
//                eventHandler.speak(page.text)
//            }
//        }

    }
}



// CuteArrowButton composable for navigation buttons
@Composable
fun CuteArrowButton(icon: Painter, onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick) {
        Image(painter = icon, contentDescription = null, modifier = modifier)
    }
}

@Composable
fun ResultDialog(
    isSuccessState: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Play sound and set up auto-dismiss
    LaunchedEffect(isSuccessState) {
        // Play sound
//        val soundRes = if (isSuccessState) {
//            R.raw.success_sound // Replace with your success sound resource
//        } else {
//            R.raw.error_sound // Replace with your error sound resource
//        }
//
//        val mediaPlayer = MediaPlayer.create(context, soundRes)
//        mediaPlayer.start()
//        mediaPlayer.setOnCompletionListener { it.release() }

        // Auto-dismiss after 3 seconds
        delay(3000)
        onDismiss()
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = if (isSuccessState) "Correct!" else "Wrong!",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = if (isSuccessState) {
                    "You answered correctly. Well done!"
                } else {
                    "Oops! That was incorrect. Try again."
                },
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}



