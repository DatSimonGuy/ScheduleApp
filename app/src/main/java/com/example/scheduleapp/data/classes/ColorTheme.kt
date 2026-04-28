package com.example.scheduleapp.data.classes

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.fromColorLong
import androidx.compose.ui.graphics.toColorLong
import com.example.scheduleapp.R
import com.example.scheduleapp.data.datastore.themeDataStore

enum class ColorTheme(
    @StringRes val displayName: Int
) {
    DEFAULT(R.string.defaultColorTheme),
    MATERIAL3(R.string.material3ColorTheme),
    PRIMARY(R.string.primaryTheme),
    CUSTOM(R.string.custom)
}

@Composable
fun getColors(colorTheme: ColorTheme, context: Context): Map<LessonType, Color> {
    val customColors by context.themeDataStore.data
        .collectAsState(initial = LessonType.entries.associateWith { it.color.toColorLong() })
    return when(colorTheme) {
        ColorTheme.DEFAULT -> {
            LessonType.entries.associateWith { it.color }
        }
        ColorTheme.MATERIAL3 -> {
            LessonType.entries.associateWith {
                when(it) {
                    LessonType.LECTURE -> MaterialTheme.colorScheme.primary
                    LessonType.LAB -> MaterialTheme.colorScheme.secondary
                    LessonType.EXERCISE -> MaterialTheme.colorScheme.tertiary
                    LessonType.EXAM -> MaterialTheme.colorScheme.primaryContainer
                    LessonType.PROJECT -> MaterialTheme.colorScheme.secondaryContainer
                    LessonType.SEMINAR -> MaterialTheme.colorScheme.tertiaryContainer
                }
            }
        }
        ColorTheme.PRIMARY -> {
            LessonType.entries.associateWith {
                MaterialTheme.colorScheme.primary
            }
        }
        ColorTheme.CUSTOM -> {
            customColors.mapValues { Color.fromColorLong(it.value) }
        }
    }
}