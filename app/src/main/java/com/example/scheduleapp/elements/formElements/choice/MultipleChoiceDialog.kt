package com.example.scheduleapp.elements.formElements.choice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultipleChoiceDialog(
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit,
    label: String,
    items: List<String>,
) {
    val selectedItems = rememberSaveable { mutableStateSetOf<String>() }
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(label) }
                    )
                },
                bottomBar = {
                    HorizontalDivider()
                    Column(
                        Modifier.fillMaxWidth()
                    ) {
                        Row (
                            Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                "Select all",
                                Modifier.align(Alignment.CenterVertically)
                            )
                            Checkbox(
                                checked = selectedItems.count() == items.count(),
                                onCheckedChange = {
                                    if (it) {
                                        selectedItems += items
                                    } else {
                                        selectedItems.clear()
                                    }
                                }
                            )
                        }
                        Row (
                            Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                modifier = Modifier.padding(end = 8.dp),
                                onClick = {
                                    onConfirm(selectedItems.toList())
                                    onDismiss()
                                }
                            ) {
                                Text("Ok")
                            }
                        }
                    }
                }
            ) { paddingValues ->
                Column (Modifier.fillMaxWidth().padding(paddingValues)) {
                    HorizontalDivider()
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                    ) {
                        items.forEach { item ->
                            item {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (item in selectedItems) {
                                                selectedItems.remove(item)
                                            } else {
                                                selectedItems.add(item)
                                            }
                                        }
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        item,
                                        Modifier.align(Alignment.CenterVertically)
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Checkbox(
                                        onCheckedChange = { checked ->
                                            if (checked) {
                                                selectedItems.add(item)
                                            } else {
                                                selectedItems.remove(item)
                                            }
                                        },
                                        checked = selectedItems.contains(item)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}