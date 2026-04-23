package com.example.scheduleapp.data.classes

import androidx.annotation.StringRes
import com.example.scheduleapp.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Occurrence (
    @StringRes val displayName: Int
) {
    @SerialName("always")
    WEEKLY(R.string.weekly),
    @SerialName("once")
    ONCE(R.string.once),
    @SerialName("2weeks")
    EVERY_TWO(R.string.everyTwo),
    @SerialName("3weeks")
    EVERY_THREE(R.string.everyThree),
    @SerialName("selected")
    SELECTED_DAYS(R.string.selectedDays);
}
