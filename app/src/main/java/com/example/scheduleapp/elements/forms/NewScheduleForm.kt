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
import androidx.compose.runtime.toString
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.SaveLocation
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.elements.formElements.ToggleCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewScheduleForm(
    onDismissRequest: () -> Unit,
    onSuccess: (String, Schedule) -> Unit,
    initialName: String = "",
    initialPrivateState: Boolean = false,
    initialChatId: Long? = null
) {
    var scheduleName by rememberSaveable { mutableStateOf(initialName) }
    var chatId by rememberSaveable { mutableStateOf(initialChatId?.toString() ?: "") }
    var isBot by rememberSaveable { mutableStateOf(initialPrivateState) }
    var chatIdError by rememberSaveable { mutableStateOf<String?>(null) }

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
            label = "Save schedule in DSB",
            checked = isBot,
            onCheckedChange = { value ->
                isBot = value
            }
        )
        if (isBot) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                singleLine = true,
                value = chatId,
                onValueChange = { chatId = it },
                label = { Text("Chat id") },
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
                    if(chatId.isEmpty() && isBot) {
                        chatIdError = "Chat id cannot be empty"
                        return@Button
                    } else {
                        chatIdError = null
                    }
                    onSuccess(
                        scheduleName,
                        Schedule(
                            saveLocation = if(isBot) SaveLocation.DSB else SaveLocation.LOCAL,
                            chatId = if(isBot) chatId.toLong() else null
                        )
                    )
                    onDismissRequest()
                }
            ) {
                Text("Ok")
            }
        }
    }
}