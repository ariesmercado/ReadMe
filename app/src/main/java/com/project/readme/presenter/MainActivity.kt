package com.project.readme.presenter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.project.readme.data.Quiz
import com.project.readme.data.Quizzes

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
            val quizes by viewModel.quizes.collectAsState()
            val currentQuiz by viewModel.currentQuiz.collectAsState()
            val quizStatus by viewModel.quizStatus.collectAsState()
            val selectedAnswer by viewModel.selectedAnswer.collectAsState()
            val correctAnswer by viewModel.correctAnswer.collectAsState()

            val isLast = quizes?.quiz?.last()?.id == currentQuiz?.id

            ReadMeTheme {
                if (selectedLesson != null) {
                    BookApp(selectedLesson,quizes, this@MainActivity, favorites)
                } else {
                    CircularProgressBar()
                }


                isSuccessState?.let {
                    ResultDialog(
                        isSuccessState = it,
                        onDismiss = { viewModel.shutDownStt() }
                    )
                }

                when (quizStatus) {
                    QuizStatus.Show -> {
                        GameExamDialog(this@MainActivity,selectedAnswer,currentQuiz, isLast)
                    }
                    QuizStatus.Result -> {
                        GameResultDialog(
                            score = correctAnswer,
                            items = quizes?.quiz?.size ?: 0,
                            onHomeClick = { finish() },
                            context = LocalContext.current,
                            onPlayClick = {
                            viewModel.updateStatus(QuizStatus.Hide)
                            viewModel.loadLessons(this@MainActivity)
                            }
                        )
                    }
                    else -> {}
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

    override fun showQuiz(qs: QuizStatus) {
        viewModel.updateStatus(qs)
    }

    override fun onNext(answer: Int) {
        viewModel.onNextQuiz(answer)
    }

    override fun onSubmit(answer: Int) {
        viewModel.onSubmitQuiz(answer)
    }

    override fun onForceSubmitScore() {
        viewModel.onForceSubmitScore()
    }

    override fun onChoose(answer: Int) {
        viewModel.onChooseAnswer(answer)
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
    fun onNext(answer: Int)
    fun showQuiz(qs: QuizStatus)
    fun onChoose(answer: Int)
    fun onSubmit(answer: Int)
    fun onForceSubmitScore()
}

@Composable
fun BookApp(
    selectedBook: Book?,
    quizes: Quizzes?,
    eventHandler: EventHandler,
    favorites: List<String>
) {
    val books = selectedBook?.pages ?: emptyList()
    var currentBookIndex by remember { mutableIntStateOf(0) }

    BookPager(pages = books,quizes, eventHandler, favorites, selectedBook?.name.orEmpty()) { index -> currentBookIndex = index }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookPager(
    pages: List<Page>,
    quizes: Quizzes?,
    eventHandler: EventHandler,
    favorites: List<String>,
    title: String,
    onBookChange: (Int) -> Unit
) {
    val view = LocalView.current
    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()
    var selectedPage by remember { mutableStateOf(pages.first()) }
    val context = LocalContext.current

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

        IconButton(onClick = {
            (context as? Activity)?.finish()
        }, modifier = Modifier.padding(16.dp).align(Alignment.TopStart)) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = if (isFavorite) "remove" else "add",
                tint = Color.DarkGray,
                modifier = Modifier.size(48.dp)
            )
        }

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
            var isProgressFinished = remember { mutableStateOf(false) }
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
                    BookViewer(page = book,quizes, isProgressFinished, eventHandler)
                    if (quizes == null && pages.last().image == book.image) {
                        eventHandler.onForceSubmitScore()
                    }
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

                            if (selectedPage.name.lowercase().contains("phonics")) {
                                val resId = LessonUtil.getRawResIdByName(selectedPage.name)
                                resId?.let {
                                    val mediaPlayer = MediaPlayer.create(context, it)
                                    mediaPlayer?.start()
                                }
                            } else {
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
                                if (selectedPage.name.lowercase().contains("phonics")) {
                                    val resId = LessonUtil.getRawResIdByName(selectedPage.name)
                                    resId?.let {
                                        val mediaPlayer = MediaPlayer.create(context, it)
                                        mediaPlayer?.start()
                                    }
                                } else {
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
                if ((quizes?.quiz?.size ?: 0) <= 0) {
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
                                    Toast.makeText(
                                        context,
                                        "Speak features is not available",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
                                        Toast.makeText(
                                            context,
                                            "Speak features is not available",
                                            Toast.LENGTH_SHORT
                                        ).show()
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

                if ((quizes?.quiz?.size ?: 0) > 0) {
                    QuizCard(eventHandler)
                }
            }
        }
    }
}


@Composable
fun QuizCard(eventHandler: EventHandler) {
    var isEnabled by remember { mutableStateOf(false) }
    var countdown by remember { mutableIntStateOf(16) }

    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000L)
            countdown--
        }
        isEnabled = true
    }

    val enabledColor = Color(android.graphics.Color.parseColor("#66cbad"))
    val disabledColor = Color(android.graphics.Color.parseColor("#66cbad")).copy(alpha = 0.6f)

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) enabledColor else disabledColor
        ),
        modifier = Modifier
            .padding(start = 4.dp)
            .then(
                if (isEnabled) Modifier.clickable {
                    eventHandler.showQuiz(QuizStatus.Show)
                } else Modifier
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
        ) {
            // Use AnimatedVisibility to maintain layout space
                CuteArrowButton(
                    icon = painterResource(id = R.drawable.quizpoint),
                    onClick = {
                        eventHandler.showQuiz(QuizStatus.Show)
                    },
                    modifier = Modifier.size(24.dp)
                )

            Text(
                text = if (isEnabled) "Take Quiz" else "Quiz starts in ${if(countdown > 15) 15 else countdown} seconds",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}



@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BookViewer(
    page: Page,
    quizes: Quizzes?,
    isProgressFinished: MutableState<Boolean>,
    eventHandler: EventHandler
) {
    val durationMillis = 16_000L
    val progress = remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = withFrameNanos { it }
        var elapsed: Long

        do {
            val currentTime = withFrameNanos { it }
            elapsed = (currentTime - startTime) / 1_000_000 // Convert to ms
            progress.floatValue = (elapsed.toFloat() / durationMillis).coerceIn(0f, 1f)
        } while (elapsed < durationMillis)

        if (quizes != null) {
            isProgressFinished.value = true
        }
    }

    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
    ) {
        val maxWidthPx = constraints.maxWidth.toFloat()
        val maxWidthDp = remember(maxWidthPx, density) {
            with(density) { maxWidthPx.toDp() }
        }

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (imageRef, progressRef) = createRefs()

            Image(
                painter = rememberAsyncImagePainter(model = page.image),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )

            if (quizes != null) {
                Box(
                    modifier = Modifier
                        .constrainAs(progressRef) {
                            start.linkTo(imageRef.start)
                            bottom.linkTo(imageRef.bottom)
                        }
                        .height(6.dp)
                        .width(maxWidthDp * progress.floatValue)
                        .background(Color.Red)
                )
            }
        }
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


