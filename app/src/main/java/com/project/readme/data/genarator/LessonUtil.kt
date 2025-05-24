package com.project.readme.data.genarator

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.project.readme.data.Book
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


    // Helper function to read text from asset
    fun readTextFromAsset(context: Context, assetPath: String): String {
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
