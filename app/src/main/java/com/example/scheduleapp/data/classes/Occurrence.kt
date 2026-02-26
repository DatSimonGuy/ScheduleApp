package com.example.scheduleapp.data.classes

import androidx.annotation.StringRes
import com.example.scheduleapp.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Occurrence {
    @SerialName("always")
    WEEKLY,
    @SerialName("once")
    ONCE,
    @SerialName("2weeks")
    EVERY_TWO,
    @SerialName("3weeks")
    EVERY_THREE,
    @SerialName("selected_dates")
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
