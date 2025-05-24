package com.project.readme.presenter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.SoundEffectConstants
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.project.readme.R
import com.project.readme.common.MainColorUtils
import com.project.readme.common.Resource
import com.project.readme.data.Book
import com.project.readme.data.UserProfile
import com.project.readme.data.genarator.LessonUtil
import com.project.readme.ui.theme.ReadMeTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.InputStream


@AndroidEntryPoint
class BookAppActivity : ComponentActivity() {

    private val viewModel: BookAppViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView

        viewModel.loadLessons(this)

        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = true // true or false as desired.
        // And then you can set any background color to the status bar.
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val books by viewModel.lesson.collectAsState()
            val profile by viewModel.profile.collectAsState()
            val favorites by viewModel.favorites.collectAsState()
            val page by viewModel.page.collectAsState()
            val navController = rememberNavController()
            ReadMeTheme {
                    // The NavHost occupies the remaining space above the bottom navigation bar
                    Box(modifier = Modifier.fillMaxSize()) {
                        val paddingValues = PaddingValues(0.dp)
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable("home") {
                                if(profile is Resource.Success) {
                                    Timber.d("profile.data -> ${profile.data}")
                                    MediumTopAppBarExample(books, profile.data, ::navigateToMainScreen, ::navigateToCover, ::onNextPage, page, ::navigateToLesson)
                                }
                            }
                            composable("results") {
                                if (false) { // this is the orig value -> favorites.isNotEmpty()
                                    FavoritesBooks(favorites, ::navigateToMainScreen)
                                } else {
                                    FavoritesScreen()
                                }
                            }
                            composable("about") {
                                Toast.makeText(LocalContext.current, "This Page is currently unavailable", Toast.LENGTH_SHORT).show()
                                //AboutPage()
                            }

                            composable("profile") {
                                ProfileScreen(::updateProfile, profile.data)
                            }
                        }

                        Column (modifier = Modifier.align(Alignment.BottomCenter)) {
                            CustomBottomNavigationBar(navController)
                        }
                }
            }
        }

    }

    private fun onNextPage() {
        viewModel.loadNextLessons(this)
    }

    private fun navigateToMainScreen(bookTitle: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("Title", bookTitle)
        startActivity(intent)
    }

    private fun navigateToCover(level: String) {
        val intent = Intent(this, ComprehensionCover::class.java)
        intent.putExtra("level", level)
        startActivity(intent)
    }

    private fun navigateToLesson() {
        val intent = Intent(this, WebviewActivity::class.java)
        startActivity(intent)
    }

    private fun updateProfile(name: String,selectedAvatar: Int) {
        viewModel.updateUserProfile(name, selectedAvatar)
        Toast.makeText(this, "Your profile has been successfully updated!", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun CustomBottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Results", Icons.AutoMirrored.Filled.List, "results"),
        BottomNavItem("About", Icons.Default.Info, "about"),
        BottomNavItem("Profile", Icons.Default.Person, "profile")
    )

    val view = LocalView.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MainColorUtils.primary)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            CustomNavItem(
                item = item,
                isSelected = currentRoute == item.route,
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun CustomNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val contentColor =
        if (isSelected) Color(android.graphics.Color.parseColor("#05453B")) else MaterialTheme.colorScheme.onPrimary

    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = contentColor
        )
        Text(
            text = item.title,
            color = contentColor,
            fontSize = 12.sp
        )
    }
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)

