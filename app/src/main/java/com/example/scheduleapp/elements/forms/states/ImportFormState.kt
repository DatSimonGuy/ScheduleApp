package com.example.scheduleapp.elements.forms.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

class ImportFormState(
    val chatId: MutableState<Long?>
) {
    fun fillFields(chatId: Long? = null) {
        chatId?.let {
            this.chatId.value = it
        }
    }
}

@Composable
fun rememberImportFormState(): ImportFormState {
    val chatId = rememberSaveable { mutableStateOf<Long?>(null) }

    return remember {
        ImportFormState(chatId)
    }
}