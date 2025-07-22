package com.project.readme.data

enum class Quizzes(
    var id: Int,
    var title: String,
    var quiz: List<Quiz>,
) {
    QuizzesLIZA(id = 1, title = "LIZA", quiz = listOf(Quiz.QUIZ70, Quiz.QUIZ71)),
    QuizzesMAX_THE_DOG(id = 2, title = "Max the Dog", quiz = listOf(Quiz.QUIZ72, Quiz.QUIZ73,Quiz.QUIZ74)),
    QuizzesMINAS_HAT(id = 3, title = "MINAS HAT", quiz = listOf(Quiz.QUIZ75, Quiz.QUIZ76)),
    QuizzesMY_CAMPING_TRIP(id = 4, title = "My Camping Trip", quiz = listOf(Quiz.QUIZ77, Quiz.QUIZ78, Quiz.QUIZ79)),
    QuizzesMY_PET_DOG(id = 5, title = "My pet dog", quiz = listOf(Quiz.QUIZ80, Quiz.QUIZ81)),
    QuizzesTED_AND_FRED(id = 5, title = "Ted and Fred", quiz = listOf(Quiz.QUIZ82, Quiz.QUIZ83)),
    QuizzesTHE_COW(id = 23, title = "The Cow", quiz = listOf(Quiz.QUIZ84, Quiz.QUIZ85, Quiz.QUIZ86)),
    QuizzesTHE_LITTLE_BIRD(id = 23, title = "The Little Bird", quiz = listOf(Quiz.QUIZ87, Quiz.QUIZ88, Quiz.QUIZ89))
}