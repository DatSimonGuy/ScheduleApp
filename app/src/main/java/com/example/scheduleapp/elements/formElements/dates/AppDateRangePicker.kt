package com.example.scheduleapp.elements.formElements.dates

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.getSelectedEndDate
import androidx.compose.material3.getSelectedStartDate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scheduleapp.elements.formElements.choice.selectorFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDateRangePicker(
    modifier: Modifier = Modifier,
    label: String,
    dateRangePickerState: DateRangePickerState,
    enabled: Boolean = true
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    if (showDatePicker) {
        AppDateRangePickerDialog (
            onDismissRequest = {
                showDatePicker = false
            },
            dateRangePickerState,
            label,
        )
    }

    Row(
        modifier.height(IntrinsicSize.Min)
    ) {
        OutlinedTextField(
            "${dateRangePickerState.getSelectedStartDate()}" +
                    " - " +
                    "${dateRangePickerState.getSelectedEndDate()}",
            onValueChange = { },
            Modifier.weight(4f),
            label = {
                Text(label)
            },
            enabled = false,
            colors = selectorFieldColors(enabled)
        )
        if (enabled) {
            DateEditButton(
                Modifier.weight(1.2f),
                onClick = {
                    showDatePicker = true
                }
            )
        }
    }
}