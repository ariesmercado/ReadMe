package com.project.readme.presenter

import android.content.Intent
import android.os.Bundle
import android.view.SoundEffectConstants
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.project.readme.R
import com.project.readme.data.UserProfile
import com.project.readme.ui.theme.ReadMeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : ComponentActivity() {

    private val viewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView

        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = true // true or false as desired.
        // And then you can set any background color to the status bar.
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val profile by viewModel.profile.collectAsState()
            ReadMeTheme {
                NameAgeGradeScreen(::navigateToMainScreen, profile.data)
            }
        }

    }

    private fun navigateToMainScreen(name: String,age: Int, grade: Int,selectedAvatar: Int) {
        viewModel.updateUserProfile(name, age, grade, selectedAvatar)
        val intent = Intent(this@RegisterActivity, BookAppActivity::class.java)
        intent.putExtra("USER", UserProfile(name, age, grade, selectedAvatar))
        startActivity(intent)
        finish()
    }
}

@Composable
fun NameAgeGradeScreen(
    onSubmitClicked: (name: String, age: Int, grade: Int, selectedAvatar: Int) -> Unit,
    profile: UserProfile?
)  {
    var name by remember { mutableStateOf(profile?.name.orEmpty()) }
    var age by remember { mutableStateOf(if(profile?.age == null) "" else profile.age.toString())}
    var grade by remember { mutableStateOf(if(profile?.grade == null) "" else profile.grade.toString()) }
    var selectedAvatar by remember { mutableStateOf(profile?.profilePic) } // Null initially for no avatar
    var showDialog by remember { mutableStateOf(false) } // To control dialog visibility

    val errorList: MutableSet<String> = mutableSetOf()

    // List of avatar resource IDs
    val avatars = listOf(
        R.drawable.a1,
        R.drawable.a2,
        R.drawable.a3,
        R.drawable.a4,
        R.drawable.a5,
        R.drawable.a6
    )
    val view = LocalView.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar Selection (Clickable to show dialog)
        AvatarSelector(selectedAvatar) {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            showDialog = true  // Open the dialog when avatar is clicked
        }

        // Name TextField
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            isError = errorList.any { it == "name" },
        )

        // Age TextField
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, shape = MaterialTheme.shapes.medium),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = errorList.any { it == "age" }
        )

        // Grade TextField
        OutlinedTextField(
            value = grade,
            onValueChange = { grade = it },
            label = { Text("Grade") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, shape = MaterialTheme.shapes.medium),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = errorList.any { it == "grade" }
        )

        Spacer(modifier = Modifier.height(24.dp))
        // Submit Button
        val context = LocalContext.current
        Button(
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                if(name.isBlank()) errorList.add("name") else errorList.remove("name")
                if(age.isBlank()) errorList.add("age") else errorList.remove("age")
                if(grade.isBlank()) errorList.add("grade") else errorList.remove("grade")
                if(selectedAvatar == null) {
                    errorList.add("avatar")
                    Toast.makeText(context, "Please select an Avatar", Toast.LENGTH_SHORT).show()
                }  else errorList.remove("grade")


                if(errorList.isEmpty()) onSubmitClicked(name,age.toInt(), grade.toInt(), selectedAvatar ?: - 1)
                      },
            colors = ButtonDefaults.buttonColors(containerColor = Color(android.graphics.Color.parseColor("#66cbad"))),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(
                text = "Submit",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
            )
        }

        // Avatar Selection Dialog
        if (showDialog) {
            AvatarSelectionDialog(avatars) { selected ->
                selectedAvatar = selected
                showDialog = false
            }
        }
    }
}

@Composable
fun AvatarSelector(
    selectedAvatar: Int?,
    onAvatarClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        val view = LocalView.current
        // Display either the selected avatar or a placeholder
        if (selectedAvatar != null) {
            // Display the selected avatar with border and shadow
            Box(
                modifier = Modifier
                    .size(120.dp) // Overall size including border
                    .clip(CircleShape)
                    .border(4.dp, Color(android.graphics.Color.parseColor("#66cbad")), CircleShape)
                    .clickable { onAvatarClick() } // Clickable to show the dialog
                    .padding(8.dp) // Add padding inside the border
            ) {
                Image(
                    painter = painterResource(id = selectedAvatar),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .fillMaxSize() // Fill the remaining space within the Box
                        .clip(CircleShape)
                )
            }
        } else {
            // Display a placeholder (gray circle) when no avatar is selected

            Image(
                painter = painterResource(id = R.drawable.unknown_user),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onAvatarClick()
                    } // Clickable to show the dialog
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Text displaying the selected avatar or placeholder message
        Text(
            text = if (selectedAvatar != null) "Avatar Selected" else "Select Avatar",
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun AvatarSelectionDialog(
    avatars: List<Int>,
    onAvatarSelected: (Int) -> Unit
) {
    val view = LocalView.current
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Select Avatar", style = MaterialTheme.typography.headlineMedium) },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // Display 3 avatars per row
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(avatars) { avatar ->

                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                onAvatarSelected(avatar)
                            }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = avatar),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK)}) {
                Text("Close")
            }
        }
    )
}

