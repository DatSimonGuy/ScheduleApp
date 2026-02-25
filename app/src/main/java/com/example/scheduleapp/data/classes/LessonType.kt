package com.example.scheduleapp.data.classes

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
enum class LessonType (val color: Color) {
    LAB(Color(0xA020F0)),
    LECTURE(Color.Blue),
    EXERCISE(Color.Magenta),
    SEMINAR(Color.Green),
    PROJECT(Color(0xFFA500))
}
