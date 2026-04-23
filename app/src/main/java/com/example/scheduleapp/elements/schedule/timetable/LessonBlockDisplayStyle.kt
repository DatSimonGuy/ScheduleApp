package com.example.scheduleapp.elements.schedule.timetable

import androidx.annotation.StringRes
import com.example.scheduleapp.R

enum class LessonBlockDisplayStyle(
    @StringRes val displayName: Int
) {
    COMPACT(R.string.compact),
    NORMAL(R.string.normal),
    EXTENDED(R.string.extended);
}