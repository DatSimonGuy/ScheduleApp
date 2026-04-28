package com.example.scheduleapp.elements.formElements.dates

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.getSelectedEndDate
import androidx.compose.material3.getSelectedStartDate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
            Modifier.weight(4f).padding(end = 8.dp),
            label = {
                Text(label)
            },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )
        if (enabled) {
            OutlinedButton(
                onClick = {
                    showDatePicker = true
                },
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(4.dp),
            ) {
                Icon(Icons.Default.Edit, "")
            }
        }
    }
}