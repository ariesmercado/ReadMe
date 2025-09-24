package com.project.readme.data

import androidx.annotation.DrawableRes
import com.project.readme.R

enum class Story(
    var id: Int,
    @DrawableRes var image: Int,
    var quiz: List<Quiz>,
    var nextStory: Story? = null
)
{
//    // Story #25
//    STORY25(id = 25, image = R.drawable.story_25, quiz = listOf(Quiz.QUIZ73, Quiz.QUIZ74, Quiz.QUIZ75), nextStory = STORY24),
//
//    // Story #24
//    STORY24(id = 24, image = R.drawable.story_24, quiz = listOf(Quiz.QUIZ70, Quiz.QUIZ71, Quiz.QUIZ72), nextStory = STORY23),

    // Story #23
    STORY23(id = 23, image = R.drawable.story_23, quiz = listOf(Quiz.QUIZ67, Quiz.QUIZ68, Quiz.QUIZ69)),

    // Story #22
    STORY22(id = 22, image = R.drawable.story_22, quiz = listOf(Quiz.QUIZ64, Quiz.QUIZ65, Quiz.QUIZ66), nextStory = STORY23),

    // Story #21
    STORY21(id = 21, image = R.drawable.story_21, quiz = listOf(Quiz.QUIZ61, Quiz.QUIZ62, Quiz.QUIZ63), nextStory = STORY22),

    // Story #20
    STORY20(id = 20, image = R.drawable.story_20, quiz = listOf(Quiz.QUIZ58, Quiz.QUIZ59, Quiz.QUIZ60), nextStory = STORY21),

    // Story #19
    STORY19(id = 19, image = R.drawable.story_19, quiz = listOf(Quiz.QUIZ55, Quiz.QUIZ56, Quiz.QUIZ57), nextStory = STORY20),

    // Story #18
    STORY18(id = 18, image = R.drawable.story_18, quiz = listOf(Quiz.QUIZ52, Quiz.QUIZ53, Quiz.QUIZ54), nextStory = STORY19),

    // Story #17
    STORY17(id = 17, image = R.drawable.story_17, quiz = listOf(Quiz.QUIZ49, Quiz.QUIZ50, Quiz.QUIZ51), nextStory = STORY18),

    // Story #16
    STORY16(id = 16, image = R.drawable.story_16, quiz = listOf(Quiz.QUIZ46, Quiz.QUIZ47, Quiz.QUIZ48), nextStory = STORY17),

    // Story #15
    STORY15(id = 15, image = R.drawable.story_15, quiz = listOf(Quiz.QUIZ43, Quiz.QUIZ44, Quiz.QUIZ45), nextStory = STORY16),

    // Story #14
    STORY14(id = 14, image = R.drawable.story_14, quiz = listOf(Quiz.QUIZ40, Quiz.QUIZ41, Quiz.QUIZ42), nextStory = STORY15),

    // Story #13
    STORY13(id = 13, image = R.drawable.story_13, quiz = listOf(Quiz.QUIZ37, Quiz.QUIZ38, Quiz.QUIZ39), nextStory = STORY14),

    // Story #12
    STORY12(id = 12, image = R.drawable.story_12, quiz = listOf(Quiz.QUIZ34, Quiz.QUIZ35, Quiz.QUIZ36), nextStory = STORY13),

    // Story #11
    STORY11(id = 11, image = R.drawable.story_11, quiz = listOf(Quiz.QUIZ31, Quiz.QUIZ32, Quiz.QUIZ33), nextStory = STORY12),

    // Story #10
    STORY10(id = 10, image = R.drawable.story_10, quiz = listOf(Quiz.QUIZ28, Quiz.QUIZ29, Quiz.QUIZ30), nextStory = STORY11),

    // Story #9
    STORY9(id = 9, image = R.drawable.story_9, quiz = listOf(Quiz.QUIZ25, Quiz.QUIZ26, Quiz.QUIZ27), nextStory = STORY10),

    // Story #8
    STORY8(id = 8, image = R.drawable.story_8, quiz = listOf(Quiz.QUIZ22, Quiz.QUIZ23, Quiz.QUIZ24), nextStory = STORY9),

    // Story #7
    STORY7(id = 7, image = R.drawable.story_7, quiz = listOf(Quiz.QUIZ19, Quiz.QUIZ20, Quiz.QUIZ21), nextStory = STORY8),

    // Story #6
    STORY6(id = 6, image = R.drawable.story_6, quiz = listOf(Quiz.QUIZ16, Quiz.QUIZ17, Quiz.QUIZ18), nextStory = STORY7),

    // Story #5
    STORY5(id = 5, image = R.drawable.story_5, quiz = listOf(Quiz.QUIZ13, Quiz.QUIZ14, Quiz.QUIZ15), nextStory = STORY6),

    // Story #4
    STORY4(id = 4, image = R.drawable.story_4, quiz = listOf(Quiz.QUIZ10, Quiz.QUIZ11, Quiz.QUIZ12), nextStory = STORY5),

    // Story #3
    STORY3(id = 3, image = R.drawable.story_3, quiz = listOf(Quiz.QUIZ7, Quiz.QUIZ8, Quiz.QUIZ9), nextStory = STORY4),

    // Story #2
    STORY2(id = 2, image = R.drawable.story_2, quiz = listOf(Quiz.QUIZ4, Quiz.QUIZ5, Quiz.QUIZ6), nextStory = STORY3),

    // Story #1
    STORY1(id = 1, image = R.drawable.story_1, quiz = listOf(Quiz.QUIZ1, Quiz.QUIZ2, Quiz.QUIZ3), nextStory = STORY2)
}



