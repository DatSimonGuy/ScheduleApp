package com.example.scheduleapp.elements.forms.validation

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scheduleapp.R
import java.time.Duration
import java.time.LocalTime

fun addLessonFormValidate(
    subject: String,
    start: LocalTime,
    end: LocalTime,
    teacherMail: String,
    context: Context
): Triple<String?, String?, String?> {
    val subjectError =
        if (subject.isBlank()) context.getString(R.string.subjectError) else null

    val timeError = if (Duration.between(start, end).toMinutes() < 45) {
        context.getString(R.string.timeError)
    } else null

    val emailError = if(!teacherMail.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(teacherMail).matches()) {
        context.getString(R.string.emailError)
    } else null

    return Triple(subjectError, timeError, emailError)
}