package com.project.readme.data.genarator

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.project.readme.R
import com.project.readme.data.Book
import com.project.readme.data.BookCovers.*
import com.project.readme.data.Quizzes.*
import com.project.readme.data.Page
import com.project.readme.data.Quizzes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object LessonUtil {
    suspend fun loadFirstLessonsFromAssets(context: Context): List<Book> {
        return withContext(Dispatchers.IO) {
            val books = mutableListOf<Book>()

            // Access the lessons directory in the assets folder
            val lessonDirectories = context.assets.list("FirstLessons") ?: return@withContext emptyList()

            // Loop through the directories (Letters, Books, etc.)
            for (lessonDir in lessonDirectories) {
                val pages = mutableListOf<Page>()
                val lessonAssets = context.assets.list("FirstLessons/$lessonDir")

                // Loop through the assets in each lesson directory (Books, etc.)
                lessonAssets?.forEach { asset ->
                    if (asset.endsWith(".png")) {
                        val bookName = asset.substringBefore(".png")
                        val textFile = "$bookName.txt"

                        // Read text from the .txt file
                        val text = readTextFromAsset(context, "FirstLessons/$lessonDir/$textFile")

                        // Load the corresponding image as Bitmap
                        val image =
                            loadImageFromAssets(context, "FirstLessons/$lessonDir/${bookName}.png")

                        // Create a Page object
                        pages.add(Page(name = bookName, image = image, text = text))
                    }
                }

                // Create a Book object and add it to the list
                books.add(Book(name = lessonDir, pages = pages))
            }



            // Return the result based on the 'page' parameter
            books.map {
                val regex = "\\d+".toRegex()
                Book(
                    name = it.name,
                    pages = it.pages.sortedBy { page -> regex.find(page.name)?.value?.toIntOrNull() }
                )
            }
        }
    }

    suspend fun loadSecondLessonsFromAssets(context: Context): List<Book> {
        return withContext(Dispatchers.IO) {
            val books = mutableListOf<Book>()

            // Access the lessons directory in the assets folder
            val lessonDirectories = context.assets.list("SecondLessons") ?: return@withContext emptyList()

            // Loop through the directories (Letters, Books, etc.)
            for (lessonDir in lessonDirectories) {
                val pages = mutableListOf<Page>()
                val lessonAssets = context.assets.list("SecondLessons/$lessonDir")

                // Loop through the assets in each lesson directory (Books, etc.)
                lessonAssets?.forEach { asset ->
                    if (asset.endsWith(".png")) {
                        val bookName = asset.substringBefore(".png")
                        val textFile = "$bookName.txt"

                        // Read text from the .txt file
                        val text = readTextFromAsset(context, "SecondLessons/$lessonDir/$textFile")

                        // Load the corresponding image as Bitmap
                        val image =
                            loadImageFromAssets(context, "SecondLessons/$lessonDir/${bookName}.png")

                        // Create a Page object
                        pages.add(Page(name = bookName, image = image, text = text))
                    }

                }

                // Create a Book object and add it to the list
                books.add(Book(name = lessonDir, pages = pages))
            }



            // Return the result based on the 'page' parameter
            books.map {
                val regex = "\\d+".toRegex()
                Book(
                    name = it.name,
                    pages = it.pages.sortedBy { page -> regex.find(page.name)?.value?.toIntOrNull() }
                )
            }
        }
    }

    suspend fun loadLessonsFromAssets(context: Context, page: Int = 0): List<Book> {
        return withContext(Dispatchers.IO) {
            val books = mutableListOf<Book>()

            // Access the lessons directory in the assets folder
            val lessonDirectories = context.assets.list("Lessons") ?: return@withContext emptyList()

            // Loop through the directories (Letters, Books, etc.)
            for (lessonDir in lessonDirectories) {
                val pages = mutableListOf<Page>()
                val lessonAssets = context.assets.list("Lessons/$lessonDir")

                // Loop through the assets in each lesson directory (Books, etc.)
                lessonAssets?.forEach { asset ->
                    if (asset.endsWith(".txt")) {
                        val bookName = asset.substringBefore(".txt")

                        // Read text from the .txt file
                        val text = readTextFromAsset(context, "Lessons/$lessonDir/$asset")

                        // Load the corresponding image as Bitmap
                        val image =
                            loadImageFromAssets(context, "Lessons/$lessonDir/${bookName}.png")

                        // Create a Page object
                        pages.add(Page(name = bookName, image = image, text = text))
                    }
                }

                // Create a Book object and add it to the list
                books.add(Book(name = lessonDir, pages = pages))
            }

            // Return the result based on the 'page' parameter
            when (page) {
                0 -> {
                    books.arrangePages()
                }

                1 -> {
                    books.arrangePages().take(6)
                }

                2 -> {
                    books.arrangePages().subList(6, 12)
                }

                3 -> {
                    books.arrangePages().subList(13, 17)
                }

                else -> {
                    emptyList()
                }
            }
        }
    }

    suspend fun loadBookFromAssets(context: Context, term: String): Book? {
        return withContext(Dispatchers.IO) {

            val path = if(term.contains(":")) { term.split(":")[1] } else { "Lessons" }
            val formattedTerm = if(term.contains(":")) { term.split(":")[0] } else { term }

            // Access the lessons directory in the assets folder
            val lessonDirectories = context.assets.list(path) ?: return@withContext null

            val book = lessonDirectories.find { it.lowercase() == formattedTerm.lowercase() }
            val pages = mutableListOf<Page>()
            val lessonAssets = context.assets.list("$path/$book")


            // Loop through the assets in each lesson directory (Books, etc.)
            lessonAssets?.forEach { asset ->
                if (asset.endsWith(".png")) {
                    val bookName = asset.substringBefore(".png")
                    val textFile = "$bookName.txt"

                    // Read text from the .txt file
                    val text = readTextFromAsset(context, "$path/$book/$textFile")

                    // Load the corresponding image as Bitmap
                    val image = loadImageFromAssets(context, "$path/$book/${bookName}.png")

                    // Create a Page object
                    pages.add(Page(name = bookName, image = image, text = text))
                }
            }

            // Create a Book object and add it to the list
            val regex = "\\d+".toRegex()
            book?.let {
                val sortedPages = pages.sortedBy { page ->
                    regex.find(page.name)?.value?.toIntOrNull() ?: 0
                }
                Book(
                    name = it,
                    pages = sortedPages
                )
            }
        }
    }

    private fun List<Book>.arrangePages(): List<Book> {
        val books = this.filterNot { it.name in unavailableBooks() }
        return books.map {
            val regex = "\\d+".toRegex()
            Book(
                name = it.name,
                pages = it.pages.sortedBy { page -> regex.find(page.name)?.value?.toIntOrNull() }
            )
        }
    }

    fun unavailableBooks(): List<String> {
        return listOf(
            THE_DOG_AND_THE_SPARROW.title,
            SEEDS_ON_THE_MOVE.title
        )
    }

    fun getCover(name: String): Int? {
        return when (name) {
            LETTERS.title -> LETTERS.cover
            BEN_GOES_FISHING.title -> BEN_GOES_FISHING.cover
            CAMPING.title -> CAMPING.cover
            LUCAS.title -> LUCAS.cover
            SIGHT_WORDS.title -> SIGHT_WORDS.cover
            THIRSTY_CROW.title -> THIRSTY_CROW.cover
            THE_ANT_AND_THE_DOVE.title -> THE_ANT_AND_THE_DOVE.cover
            GOOSE.title -> GOOSE.cover
            DENTIST.title -> DENTIST.cover
            SEEDS_ON_THE_MOVE.title -> SEEDS_ON_THE_MOVE.cover
            PIZZA_FOR_POLLY.title -> PIZZA_FOR_POLLY.cover
            THE_KITE.title -> THE_KITE.cover
            HOME_SCHOOL.title -> HOME_SCHOOL.cover
            A_DAY_AT_THE_BEACH.title -> A_DAY_AT_THE_BEACH.cover
            THE_COWS_AND_LIONS.title -> THE_COWS_AND_LIONS.cover
            THE_ANT_AND_GRASSHOPPER.title -> THE_ANT_AND_GRASSHOPPER.cover
            THE_DOG_AND_THE_SPARROW.title -> THE_DOG_AND_THE_SPARROW.cover
            THE_CLEAN_PARK.title -> THE_CLEAN_PARK.cover
            A_MERCHANDISE.title -> A_MERCHANDISE.cover
            A_KING_SHEPHERD.title -> A_KING_SHEPHERD.cover
            PHONICS.title -> PHONICS.cover
            SIGHTWORDS.title -> SIGHTWORDS.cover
            LIZA.title -> LIZA.cover
            MAX_THE_DOG.title -> MAX_THE_DOG.cover
            MINAS_HAT.title -> MINAS_HAT.cover
            MY_CAMPING_TRIP.title -> MY_CAMPING_TRIP.cover
            MY_PET_DOG.title -> MY_PET_DOG.cover
            TED_AND_FRED.title -> TED_AND_FRED.cover
            THE_COW.title -> THE_COW.cover
            THE_LITTLE_BIRD.title -> THE_LITTLE_BIRD.cover
            else -> null
        }
    }

    fun getQuizzes(title: String): Quizzes? {
        return when (title) {
            QuizzesLIZA.title -> QuizzesLIZA
            QuizzesMAX_THE_DOG.title -> QuizzesMAX_THE_DOG
            QuizzesMINAS_HAT.title -> QuizzesMINAS_HAT
            QuizzesMY_CAMPING_TRIP.title -> QuizzesMY_CAMPING_TRIP
            QuizzesMY_PET_DOG.title -> QuizzesMY_PET_DOG
            QuizzesTED_AND_FRED.title -> QuizzesTED_AND_FRED
            QuizzesTHE_COW.title -> QuizzesTHE_COW
            QuizzesTHE_LITTLE_BIRD.title -> QuizzesTHE_LITTLE_BIRD
            else -> null
        }
    }

    fun isAllowedToSpeak(name: String): Boolean {
        return when (name) {
            LETTERS.title -> true
            else -> false
        }
    }
    fun getLessonType(title: String): String {
        return when (title) {
            QuizzesLIZA.title,
            QuizzesMAX_THE_DOG.title,
            QuizzesMINAS_HAT.title,
            QuizzesMY_CAMPING_TRIP.title,
            QuizzesMY_PET_DOG.title,
            QuizzesTED_AND_FRED.title,
            QuizzesTHE_COW.title,
            QuizzesTHE_LITTLE_BIRD.title -> "SecondLessons"
            LETTERS.title,
            PHONICS.title,
            SIGHTWORDS.title -> "SecondLessons"
            else -> ""
        }
    }


    // Helper function to read text from asset
    private fun readTextFromAsset(context: Context, assetPath: String): String {
        return try {
            val inputStream = context.assets.open(assetPath)
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            ""
        }
    }

    // Helper function to load an image as Bitmap from assets
    private fun loadImageFromAssets(context: Context, assetPath: String): Bitmap? {
        return try {
            val inputStream = context.assets.open(assetPath)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null // Return null if the image can't be loaded
        }
    }

    fun getRawResIdByName(fileName: String): Int? {
        return try {
            val resId = R.raw::class.java.getDeclaredField(fileName).getInt(null)
            resId
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
