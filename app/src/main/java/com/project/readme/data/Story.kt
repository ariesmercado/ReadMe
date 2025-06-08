package com.project.readme.data

import androidx.annotation.DrawableRes
import com.project.readme.R

enum class Story(
    var id: Int,
    @DrawableRes var image: Int? = null,
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
    STORY23(id = 23, quiz = listOf(Quiz.QUIZ67, Quiz.QUIZ68, Quiz.QUIZ69)),

    // Story #22
    STORY22(id = 22, quiz = listOf(Quiz.QUIZ64, Quiz.QUIZ65, Quiz.QUIZ66), nextStory = STORY23),

    // Story #21
    STORY21(id = 21, quiz = listOf(Quiz.QUIZ61, Quiz.QUIZ62, Quiz.QUIZ63), nextStory = STORY22),

    // Story #20
    STORY20(id = 20, quiz = listOf(Quiz.QUIZ58, Quiz.QUIZ59, Quiz.QUIZ60), nextStory = STORY21),

    // Story #19
    STORY19(id = 19, quiz = listOf(Quiz.QUIZ55, Quiz.QUIZ56, Quiz.QUIZ57), nextStory = STORY20),

    // Story #18
    STORY18(id = 18, quiz = listOf(Quiz.QUIZ52, Quiz.QUIZ53, Quiz.QUIZ54), nextStory = STORY19),

    // Story #17
    STORY17(id = 17, quiz = listOf(Quiz.QUIZ49, Quiz.QUIZ50, Quiz.QUIZ51), nextStory = STORY18),

    // Story #16
    STORY16(id = 16, quiz = listOf(Quiz.QUIZ46, Quiz.QUIZ47, Quiz.QUIZ48), nextStory = STORY17),

    // Story #15
    STORY15(id = 15, quiz = listOf(Quiz.QUIZ43, Quiz.QUIZ44, Quiz.QUIZ45), nextStory = STORY16),

    // Story #14
    STORY14(id = 14, quiz = listOf(Quiz.QUIZ40, Quiz.QUIZ41, Quiz.QUIZ42), nextStory = STORY15),

    // Story #13
    STORY13(id = 13, quiz = listOf(Quiz.QUIZ37, Quiz.QUIZ38, Quiz.QUIZ39), nextStory = STORY14),

    // Story #12
    STORY12(id = 12, quiz = listOf(Quiz.QUIZ34, Quiz.QUIZ35, Quiz.QUIZ36), nextStory = STORY13),

    // Story #11
    STORY11(id = 11, quiz = listOf(Quiz.QUIZ31, Quiz.QUIZ32, Quiz.QUIZ33), nextStory = STORY12),

    // Story #10
    STORY10(id = 10, quiz = listOf(Quiz.QUIZ28, Quiz.QUIZ29, Quiz.QUIZ30), nextStory = STORY11),

    // Story #9
    STORY9(id = 9, quiz = listOf(Quiz.QUIZ25, Quiz.QUIZ26, Quiz.QUIZ27), nextStory = STORY10),

    // Story #8
    STORY8(id = 8, quiz = listOf(Quiz.QUIZ22, Quiz.QUIZ23, Quiz.QUIZ24), nextStory = STORY9),

    // Story #7
    STORY7(id = 7, quiz = listOf(Quiz.QUIZ19, Quiz.QUIZ20, Quiz.QUIZ21), nextStory = STORY8),

    // Story #6
    STORY6(id = 6, quiz = listOf(Quiz.QUIZ16, Quiz.QUIZ17, Quiz.QUIZ18), nextStory = STORY7),

    // Story #5
    STORY5(id = 5, quiz = listOf(Quiz.QUIZ13, Quiz.QUIZ14, Quiz.QUIZ15), nextStory = STORY6),

    // Story #4
    STORY4(id = 4, quiz = listOf(Quiz.QUIZ10, Quiz.QUIZ11, Quiz.QUIZ12), nextStory = STORY5),

    // Story #3
    STORY3(id = 3, quiz = listOf(Quiz.QUIZ5, Quiz.QUIZ9), nextStory = STORY4),

    // Story #2
    STORY2(id = 2, quiz = listOf(
        Quiz.QUIZ2,
        Quiz.QUIZ3,
        Quiz.QUIZ4,
        Quiz.QUIZ5,
        Quiz.QUIZ6,
        Quiz.QUIZ7,
        Quiz.QUIZ8,
        Quiz.QUIZ9), nextStory = STORY3),

    // Story #1
    STORY1(id = 1, image = R.drawable.addition_question1, quiz = listOf(Quiz.QUIZ1), nextStory = STORY2)
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
        question = "Which of the following equations represents the total number of circles in the figure above?",
        choices = listOf("6 + 7  = 13", "8 + 6 \u200E = 14", "8 + 7 \u200E = 15", "7 + 5 \u200E = 12"),
        1
    ),
    QUIZ2(
        id = 2,
        question = "What is the sum of 700 and 136?",
        choices = listOf("636", "664", "736", "836"),
        2
    ),
    QUIZ3(
        id = 3,
        question = "Find the value of 4 hundreds + 26 tens?",
        choices = listOf("426", "606", "660", "4026"),
        1
    ),

    // Story #2
    QUIZ4(
        id = 4,
        question = "Which of the following is the sum of the smallest and the greatest two-digit numbers?",

        choices = listOf("19", "99", "100","109"),
        1
    ),
    QUIZ5(
        id = 5,
        question = "1385 girls and 432 boys took part in an art competition. Find the total number of participants.\n",
        choices = listOf("935", "1817", "2165", "None of these"),
        3
    ),
    QUIZ6(
        id = 6,
        question = "Which property is shown by the addition sentence 11 + 0 \u200E = 11",
        choices = listOf("Associative property", "Identity Property", "Additive Property", "Distributive Property"),
        2
    ),

    // Story #3
    QUIZ7(
        id = 7,
        question = "Find 5 + 7 + 9",
        choices = listOf("20", "12", "14", "21"),
        3
    ),
    QUIZ8(
        id = 8,
        question = "A bouquet has 36 red roses and 18 white roses. How many roses does the bouquet have in all?",
        choices = listOf("54", "72", "36", "108"),
        1
    ),
    QUIZ9(
        id = 9,
        question = "Bus A has 45 passengers, Bus B has 56 passengers and Bus C has 32 passengers. How many passengers are on Bus A and Bus C?",
        choices = listOf("101", "88", "77", "133"),
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
    )

}

