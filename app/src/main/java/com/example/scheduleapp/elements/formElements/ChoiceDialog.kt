package com.example.scheduleapp.elements.formElements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceDialog(
    onDismiss: () -> Unit,
    onSelectionChange: (String) -> Unit,
    label: String,
    items: List<String>,
    selectedItem: String
) {
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
                            onClick = onDismiss
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            ) { paddingValues ->
                LazyColumn (
                    Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                        .padding(paddingValues)
                ) {
                    items.forEach {
                        item {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelectionChange(it)
                                    }
                                    .padding(16.dp)
                            ) {
                                Text(it)
                                Spacer(Modifier.weight(1f))
                                RadioButton(
                                    selected = selectedItem == it,
                                    onClick = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}