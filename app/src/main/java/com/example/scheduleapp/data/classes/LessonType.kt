package com.example.scheduleapp.data.classes

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.scheduleapp.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LessonType (
    val color: Color,
    @StringRes val displayName: Int
) {
    @SerialName("lab")
    LAB(Color(0xFFA020F0), R.string.lab),
    @SerialName("lecture")
    LECTURE(Color(0xFF476FFF), R.string.lecture),
    @SerialName("exercise")
    EXERCISE(Color(0xFFEE54FF), R.string.exercise),
    @SerialName("seminar")
    SEMINAR(Color(0xFFFFF21D), R.string.seminar),
    @SerialName("project")
    PROJECT(Color(0xFFFFA500), R.string.project),
    @SerialName("exam")
    EXAM(Color(0xFFFF4747), R.string.exam);
}
