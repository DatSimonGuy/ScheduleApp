package com.example.scheduleapp.elements.timetable

enum class LessonBlockDisplayStyle {
    COMPACT,
    NORMAL,
    EXTENDED;

    companion object {
        fun from(value: Int): LessonBlockDisplayStyle {
            return when(value) {
                0 -> COMPACT
                1 -> NORMAL
                2 -> EXTENDED
                else -> NORMAL
            }
        }
    }
}