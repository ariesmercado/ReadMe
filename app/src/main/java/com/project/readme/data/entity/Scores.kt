package com.project.readme.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Scores(
    @PrimaryKey(autoGenerate = false)
    val title: String = "",
    val score: Int? = 0
)