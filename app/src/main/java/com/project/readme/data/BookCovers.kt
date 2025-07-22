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
    THE_ANT_AND_THE_DOVE(id = 7, title = "THE ANT AND THE DOVE", cover = R.drawable.the_ant_and_the_dove),
    GOOSE(id = 8, title = "GOOSE", cover = R.drawable.goose),
    DENTIST(id = 9, title = "DENTIST", cover = R.drawable.dentist),
    SEEDS_ON_THE_MOVE(id = 10, title = "SEEDS ON THE MOVE", cover = R.drawable.seedsonthemove),
    PIZZA_FOR_POLLY(id = 11, title = "PIZZA FOR POLLY", cover = R.drawable.pizzaforpolly),
    THE_KITE(id = 12, title = "THE KITE", cover = R.drawable.thekite),
    HOME_SCHOOL(id = 13, title = "HOME SCHOOL", cover = R.drawable.homeschool),
    A_DAY_AT_THE_BEACH(id = 14, title = "A DAY AT THE BEACH", cover = R.drawable.thedayatthebeach),
    THE_COWS_AND_LIONS(id = 15, title = "THE COWS AND THE LIONS", cover = R.drawable.thecowandthelions),
    THE_ANT_AND_GRASSHOPPER(id = 16, title = "THE ANT AND THE GRASSHOPPER", cover = R.drawable.theantandthegrasshopper),
    THE_DOG_AND_THE_SPARROW(id = 17, title = "THE DOG AND THE SPARROW", cover = R.drawable.thedogandthesparrow),
    THE_CLEAN_PARK(id = 18, title = "THE CLEAN PARK", cover = R.drawable.the_clean_park),
    A_MERCHANDISE(id = 19, title = "A MERCHANDISE", cover = R.drawable.amerchandize),
    A_KING_SHEPHERD(id = 20, title = "A KING SHEPHERD", cover = R.drawable.akingshepherd),
    PHONICS(id = 21, title = "Phonics", cover = R.drawable.phonics),
    SIGHTWORDS(id = 22, title = "SIGHTWORDS", cover = R.drawable.sightwords1),
    LIZA(id = 23, title = "LIZA", cover = R.drawable.liza),
    MAX_THE_DOG(id = 24, title = "Max the Dog", cover = R.drawable.maxthedog),
    MINAS_HAT(id = 25, title = "MINAS HAT", cover = R.drawable.minashat),
    MY_CAMPING_TRIP(id = 26, title = "My Camping Trip", cover = R.drawable.mycampingtrip),
    MY_PET_DOG(id = 27, title = "My pet dog", cover = R.drawable.mypetdog),
    TED_AND_FRED(id = 28, title = "Ted and Fred", cover = R.drawable.tedandfred),
    THE_COW(id = 23, title = "The Cow", cover = R.drawable.thecow),
    THE_LITTLE_BIRD(id = 23, title = "The Little Bird", cover = R.drawable.thelittlebird)
}