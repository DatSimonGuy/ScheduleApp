package com.example.scheduleapp.elements.formElements.time

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTimePicker(
    modifier: Modifier = Modifier,
    label: String,
    timePickerState: TimePickerState,
    isEnabled: Boolean = true,
    onValueChange: (LocalTime) -> Unit
) {
    var showTimePicker by rememberSaveable { mutableStateOf(false) }

    val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
    val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
    val formattedTime = time.format(formatter)

    if (showTimePicker) {
        TimePickerWindow(
            {
                val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
                onValueChange(time)
                showTimePicker = false
            },
            timePickerState
        )
    }

    ElevatedCard(
        modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                if (isEnabled) {
                    showTimePicker = true
                }
            }
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label)
            Spacer(Modifier.weight(1f))
            Text(
                formattedTime,
                color = if (isEnabled) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface
            )
        }
    }
}