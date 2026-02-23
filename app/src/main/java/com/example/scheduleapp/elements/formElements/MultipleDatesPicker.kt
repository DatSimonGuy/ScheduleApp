package com.example.scheduleapp.elements.formElements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.setDisplayedMonth
import androidx.compose.material3.setSelectedDate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultipleDatesPicker(
    modifier: Modifier = Modifier,
    selectedDates: MutableList<LocalDate>
) {
    val datePickerState = rememberDatePickerState()
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }

    if (showDatePickerDialog) {
        AppDatePickerDialog(
            onDismissRequest = {
                showDatePickerDialog = false
            },
            datePickerState,
            "Add a new date",
            onConfirm = {
                selectedDates.add(it)
                datePickerState
                    .setDisplayedMonth(
                        YearMonth.of(
                            LocalDate.now().year,
                            LocalDate.now().month
                        )
                    )
                datePickerState
                    .setSelectedDate(
                        LocalDate.now()
                    )
            }
        )
    }

    Column (
        modifier.fillMaxWidth()
    ) {
        Row (
            Modifier.fillMaxWidth().padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Selected dates:", Modifier.padding(start = 16.dp))
            IconButton(
                onClick = {
                    showDatePickerDialog = true
                }
            ) {
                Icon(Icons.Default.Add, "")
            }
        }
        for (date in selectedDates) {
            Row (
                Modifier
                    .fillMaxWidth(0.95f)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceBright),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(date.toString(), Modifier.padding(start = 16.dp))
                IconButton(
                    onClick = {
                        selectedDates -= date
                    }
                ) {
                    Icon(Icons.Default.Delete, "")
                }
            }
        }
    }
}