package com.project.readme.presenter

import android.content.Intent
import android.os.Build
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.readme.R
import com.project.readme.common.Resource
import com.project.readme.data.Book
import com.project.readme.data.UserProfile
import com.project.readme.data.genarator.LessonUtil
import com.project.readme.ui.theme.ReadMeTheme
import dagger.hilt.android.AndroidEntryPoint


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
                                    MediumTopAppBarExample(books, profile.data, ::navigateToMainScreen)
                                }

                            }
                            composable("favorites") {
                                FavoritesScreen()
                            }
                            composable("profile") {
                                ProfileScreen(::updateProfile, profile.data)
                            }
                        }

                        Column (modifier = Modifier.align(Alignment.BottomCenter)) {
                            CustomBottomNavigationBar(navController,wic)
                        }
                }
            }
        }
    }

    private fun navigateToMainScreen(bookTitle: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("Title", bookTitle)
        startActivity(intent)
    }

    private fun updateProfile(name: String,age: Int, grade: Int,selectedAvatar: Int) {
        viewModel.updateUserProfile(name, age, grade, selectedAvatar)
        Toast.makeText(this, "Your profile has been successfully updated!", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun CustomBottomNavigationBar(navController: NavController, wic: WindowInsetsControllerCompat) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Favorites", Icons.Default.Favorite, "favorites"),
        BottomNavItem("Profile", Icons.Default.Person, "profile")
    )

    val view = LocalView.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(android.graphics.Color.parseColor("#66cbad")))
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
        Text("Favorites Screen")
    }
}

@Composable
fun ProfileScreen(kFunction4: (String, Int, Int, Int) -> Unit, profile: UserProfile?) {
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
    onItemClicked: (bookTitle: String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // Detect scroll state to conditionally apply rounded corners
    val scrollState = scrollBehavior.state
    val isCollapsed = scrollState.collapsedFraction > 0.5f // Adjust this value based on how much the header should collapse

    // Remember the state of the LazyColumn
    val listState = rememberLazyListState()
    // Box to layer the image background and content
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.readme_bg), // Replace with your image resource
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.book_for_bg), // Replace with your image resource
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(.8f),
            contentScale = ContentScale.FillWidth
        )

        // Scaffold for content with top bar
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            topBar = {
                MediumTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (isCollapsed) Color.White else Color.Transparent, // Make the top bar transparent
                        scrolledContainerColor = if (isCollapsed) Color.White else Color.Transparent,
                        titleContentColor = Color(android.graphics.Color.parseColor("#66cbad"))
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
                                "Hello ${user?.name.orEmpty()}!",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = if (!isCollapsed) Color.White else Color(android.graphics.Color.parseColor("#66cbad"))
                            )
                        }

                    },
                    scrollBehavior = scrollBehavior
                )
            },
            containerColor = if (isCollapsed) Color.White else Color.Transparent,
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
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Search Bar
                        stickyHeader {
                            Column(modifier = Modifier.background(color = Color.White)) {
                                Spacer(modifier = Modifier.height(if (isCollapsed) 0.dp else 20.dp))
                                SearchBar()
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        // Example content (popular books, etc.)
                        item {
                            ListBooks(books, onItemClicked)
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

@Composable
fun SearchBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                Color(android.graphics.Color.parseColor("#F4F4F4")),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = "Search Icon")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Search book",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListBooks(books: List<Book>, onItemClicked: (bookTitle: String) -> Unit) {
    val view = LocalView.current
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center, // Center items horizontally
        verticalArrangement = Arrangement.spacedBy(16.dp) // Space between rows
    ) {
        books.forEach { coverResId ->
            Box(
                modifier = Modifier
                    .padding(8.dp) // Inner padding for spacing
                    .fillMaxWidth(0.45f) // Each item takes up 45% of the parent's width
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onItemClicked(coverResId.name)
                    }
            ) {
                LessonUtil.getCover(coverResId.name)?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth, // Crop to fill the space while maintaining aspect ratio
                        modifier = Modifier.fillMaxSize() // Fill the container size
                    )
                }
            }
        }
    }
}


