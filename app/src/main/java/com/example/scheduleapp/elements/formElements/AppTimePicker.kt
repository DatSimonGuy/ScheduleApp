package com.example.scheduleapp.elements.formElements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePicker(
    modifier: Modifier = Modifier,
    timePickerState: TimePickerState
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

    val formattedTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
    Text(
        formattedTime,
        modifier.clickable{
            showTimePicker = true
        }.padding(16.dp),
        fontSize = 20.sp
    )

}