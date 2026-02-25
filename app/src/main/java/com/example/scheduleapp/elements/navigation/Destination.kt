package com.example.scheduleapp.elements.navigation

import androidx.annotation.StringRes
import com.example.scheduleapp.R
import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@Serializable
sealed class Destination(
    @StringRes val displayName: Int?,
) {
    @Serializable
    object Home : Destination(R.string.home)

    @Serializable
    object Schedule : Destination(R.string.schedule)

    @Serializable
    object Settings : Destination(R.string.settings)

    companion object {
        val all = listOf(Home, Schedule, Settings)
    }
}

@Serializable
sealed class SettingsDestination(
): Destination(null) {
    @Serializable
    object SchedulesSettings: SettingsDestination()
    @Serializable
    object AppearanceSettings: SettingsDestination()
    @Serializable
    object AboutSettings: SettingsDestination()
}

@Serializable
sealed class ScheduleDestination(
): Destination(null) {
    @Serializable
    data class LessonScreen(val scheduleName: String, val lessonId: String): ScheduleDestination()
}
