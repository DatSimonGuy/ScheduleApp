package com.example.scheduleapp.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance


fun textColorForBackground(background: Color): Color {
    val luminance = background.luminance()
    return if (luminance > 0.5f) Color.Black else Color.White
}