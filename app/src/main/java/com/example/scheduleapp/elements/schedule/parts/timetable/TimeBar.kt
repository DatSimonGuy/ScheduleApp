package com.example.scheduleapp.elements.schedule.parts.timetable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun TimeBar(
    lineColor: Color = MaterialTheme.colorScheme.primary,
    topOffset: Dp,
    circleRadius: Float = 8f,
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.dp)
            .zIndex(2.0f)
    ) {
        val topOff = topOffset.toPx()
        drawLine(
            color = lineColor,
            start = Offset(0f, topOff),
            end = Offset(size.width, topOff),
            strokeWidth = 4f
        )
        drawCircle(
            color = lineColor,
            radius = circleRadius,
            center = Offset(0f, topOff)
        )
        drawCircle(
            color = lineColor,
            radius = circleRadius,
            center = Offset( size.width, topOff)
        )
    }
}