@Composable
fun FavoritesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No Result Found", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun ProfileScreen(kFunction4: (String, Int) -> Unit, profile: UserProfile?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NameAgeGradeScreen(kFunction4, profile)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun MediumTopAppBarExample(
    books: List<Book>,
    user: UserProfile?,
    onItemClicked: (bookTitle: String) -> Unit,
    onComprehensionCover: (String) -> Unit,
    onNextPage: () -> Unit,
    page: Int,
    onLessonClick: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // Detect scroll state to conditionally apply rounded corners
    val scrollState = scrollBehavior.state
    val isCollapsed = true // Adjust this value based on how much the header should collapse

    // Remember the state of the LazyColumn
    val listState = rememberLazyListState()
    // Box to layer the image background and content
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Scaffold for content with top bar
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MainColorUtils.primary,
                        scrolledContainerColor = MainColorUtils.primary,
                        titleContentColor = Color.White
                    ),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp) // Overall size including border
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                    .padding(3.dp) // Add padding inside the border
                            ) {
                                Image(
                                    painter = painterResource(id = user?.profilePic ?: R.drawable.unknown_user),
                                    contentDescription = "Avatar",
                                    contentScale = ContentScale.Inside,
                                    modifier = Modifier
                                        .fillMaxSize() // Fill the remaining space within the Box
                                        .clip(CircleShape)
                                )
                            }


                            Spacer(Modifier.width(8.dp))

                            Text(
                                "Hi ${user?.name.orEmpty()}!",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )
                        }

                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .clip(
                        RoundedCornerShape(
                            topStart = if (isCollapsed) 0.dp else 50.dp,
                            topEnd = if (isCollapsed) 0.dp else 50.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    ),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn (Modifier.fillMaxSize()) {
                        item {
                            SubjectButton(R.drawable.plus,"Addition", onLessonClick)
                        }
                        item {
                            SubjectButton(R.drawable.minus, "Subtraction", onLessonClick)
                        }
                        item {
                            SubjectButton(
                                R.drawable.multiplication,
                                "Multiplication",
                                onLessonClick
                            )
                        }
                        item {
                            SubjectButton(R.drawable.divide, "Division", onLessonClick)
                        }
                        item {
                            SubjectButton(R.drawable.pie_chart, "What is Fractions?", onLessonClick)
                        }
                        item {
                            SubjectButton(
                                R.drawable.pie_chart,
                                "Addition of Fractions",
                                onLessonClick
                            )
                        }
                        item {
                            SubjectButton(
                                R.drawable.pie_chart,
                                "Subtraction of Fractions",
                                onLessonClick
                            )
                        }
                        item {
                            SubjectButton(
                                R.drawable.pie_chart,
                                "Multiplication of Fractions",
                                onLessonClick
                            )
                        }
                        item {
                            SubjectButton(
                                R.drawable.pie_chart,
                                "Divition of Fractions",
                                onLessonClick
                            )
                        }
                        item {
                            SubjectButton(R.drawable.decimal, "What is Decimals?", onLessonClick)
                        }
                        item {
                            SubjectButton(
                                R.drawable.decimal,
                                "Addition of Decimals?",
                                onLessonClick
                            )
                        }
                        item {
                            SubjectButton(
                                R.drawable.decimal,
                                "Subtraction of Decimals?",
                                onLessonClick
                            )
                        }
                        item {
                            SubjectButton(
                                R.drawable.decimal,
                                "Mutliplication of Decimals?",
                                onLessonClick
                            )
                        }
                        item {
                            SubjectButton(
                                R.drawable.decimal,
                                "Division of Decimals?",
                                onLessonClick
                            )
                        }
                        item {
                            SubjectButton(R.drawable.quiz, "Take final Quiz", onLessonClick)
                        }

                        item {
                            Spacer(Modifier.height(150.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectButton(icon: Int, title: String, onLessonClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF49AFDC)),
        modifier = Modifier
            .clickable {
                onLessonClick.invoke()
            }
            .padding(top = 16.dp,start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp),
                    textAlign = TextAlign.Center,
                )

                OutlinedCard (
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF49AFDC)),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Taken",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            modifier = Modifier.padding(start = 6.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "check",
                            tint = Color.Green,
                            modifier = Modifier.size(24.dp).padding(top = 4.dp, bottom = 4.dp, end = 4.dp)
                        )
                    }



                }

            }

            CuteImage(
                icon = painterResource(id = icon),
                modifier = Modifier.padding(end = 16.dp).size(36.dp)
            )
        }
    }
}

@Composable
fun CuteImage(icon: Painter, modifier: Modifier = Modifier) {
    Image(painter = icon, contentDescription = null, modifier = modifier)
}

@Composable
fun SubjectButtonPreview() {
    SubjectButton(R.drawable.plus, "Addition", {})
}

