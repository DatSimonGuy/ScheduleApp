package com.example.scheduleapp.elements.formElements

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.getSelectedDate
import androidx.compose.runtime.Composable
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    onDismissRequest: () -> Unit,
    datePickerState: DatePickerState,
    label: String,
    onConfirm: (LocalDate) -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                    onConfirm(datePickerState.getSelectedDate() ?: LocalDate.now())
                }
            ) {
                Text("Ok")
            }
        }
    ) {
        DatePicker(
            datePickerState,
            title = {
                Text(
                    label,
                )
            }
        )
    }
}