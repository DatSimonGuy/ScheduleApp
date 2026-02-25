package com.example.scheduleapp.data.classes

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.scheduleapp.R
import kotlinx.serialization.Serializable

@Serializable
enum class LessonType (val color: Color) {
    LAB(Color(0xFFA020F0)),
    LECTURE(Color.Blue),
    EXERCISE(Color.Magenta),
    SEMINAR(Color.Green),
    PROJECT(Color(0xFFFFA500));

    @StringRes
    fun getDisplayName(): Int {
        return when(this) {
            LAB -> R.string.lab
            LECTURE -> R.string.lecture
            EXERCISE -> R.string.exercise
            SEMINAR -> R.string.seminar
            PROJECT -> R.string.project
        }
    }
}
