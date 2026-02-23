package com.example.scheduleapp.elements.formElements

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.getSelectedEndDate
import androidx.compose.material3.getSelectedStartDate
import androidx.compose.runtime.Composable
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDateRangePickerDialog(
    onDismissRequest: () -> Unit,
    dateRangePickerState: DateRangePickerState,
    label: String,
    onConfirm: (LocalDate, LocalDate) -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                    onConfirm(
                        dateRangePickerState.getSelectedStartDate() ?: LocalDate.now(),
                        dateRangePickerState.getSelectedEndDate() ?: LocalDate.now()
                    )
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