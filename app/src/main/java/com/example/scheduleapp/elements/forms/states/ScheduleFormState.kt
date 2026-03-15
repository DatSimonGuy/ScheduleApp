package com.example.scheduleapp.elements.forms.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

class ScheduleFormState(
    val name: MutableState<String>,
    val isPrivate: MutableState<Boolean>,
    val chatId: MutableState<Long?>
) {
    fun fillFields(name: String? = null, isPrivate: Boolean? = null, chatId: Long? = null) {
        name?.let {
            this.name.value = it
        }
        isPrivate?.let {
            this.isPrivate.value = it
        }
        chatId?.let {
            this.chatId.value = it
        }
    }
}

@Composable
fun rememberScheduleFormState(): ScheduleFormState {
    val name = rememberSaveable { mutableStateOf("") }
    val isPrivate = rememberSaveable { mutableStateOf(true) }
    val chatId = rememberSaveable { mutableStateOf<Long?>(null) }

    return remember {
        ScheduleFormState(name, isPrivate, chatId)
    }
}