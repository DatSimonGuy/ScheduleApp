package com.example.scheduleapp.elements.forms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.SaveLocation
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.elements.formElements.choice.ToggleCard
import com.example.scheduleapp.elements.forms.states.ScheduleFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewScheduleForm(
    onDismissRequest: () -> Unit,
    onSuccess: (String, Schedule) -> Unit,
    formState: ScheduleFormState,
    extraFieldsEnabled: Boolean = true
) {
    var chatIdError by rememberSaveable { mutableStateOf<String?>(null) }
    var chatId by rememberSaveable { mutableStateOf(formState.chatId.value?.toString() ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            singleLine = true,
            value = formState.name.value,
            onValueChange = { formState.name.value = it },
            label = { Text("Schedule name") }
        )
        ToggleCard(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            label = "Save schedule in DSB",
            checked = !formState.isPrivate.value,
            enabled = extraFieldsEnabled,
            onCheckedChange = { value ->
                formState.isPrivate.value = !value
            }
        )
        if (!formState.isPrivate.value) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                singleLine = true,
                value = chatId,
                onValueChange = {
                    try {
                        formState.chatId.value = it.toLong()
                    } catch (_: NumberFormatException) {
                        formState.chatId.value = null
                    }
                    chatId = it
                },
                label = { Text("Chat id") },
                enabled = extraFieldsEnabled,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = chatIdError != null
            )
            chatIdError?.let {
                Text(
                    it,
                    Modifier.fillMaxWidth(0.95f)
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
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
                    if(formState.chatId.value == null && !formState.isPrivate.value) {
                        chatIdError = "Chat id cannot be empty"
                        return@Button
                    } else {
                        chatIdError = null
                    }
                    onSuccess(
                        formState.name.value,
                        Schedule(
                            saveLocation = if(formState.isPrivate.value) SaveLocation.LOCAL else SaveLocation.DSB,
                            chatId = if(formState.isPrivate.value) null else formState.chatId.value
                        )
                    )
                }
            ) {
                Text("Ok")
            }
        }
    }
}