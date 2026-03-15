package com.example.scheduleapp.elements.formElements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
                .fillMaxHeight(0.5f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            TopAppBar(
                                title = { Text(label) }
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
                                onConfirm(selectedItems.toList())
                                onDismiss()
                            }
                        ) {
                            Text("Ok")
                        }
                    }
                }
            ) { paddingValues ->
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
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
                                    .padding(8.dp)
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