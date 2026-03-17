package com.example.scheduleapp.data.classes

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.scheduleapp.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LessonType (val color: Color) {
    @SerialName("lab")
    LAB(Color(0xFFA020F0)),
    @SerialName("lecture")
    LECTURE(Color(0xFF476FFF)),
    @SerialName("exercise")
    EXERCISE(Color(0xFFEE54FF)),
    @SerialName("seminar")
    SEMINAR(Color(0xFFFFF21D)),
    @SerialName("project")
    PROJECT(Color(0xFFFFA500)),
    @SerialName("exam")
    EXAM(Color(0xFFFF4747));

    @StringRes
    fun getDisplayName(): Int {
        return when(this) {
            LAB -> R.string.lab
            LECTURE -> R.string.lecture
            EXERCISE -> R.string.exercise
            SEMINAR -> R.string.seminar
            PROJECT -> R.string.project
            EXAM -> R.string.exam
        }
    }
}
