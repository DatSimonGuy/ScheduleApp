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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toString
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.elements.formElements.MultipleChoiceDialog
import com.example.scheduleapp.elements.forms.states.ImportFormState
import com.example.scheduleapp.elements.settings.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportForm(
    onDismissRequest: () -> Unit,
    onSuccess: (List<String>, Long) -> Unit,
    viewModel: SettingsViewModel,
    formState: ImportFormState,
    extraFieldsEnabled: Boolean = true
) {
    var chatIdError by rememberSaveable { mutableStateOf<String?>(null) }
    var showSchedulesSelector by rememberSaveable { mutableStateOf(false) }
    var showChatIdForm by rememberSaveable { mutableStateOf(true) }
    var scheduleNames by rememberSaveable { mutableStateOf(emptyList<String>()) }
    val scope = rememberCoroutineScope()
    var chatId by rememberSaveable { mutableStateOf(formState.chatId.value?.toString() ?: "") }

    if (showSchedulesSelector) {
        MultipleChoiceDialog(
            onDismiss = {
                showSchedulesSelector = false
                onDismissRequest()
            },
            onConfirm = { onSuccess(it, formState.chatId.value ?: 0) },
            label = "Select schedules to import",
            items = scheduleNames
        )
    }

    if (showChatIdForm) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest
        ) {
            Text(
                "Import schedules from bot",
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )
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
                        if (formState.chatId.value == null) {
                            chatIdError = "Chat id must be a valid number"
                            return@Button
                        } else {
                            chatIdError = null
                        }
                        showChatIdForm = false
                        formState.chatId.value?.let {
                            scope.launch {
                                scheduleNames =
                                    viewModel.getSchedules(formState.chatId.value ?: 0).first
                                        ?: emptyList()
                            }
                            showSchedulesSelector = true
                        }
                    }
                ) {
                    Text("Ok")
                }
            }
        }
    }
}