enum class Quiz(
    var id: Int,
    var question: String,
    var choices: List<String>,
    var correctAnswer: Int
) {
    // Story #1
    QUIZ1(
        id = 1,
        question = "I have ________.",
        choices = listOf("a friend", "a cousin", "a nephew"),
        1
    ),
    QUIZ2(
        id = 2,
        question = "His name is ________.",
        choices = listOf("Rick", "Greg", "Bill"),
        2
    ),
    QUIZ3(
        id = 3,
        question = "He is holding ________.",
        choices = listOf("a flag", "a banner", "a bag"),
        1
    ),

    // Story #2
    QUIZ4(
        id = 4,
        question = "I see _______ in the tree.",

        choices = listOf("an owl", "a horse", "a deer"),
        1
    ),
    QUIZ5(
        id = 5,
        question = "The owl is _______.",
        choices = listOf("red", "blue", "brown"),
        3
    ),
    QUIZ6(
        id = 6,
        question = "The owl has ______.",
        choices = listOf("big feet", "big eyes", "nice feathers"),
        2
    ),

    // Story #3
    QUIZ7(
        id = 7,
        question = "Today is ______.",
        choices = listOf("Thanksgiving", "Christmas", "Mother’s day"),
        3
    ),
    QUIZ8(
        id = 8,
        question = "I have ________.",
        choices = listOf("flowers", "a flower", "dolls"),
        1
    ),
    QUIZ9(
        id = 9,
        question = "My flowers are for ________.",
        choices = listOf("my friend", "my dad", "my mom"),
        3
    ),
    // Story #4
    QUIZ10(
        id = 10,
        question = "I have ________.",
        choices = listOf("a house", "a friend", "a pencil"),
2
    ),
    QUIZ11(
        id = 11,
        question = "Her name is ________.",
        choices = listOf("Jenny", "Brittany", "Cindy"),
        3
    ),
    QUIZ12(
        id = 12,
        question = "She has _______.",
        choices = listOf("a box of toys", "a jar of candy", "a box of chocolates"),
3
    ),

    // Story #5
    QUIZ13(
        id = 13,
        question = "Olivia _________.",
        choices = listOf("can cook", "can climb", "can write"),
3
    ),
    QUIZ14(
        id = 14,
        question = "She is ________.",
        choices = listOf("helping her mother", "cleaning her room", "writing a letter"),
        3
    ),
    QUIZ15(
        id = 15,
        question = "She will mail it _______.",
        choices = listOf("to her cousin", "to her friend", "to her grandmother"),
        3
    ),

    // Story #6
    QUIZ16(
        id = 16,
        question = "I see _______.",
        choices = listOf("my friends", "my dad", "my friend"),
        1
    ),
    QUIZ17(
        id = 17,
        question = "Rey is kicking _______.",
        choices = listOf("the leaves", "the ball", "the wall"),
        1
    ),
    QUIZ18(
        id = 18,
        question = "Joe is standing on _______.",
        choices = listOf("floors", "a tree stump", "a table"),
        2
    ),

    // Story #7
    QUIZ19(
        id = 19,
        question = "Olivia is _______.",
        choices = listOf("my teacher", "my friend", "my neighbor"),
        2
    ),
    QUIZ20(
        id = 20,
        question = "She has ________.",
        choices = listOf("a teddy bear", "a teddy bears", "dolls"),
        1
    ),
    QUIZ21(
        id = 21,
        question = "Her _____ gave it to her.",
        choices = listOf("aunt", "mom", "dad"),
        3
    ),

    // Story #8
    QUIZ22(
        id = 22,
        question = "James went to the ________.",
        choices = listOf("the bank", "the hospital", "the playground"),
        2
    ),
    QUIZ23(
        id = 23,
        question = "He saw ________.",
        choices = listOf("an ambulance", "a police car", "a shipwreck"),
        1
    ),
    QUIZ24(
        id = 24,
        question = "The ambulance was __________.",
        choices = listOf("tall", "little", "big"),
        3
    ),
    // Story #9
    QUIZ25(
        id = 25,
        question = "Larry has ______.",
        choices = listOf("a cart", "a basket", "a ball"),
        2
    ),
    QUIZ26(
        id = 26,
        question = "He is going to ______.",
        choices = listOf("to the store", "to the garden", "to the zoo"),
        2
    ),
    QUIZ27(
        id = 27,
        question = "He will _____ the eggs.",
        choices = listOf("hide", "bring", "paint"),
        1
    ),

    // Story #10
    QUIZ28(
        id = 28,
        question = "Sarah and Will are ______.",
        choices = listOf("my friends", "my friend", "cousins"),
        1
    ),
    QUIZ29(
        id = 29,
        question = "They are _______ a Christmas tree.",
        choices = listOf("bringing", "decorating", "selling"),
        2
    ),
    QUIZ30(
        id = 30,
        question = "They love ______.",
        choices = listOf("winter", "thanksgiving", "Christmas"),
        3
    ),

    // Story #11
    QUIZ31(
        id = 31,
        question = "The vampire lives ______.",
        choices = listOf("in the castle", "in the hotel", "in the house"),
        1
    ),
    QUIZ32(
        id = 32,
        question = "He wears ________.",
        choices = listOf("blue shoes", "a black cape", "a brown hat"),
        2
    ),
    QUIZ33(
        id = 33,
        question = "He has ______.",
        choices = listOf("a stove", "a car", "a candlestick"),
        3
    ),

    // Story #12
    QUIZ34(
        id = 34,
        question = "I see _____",
        choices = listOf("policeman", "a chef", "a banker"),
        2
    ),
    QUIZ35(
        id = 35,
        question = "She has ______.",
        choices = listOf("an oven", "a bike", "a dress"),
        1
    ),
    QUIZ36(
        id = 36,
        question = "She can make ______.",
        choices = listOf("a house", "spaghetti", "pizza"),
        3
    ),
    // Story #13
    QUIZ37(
        id = 37,
        question = "I see _____.",
        choices = listOf("a boy", "a girl", "a friend"),
        1
    ),
    QUIZ38(
        id = 38,
        question = "He has ______.",
        choices = listOf("a bike", "a rake", "a tire"),
        2
    ),
    QUIZ39(
        id = 39,
        question = "He is ______ the backyard.",
        choices = listOf("cleaning", "playing", "watching"),
        1
    ),

    // Story #14
    QUIZ40(
        id = 40,
        question = "Today is _______.",
        choices = listOf("Christmas", "Thanksgiving", "Wednesday"),
        2
    ),
    QUIZ41(
        id = 41,
        question = "We are eating _______.",
        choices = listOf("turkey", "pizza", "spaghetti"),
        1
    ),
    QUIZ42(
        id = 42,
        question = "We are very ______.",
        choices = listOf("happy", "sad", "full"),
        1
    ),

    // Story #15
    QUIZ43(
        id = 43,
        question = "I have a friend and his name is _______.",
        choices = listOf("Greg", "Bruce", "Larry"),
        3
    ),
    QUIZ44(
        id = 44,
        question = "He can make ______.",
        choices = listOf("a triangle", "a heart", "a square"),
        2
    ),
    QUIZ45(
        id = 45,
        question = "He is going to give it _______.",
        choices = listOf("to his friend", "to his dad", "to his mom"),
        3
    ),

    // Story #16
    QUIZ46(
        id = 46,
        question = "Rebecca is ______.",
        choices = listOf("a scuba diver", "a banker", "a zookeeper"),
        1
    ),
    QUIZ47(
        id = 47,
        question = "She is at ________.",
        choices = listOf("the bottom of the aquarium", "the bottom of the pool", "the bottom of the ocean"),
        3
    ),
    QUIZ48(
        id = 48,
        question = "She finds ______.",
        choices = listOf("the fish", "an anchor", "the treasure"),
        2
    ),
    QUIZ49(
        id = 49,
        question = "I see _____.",
        choices = listOf("a soldier", "a forest ranger", "a policeman"),
        2
    ),
    QUIZ50(
        id = 50,
        question = "She is holding ______.",
        choices = listOf("a lantern", "lanterns", "a stick"),
        1
    ),
    QUIZ51(
        id = 51,
        question = "She has ____.",
        choices = listOf("a red truck", "an airplane", "a big truck"),
        3
    ),

    // Story #18
    QUIZ52(
        id = 52,
        question = "I saw _____.",
        choices = listOf("a bird and a pig", "a dog and a cat", "a dog and a pig"),
        3
    ),
    QUIZ53(
        id = 53,
        question = "The pig was _____.",
        choices = listOf("healthy", "dirty", "clean"),
        2
    ),
    QUIZ54(
        id = 54,
        question = "The dog was _____.",
        choices = listOf("white", "dry", "wet"),
        3
    ),

    // Story #19
    QUIZ55(
        id = 55,
        question = "Diana has _____.",
        choices = listOf("a flower", "flowers", "a notebook"),
        2
    ),
    QUIZ56(
        id = 56,
        question = "Her flowers are _____.",
        choices = listOf("in the floor", "in the pot", "in the box"),
        2
    ),
    QUIZ57(
        id = 57,
        question = "Her flowers are _____.",
        choices = listOf("blue and white", "yellow and pink", "white and gray"),
        2
    ),

    // Story #20
    QUIZ58(
        id = 58,
        question = "I see _____.",
        choices = listOf("Santa", "my dad", "Brian"),
        1
    ),
    QUIZ59(
        id = 59,
        question = "He wears his _____.",
        choices = listOf("hat", "shoes", "glasses"),
        3
    ),
    QUIZ60(
        id = 60,
        question = "He is ______ a list.",
        choices = listOf("cutting", "reading", "folding"),
        2
    ),
    QUIZ61(
        id = 61,
        question = "Emma likes _____.",
        choices = listOf("to play", "to skate", "to ski"),
        2
    ),
    QUIZ62(
        id = 62,
        question = "Alex likes _____.",
        choices = listOf("to ski", "to skate", "to ride"),
        1
    ),
    QUIZ63(
        id = 63,
        question = "They are _____.",
        choices = listOf("my friend", "my friends", "my teachers"),
        1
    ),

    // Story #22
    QUIZ64(
        id = 64,
        question = "They are _____.",
        choices = listOf("in the sand", "at the bottom of the pool", "at the bottom of the ocean"),
        3
    ),
    QUIZ65(
        id = 65,
        question = "They find _____.",
        choices = listOf("a boat", "a ship", "a shipwreck"),
        3
    ),
    QUIZ66(
        id = 66,
        question = "They are looking for _______.",
        choices = listOf("treasure", "the gold", "the bag"),
        1
    ),

    // Story #23
    QUIZ67(
        id = 67,
        question = "My name is _____.",
        choices = listOf("Pamela", "Susan", "Rachel"),
        3
    ),
    QUIZ68(
        id = 68,
        question = "That is my ______ tree.",
        choices = listOf("green", "Christmas", "oak tree"),
        2
    ),
    QUIZ69(
        id = 69,
        question = "There are _____ under the tree.",
        choices = listOf("gifts", "toys", "cookies"),
        1
    ),

    //For Liza
    QUIZ70(
        id = 70,
        question = "1. Where was Liza?",
        choices = listOf("at home", "at recess", "at friends home", "at park"),
        1
    ),

    QUIZ71(
    id = 71,
    question = "2. How long was Liza on the swing?",
    choices = listOf("hour", "minutes", "seconds", "day"),
    1
    ),
    //For Max the Dog
    QUIZ72(
        id = 72,
        question = "1. What kind of pet is Max?",
        choices = listOf("Dog", "Fish", "Cat", "Goat"),
        0
    ),

    QUIZ73(
        id = 73,
        question = "2. What color is Max?",
        choices = listOf("Gray", "Brown", "White", "Yellow"),
        1
    ),
    QUIZ74(
        id = 74,
        question = "3. What does Max like to do?",
        choices = listOf("Sleep", "Dance", "Run and Jump", "Sing"),
        2
    ),

    // Minas Hat
    QUIZ75(
        id = 75,
        question = "1. Who has a hat?",
        choices = listOf("Ana", "Mina"),
        1
    ),

    QUIZ76(
        id = 76,
        question = "2. What did Pat give Mina?",
        choices = listOf("Hat", "Cat",),
        0
    ),

    //My Camping Trip
    QUIZ77(
        id = 77,
        question = "1. Where am I going?",
        choices = listOf("School", "Farm", "Camping", "Park"),
        2
    ),

    QUIZ78(
        id = 78,
        question = "2. Where will we sleep?",
        choices = listOf("House", "Tent", "Lake", "Church"),
        1
    ),
    QUIZ79(
        id = 79,
        question = "3. What will we do?",
        choices = listOf("Swim", "Sleep","Jogging","Dance"),
        0
    ),

    // My Pet Dog
    QUIZ80(
        id = 80,
        question = "1. What is my pet?",
        choices = listOf("Cat", "Goat", "Bird", "Dog"),
        3
    ),

    QUIZ81(
        id = 81,
        question = "2. What do we love to do?",
        choices = listOf("to swim", "to play"),
        1
    ),

    // Ted and Fred
    QUIZ82(
        id = 82,
        question = "1.What kind of anim al is Ted and Fred?",
        choices = listOf("Ant", "Dog"),
        0
    ),

    QUIZ83(
        id = 83,
        question = "2. Where do they live?",
        choices = listOf("Tree", "Sand"),
        1
    ),

    // The Cow
    QUIZ84(
        id = 84,
        question = "1. Where did I go?",
        choices = listOf("Church", "Plaza", "Farm", "Park"),
        2
    ),

    QUIZ85(
        id = 85,
        question = "2. What did I see?",
        choices = listOf("Cow", "Horse", "Chicken", "Pig"),
        0
    ),

    QUIZ86(
        id = 86,
        question = "3. What is the color of the cow?",
        choices = listOf("Brown", "Black", "White and Black", "Gray"),
        2
    ),

    //The Little Bird
    QUIZ87(
        id = 87,
        question = "1. Where is the bird?",
        choices = listOf("Park", "Tree", "Grass", "River"),
        1
    ),

    QUIZ88(
        id = 88,
        question = "2. What did I see?",
        choices = listOf("Cow", "Horse", "Chicken", "Pig"),
        1
    ),

    QUIZ89(
        id = 89,
        question = "3. What is the color of the cow?",
        choices = listOf("Brown", "Black", "White and Black", "Gray"),
        2
    ),
}

