package com.example.scheduleapp.elements.formElements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePicker(
    modifier: Modifier = Modifier,
    timePickerState: TimePickerState,
    enabled: Boolean = true,
    isError: Boolean = false
) {
    var showTimePicker by rememberSaveable { mutableStateOf(false) }

    if (showTimePicker) {
        Dialog(
            onDismissRequest = {
                showTimePicker = false
            }
        ) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.64f)
            ) {
                Column(
                    Modifier.background(MaterialTheme.colorScheme.surface).fillMaxSize().padding(16.dp)
                ) {
                    TimePicker(
                        timePickerState,
                        Modifier.fillMaxWidth().padding(8.dp)
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                showTimePicker = false
                            },
                        ) {
                            Text("Ok")
                        }
                    }
                }
            }
        }
    }
    val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
    val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
    val formattedTime = time.format(formatter)
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (isError) MaterialTheme.colorScheme.error else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .clickable(enabled) { showTimePicker = true }
    ) {
        Text(
            text = formattedTime,
            fontSize = 20.sp
        )
    }

}