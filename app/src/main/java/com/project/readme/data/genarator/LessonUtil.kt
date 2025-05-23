package com.project.readme.data.genarator

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.project.readme.data.Book
import com.project.readme.data.BookCovers.A_DAY_AT_THE_BEACH
import com.project.readme.data.BookCovers.A_KING_SHEPHERD
import com.project.readme.data.BookCovers.A_MERCHANDISE
import com.project.readme.data.BookCovers.BEN_GOES_FISHING
import com.project.readme.data.BookCovers.CAMPING
import com.project.readme.data.BookCovers.DENTIST
import com.project.readme.data.BookCovers.GOOSE
import com.project.readme.data.BookCovers.HOME_SCHOOL
import com.project.readme.data.BookCovers.LETTERS
import com.project.readme.data.BookCovers.LUCAS
import com.project.readme.data.BookCovers.PIZZA_FOR_POLLY
import com.project.readme.data.BookCovers.SEEDS_ON_THE_MOVE
import com.project.readme.data.BookCovers.SIGHT_WORDS
import com.project.readme.data.BookCovers.THE_ANT_AND_GRASSHOPPER
import com.project.readme.data.BookCovers.THE_ANT_AND_THE_DOVE
import com.project.readme.data.BookCovers.THE_CLEAN_PARK
import com.project.readme.data.BookCovers.THE_COWS_AND_LIONS
import com.project.readme.data.BookCovers.THE_DOG_AND_THE_SPARROW
import com.project.readme.data.BookCovers.THE_KITE
import com.project.readme.data.BookCovers.THIRSTY_CROW
import com.project.readme.data.Page
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object LessonUtil {

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

            // Access the lessons directory in the assets folder
            val lessonDirectories = context.assets.list("Lessons") ?: return@withContext null

            val book = lessonDirectories.find { it.lowercase() == term.lowercase() }
            val pages = mutableListOf<Page>()
            val lessonAssets = context.assets.list("Lessons/$book")?.take(6)


            // Loop through the assets in each lesson directory (Books, etc.)
            lessonAssets?.forEach { asset ->
                if (asset.endsWith(".txt")) {
                    val bookName = asset.substringBefore(".txt")

                    // Read text from the .txt file
                    val text = readTextFromAsset(context, "Lessons/$book/$asset")

                    // Load the corresponding image as Bitmap
                    val image = loadImageFromAssets(context, "Lessons/$book/${bookName}.png")

                    // Create a Page object
                    pages.add(Page(name = bookName, image = image, text = text))
                }
            }

            // Create a Book object and add it to the list
            book?.let { Book(name = it, pages = pages) }
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
            else -> null
        }
    }

    fun isAllowedToSpeak(name: String): Boolean {
        return when (name) {
            LETTERS.title -> true
            else -> false
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
}
