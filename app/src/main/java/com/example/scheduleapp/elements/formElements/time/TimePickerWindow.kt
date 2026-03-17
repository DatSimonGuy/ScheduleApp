package com.example.scheduleapp.elements.formElements.time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerWindow(
    onDismissRequest: () -> Unit,
    timePickerState: TimePickerState
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .height(500.dp)
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
                        onClick = onDismissRequest,
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}