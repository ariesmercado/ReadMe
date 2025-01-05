package com.project.readme.data

import androidx.annotation.DrawableRes
import com.project.readme.R

enum class BookCovers(val id: Int, val title: String, @DrawableRes val cover: Int) {
    LETTERS(id = 1, title = "Letters", cover = R.drawable.letters),
    BEN_GOES_FISHING(id = 2, title = "BEN GOES FISHING", cover = R.drawable.ben_goes_fishing),
    CAMPING(id = 3, title = "CAMPING", cover = R.drawable.camping),
    LUCAS(id = 4, title = "LUCAS", cover = R.drawable.lucas),
    SIGHT_WORDS(id = 5, title = "SIGHT WORDS", cover = R.drawable.sight_words),
    THIRSTY_CROW(id = 6, title = "THIRSTY CROW", cover = R.drawable.trirsty_crow),
}