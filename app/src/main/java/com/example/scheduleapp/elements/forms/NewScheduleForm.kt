package com.example.scheduleapp.elements.forms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.elements.formElements.ToggleCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewScheduleForm(
    onDismissRequest: () -> Unit,
    onSuccess: (String, Boolean) -> Unit,
    initialName: String = "",
    initialPrivateState: Boolean = true,
) {
    var scheduleName by rememberSaveable { mutableStateOf(initialName) }
    var isPrivate by rememberSaveable { mutableStateOf(initialPrivateState) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            singleLine = true,
            value = scheduleName,
            onValueChange = { scheduleName = it },
            label = { Text("Schedule name") }
        )
        ToggleCard(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            label = "Save locally (currently only locally)",
            checked = isPrivate,
            onCheckedChange = { value ->
                isPrivate = value
            }
        )
        Row(
            Modifier
                .align(Alignment.End)
                .padding(end = 16.dp, top = 48.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    onSuccess(scheduleName, isPrivate)
                    onDismissRequest()
                }
            ) {
                Text("Ok")
            }
        }
    }
}