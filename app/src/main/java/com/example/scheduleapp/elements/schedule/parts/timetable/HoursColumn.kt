package com.example.scheduleapp.elements.schedule.parts.timetable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalTime

@Composable
fun HoursColumn(
    startHour: Int,
    modifier: Modifier,
    hourHeight: HourHeight
) {
    Column(
        modifier
    ) {
        for(i in startHour..23) {
            val time = LocalTime.of(i, 0)
            Text(
                time.toString(),
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .height(hourHeight.value)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
            )
        }
    }
}