@Preview(widthDp = 800, heightDp = 430)
@Composable
fun GameExamPreview() {
    GameExamDialog(null, null, Quizzes.QuizzesLIZA.quiz[0], false)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameExamDialog(
    eventHandler: EventHandler?,
    selectedAnswer: Int?,
    currentQuiz: Quiz?,
    isLast: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C3E50).copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        // Gold Border Wrapper
        Box(
            modifier = Modifier
                .fillMaxSize(.9f)
                .border(
                    width = 6.dp,
                    color = Color(0xFFFFD700), // Gold color
                    shape = RoundedCornerShape(28.dp)
                )
                .background(Color(0xFFFCE9CB), shape = RoundedCornerShape(22.dp))
                .padding(start = 24.dp, bottom = 24.dp)
        ) {

            IconButton(onClick = {
                eventHandler?.showQuiz(QuizStatus.Hide)
            }, modifier = Modifier.padding(8.dp).align(Alignment.TopEnd)) {

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

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
                            text = currentQuiz?.question.orEmpty(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                FlowRow (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp) // Space between rows
                ) {
                    currentQuiz?.choices?.forEachIndexed { index, choice ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(
                                2.dp,
                                when {
                                    selectedAnswer != null && selectedAnswer == index -> Color.Blue
                                    else -> Color.LightGray
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .clickable {
                                    eventHandler?.onChoose(index)
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(16.dp),
                            ) {
                                Text(
                                    text = "${'A' + index}) $choice",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                    }
                }

                Spacer(Modifier.height(24.dp))

                val buttonText = when {
                    selectedAnswer != null && !isLast -> "Next Question"
                    isLast -> "See Result"
                    else -> "Next Question"
                }

                Button(
                    onClick = {
                        selectedAnswer?.let {
                            when (buttonText) {
                                "Next Question" -> { eventHandler?.onNext(it)}
                                else -> { eventHandler?.onSubmit(it) }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49AFDC)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    enabled = selectedAnswer != null
                ) {
                    Text(
                        text = buttonText,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun GameResultDialog(
    score: Int,
    onHomeClick: () -> Unit,
    onPlayClick: () -> Unit,
    context: Context,
    items: Int = 0
) {

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // 🔥 This makes it react only when answerStatus changes
    LaunchedEffect(score) {
        mediaPlayer?.release() // release previous if any

        when (score) {
            in 1..3 -> {
                mediaPlayer = MediaPlayer.create(context, R.raw.success)
                mediaPlayer?.start()
            }
            else -> {
                mediaPlayer = MediaPlayer.create(context, R.raw.wrong)
                mediaPlayer?.start()
            }
        }
    }


    val calculatedScore: Float = (score.toFloat() / items) * 3f
    val (starsEarned, label, bannerColor) = when {
        calculatedScore >= 3f -> Triple(3, "EXCELLENT", Color(0xFF4CAF50))
        calculatedScore > 1f -> Triple(2, "   GOOD  ", Color(0xFFFF9800))
        calculatedScore == 1f -> Triple(1, "  It's Okay  ", Color(0xFFF44336))
        else ->       Triple(0, "  Failed  ", Color(0xFFF44336))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C3E50).copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        // Gold Border Wrapper
        Box(
            modifier = Modifier
                .border(
                    width = 6.dp,
                    color = Color(0xFFFFD700), // Gold color
                    shape = RoundedCornerShape(28.dp)
                )
                .background(Color(0xFFFCE9CB), shape = RoundedCornerShape(22.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Banner
                Box(
                    modifier = Modifier
                        .background(bannerColor, RoundedCornerShape(16.dp))
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stars
                Row(horizontalArrangement = Arrangement.Center) {
                    repeat(3) { index ->
                        val starColor = if (index < starsEarned) Color(0xFFFFC107) else Color.Gray
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = starColor,
                            modifier = Modifier.size(40.dp).padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("YOUR SCORE", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
                Text("$score/$items", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF57C00))

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    IconButton(onClick = onHomeClick) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.White,
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color(0xFF2196F3), CircleShape)
                                .padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    IconButton(onClick = onPlayClick) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color(0xFF2196F3), CircleShape)
                                .padding(12.dp)
                                .scale(scaleX = -1f, scaleY = 1f)
                        )
                    }
                }
            }
        }
    }
}