@ExperimentalMaterial3Api
@Composable
fun FavoritesBooks(
    books: List<String>,
    onItemClicked: (bookTitle: String) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // Detect scroll state to conditionally apply rounded corners
    val scrollState = scrollBehavior.state
    // Remember the state of the LazyColumn
    val listState = rememberLazyListState()
    // Box to layer the image background and content
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Scaffold for content with top bar
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White, // Make the top bar transparent
                        scrolledContainerColor = Color.White,
                        titleContentColor = Color(android.graphics.Color.parseColor("#66cbad"))
                    ),
                    title = {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Spacer(Modifier.width(8.dp))

                            Text(
                                "Favorite Books",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color(android.graphics.Color.parseColor("#66cbad"))
                            )
                        }

                    },
                    scrollBehavior = scrollBehavior
                )
            },
            containerColor = Color.White,
        ) { innerPadding ->

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Example content (popular books, etc.)
                        item {
                            FavoritesBooksItems(books, onItemClicked)
                        }

                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FavoritesBooksItems(books: List<String>, onItemClicked: (bookTitle: String) -> Unit) {
    val view = LocalView.current

    val newList = books.toMutableList()
    if ((newList.size % 2) != 0) {
        newList.add("++")
    }
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.Center, // Align items to the start of the row
        verticalArrangement = Arrangement.spacedBy(16.dp), // Space between rows
    ) {
        newList.forEach { title ->
            Box(
                modifier = Modifier
                    .padding(8.dp) // Inner padding for spacing
                    .fillMaxWidth(0.45f) // Each item takes up 45% of the parent's width
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onItemClicked(title)
                    }
            ) {
                LessonUtil.getCover(title)?.let {
                    CompressedAsyncImage(imageResId = it)
                }
            }
        }
    }
}

@Composable
fun AboutPage() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, top = 60.dp, bottom = 200.dp, end = 16.dp)
    ) {
        // Header Section
        Text(
            text = "About Us",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Subheader with Introduction
        Text(
            text = "Empowering young minds through improved reading comprehension.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Decorative Divider
        Divider(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Project Description
        Text(
            text = """
                We are a dedicated team of future educators from Mabini Colleges, Inc., committed to supporting Grade 2 learners in developing essential reading skills. Our application combines interactive and engaging learning experiences to foster better comprehension and instill a love for reading in young minds.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Team Members Section
        Text(
            text = "Meet the Team",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TeamMembersCard()

        // Department & Course Section
        SectionWithIcon(
            icon = Icons.Default.Place,
            title = "Department",
            content = "College of Education"
        )
        SectionWithIcon(
            icon = Icons.Default.AccountBox,
            title = "Course",
            content = "Bachelor of Elementary Education"
        )

        // School Information
        SectionWithIcon(
            icon = Icons.Default.Place,
            title = "School",
            content = "Mabini Colleges, Inc."
        )

        // Mission Statement
        Text(
            text = "Our Mission",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text(
            text = "To inspire young learners to achieve their full potential by building a strong foundation in reading comprehension, paving the way for academic success and lifelong learning.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Closing Message
        Text(
            text = "Together, let’s create a brighter future, one reader at a time.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp),
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun TeamMembersCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Our Team Members",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = """
                    • Pia A. Breguiles
                    • Ronalyn Cemanes
                    • Jonalyn Octa
                    • Patricia Mae De Jesus
                    • Jilyn Santiago
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun SectionWithIcon(icon: ImageVector, title: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

fun compressImage(inputStream: InputStream, quality: Int): Bitmap? {
    val originalBitmap = BitmapFactory.decodeStream(inputStream)
    val byteArrayOutputStream = ByteArrayOutputStream()
    originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

@Composable
fun CompressedAsyncImage(
    imageResId: Int,
    quality: Int = 20, // Set quality to 20%
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val context = LocalContext.current
    val inputStream = context.resources.openRawResource(imageResId)

    // Compress the image to the desired quality
    val compressedBitmap = compressImage(inputStream, quality)

    compressedBitmap?.let {
        // Convert the compressed Bitmap to a painter that can be used by AsyncImage
        val imageRequest = ImageRequest.Builder(context)
            .data(it)
            .build()

        AsyncImage(
            model = imageRequest,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = modifier
        )
    }
}

@Composable
fun CircularProgressBar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.height(150.dp))
        CircularProgressIndicator(
            color = Color(android.graphics.Color.parseColor("#66cbad")),
            strokeWidth = 6.dp
        )
        Spacer(Modifier.height(150.dp))
    }
}

@Composable
fun CircularProgressBarPaging() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(Modifier.height(16.dp))
        CircularProgressIndicator(
            color = Color(android.graphics.Color.parseColor("#66cbad")),
            strokeWidth = 6.dp
        )

        Spacer(Modifier.height(16.dp))
    }
}