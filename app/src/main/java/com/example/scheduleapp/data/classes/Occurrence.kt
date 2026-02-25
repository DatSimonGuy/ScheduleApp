package com.example.scheduleapp.data.classes

import androidx.annotation.StringRes
import com.example.scheduleapp.R
import kotlinx.serialization.Serializable

@Serializable
enum class Occurrence {
    WEEKLY,
    ONCE,
    EVERY_TWO,
    EVERY_THREE,
    SELECTED_DAYS;

    @StringRes
    fun getDisplayName(): Int {
        return when(this) {
            WEEKLY -> R.string.weekly
            ONCE -> R.string.once
            EVERY_TWO -> R.string.everyTwo
            EVERY_THREE -> R.string.everyThree
            SELECTED_DAYS -> R.string.selectedDays
        }
    }
}
