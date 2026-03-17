package com.example.scheduleapp.elements.formElements.dates

import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDateRangePickerDialog(
    onDismissRequest: () -> Unit,
    dateRangePickerState: DateRangePickerState,
    label: String,
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Ok")
            }
        }
    ) {
        DateRangePicker(
            dateRangePickerState,
            title = {
                Text(
                    label,
                )
            }
        )
    }
}