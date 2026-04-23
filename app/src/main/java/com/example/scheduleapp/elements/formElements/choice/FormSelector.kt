package com.example.scheduleapp.elements.formElements.choice

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSelector(
    modifier: Modifier = Modifier,
    label: String,
    onValueChanged: (String, Int) -> Unit,
    items: List<String>,
    selectedItem: String,
    enabled: Boolean = true
) {
    var pickerExpanded by rememberSaveable { mutableStateOf(false) }
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        disabledBorderColor = MaterialTheme.colorScheme.outline,
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = pickerExpanded,
        onExpandedChange = { pickerExpanded = !pickerExpanded && enabled }
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            enabled = false,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(pickerExpanded)
            },
            colors = textFieldColors
        )

        ExposedDropdownMenu(
            modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.secondary),
            expanded = pickerExpanded,
            onDismissRequest = { pickerExpanded = false }
        ) {
            items.forEachIndexed { i, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onValueChanged(item, i)
                        pickerExpanded = false
                    }
                )
            }
        }
    }
}