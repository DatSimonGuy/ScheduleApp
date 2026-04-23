package com.example.scheduleapp.elements.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.R
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.elements.forms.fields.LessonFormFields
import com.example.scheduleapp.elements.forms.states.rememberLessonFormState
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLessonForm(
    onDismissRequest: () -> Unit,
    onSuccess: (Lesson, DayOfWeek) -> Unit,
    startingDay: DayOfWeek = DayOfWeek.MONDAY
) {
    val sheetState = rememberModalBottomSheetState(true)
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        var subjectError by remember { mutableStateOf<String?>(null) }
        var timeError by remember { mutableStateOf<String?>(null) }
        val formState = rememberLessonFormState(null, startingDay)

        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            LessonFormFields(Modifier, formState, subjectError, timeError)
            Row(
                Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp, top = 48.dp, bottom = 16.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = onDismissRequest
                ) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    onClick = {
                        val (lesson, errs) = formState.validateAndMap(null)
                        if (lesson == null) {
                            subjectError = errs.first
                            timeError = errs.second
                            return@Button
                        }
                        onSuccess(
                            lesson,
                            formState.dayOfWeek.value
                        )
                        onDismissRequest()
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        }
    }
}