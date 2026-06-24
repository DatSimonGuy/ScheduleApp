package com.example.scheduleapp.elements.schedule.parts.timetable

import androidx.annotation.StringRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.R

enum class HourHeight(
    val value: Dp,
    @StringRes val displayName: Int
) {
    SHORT(80.dp, R.string.shortHeight),
    MEDIUM(120.dp, R.string.medium),
    TALL(160.dp, R.string.tall)
}