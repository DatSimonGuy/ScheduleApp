package com.example.scheduleapp.elements.forms.validation

import java.time.Duration
import java.time.LocalTime

fun addLessonFormValidate(
    subject: String,
    start: LocalTime,
    end: LocalTime
): Pair<String?, String?> {
    val subjectError =
        if (subject.isBlank()) "Subject cannot be empty" else null

    val timeError = if (Duration.between(start, end).toMinutes() < 45) {
        "End time must be at least 45 minutes after the start time"
    } else null

    return subjectError to timeError
}