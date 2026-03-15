package com.example.scheduleapp.elements.settings.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteScheduleDialog(
    onDismiss: () -> Unit,
    scheduleName: String,
    isLocal: Boolean,
    onSuccess: (Boolean, Boolean) -> Unit
) {
    var repeatForAll by rememberSaveable { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            if (isLocal) {
                                Text(
                                    "Do you really want to delete $scheduleName?",
                                )
                            } else {
                                Text(
                                    "Do you want to delete $scheduleName from bot?",
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    Row (
                        Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (isLocal) {
                            OutlinedButton(
                                modifier = Modifier.padding(end = 8.dp),
                                onClick = {
                                    onSuccess(true, repeatForAll)
                                }
                            ) {
                                Text("Delete")
                            }
                        } else {
                            OutlinedButton(
                                modifier = Modifier.padding(end = 8.dp),
                                onClick = {
                                    onSuccess(true, repeatForAll)
                                }
                            ) {
                                Text("Delete locally")
                            }
                            OutlinedButton(
                                modifier = Modifier.padding(end = 8.dp),
                                onClick = {
                                    onSuccess(false, repeatForAll)
                                }
                            ) {
                                Text("Delete in bot")
                            }
                        }
                    }
                }
            ) { paddingValues ->
                Row(
                    Modifier
                        .padding(paddingValues)
                        .clickable {
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
                        "Repeat this action for all " +
                                (if (isLocal) "local" else "dsb") +
                                " schedules"
                    )
                }
            }
        }
    }
}