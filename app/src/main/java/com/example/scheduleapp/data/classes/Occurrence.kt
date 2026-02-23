package com.example.scheduleapp.data.classes

import kotlinx.serialization.Serializable

@Serializable
enum class Occurrence {
    WEEKLY,
    ONCE,
    EVERY_TWO,
    EVERY_THREE,
    SELECTED_DAYS
}
