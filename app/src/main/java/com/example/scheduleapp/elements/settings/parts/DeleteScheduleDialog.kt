package com.example.scheduleapp.elements.settings.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteScheduleDialog(
    onDismiss: () -> Unit,
    scheduleName: String,
    onSuccess: (Boolean, Boolean) -> Unit
) {
    var repeatForAll by rememberSaveable { mutableStateOf(false) }
    var deleteLocally by rememberSaveable { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                "Do you really want to delete $scheduleName?",
                            )
                        }
                    )
                },
                bottomBar = {
                    Row (
                        Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            modifier = Modifier.padding(end = 8.dp),
                            onClick = {
                                onSuccess(deleteLocally, repeatForAll)
                            }
                        ) {
                            Text("Delete")
                        }
                        OutlinedButton(
                            modifier = Modifier.padding(end = 8.dp),
                            onClick = {
                                onDismiss()
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            ) { paddingValues ->
                Column (
                    Modifier.padding(paddingValues)

                ) {
                    Row (
                        Modifier.clickable {
                            deleteLocally = !deleteLocally
                        }
                    ) {
                        Checkbox(
                            checked = !deleteLocally,
                            onCheckedChange = {
                                deleteLocally = it
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            "Also delete schedules from bot",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    Row (
                        Modifier.clickable {
                            repeatForAll = !repeatForAll
                        }
                    ) {
                        Checkbox(
                            checked = repeatForAll,
                            onCheckedChange = {
                                repeatForAll = it
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            "Repeat this action for all schedules",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}