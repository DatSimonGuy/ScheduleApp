package com.example.scheduleapp.data.classes

import kotlinx.serialization.Serializable

@Serializable
enum class LessonType {
    LAB,
    LECTURE,
    EXERCISE,
    SEMINAR,
    PROJECT
}
