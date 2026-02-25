package com.example.scheduleapp.elements.formElements

import androidx.compose.foundation.border
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsSelector(
    modifier: Modifier = Modifier,
    label: String,
    items: List<String>,
    onSelectionChange: (String) -> Unit,
    selectedItem: String,
    enabled: Boolean = true
) {
    var isSelectorVisible by rememberSaveable { mutableStateOf(false) }

    if (isSelectorVisible) {
        ChoiceDialog(
            onDismiss = {
                isSelectorVisible = false
            },
            onSelectionChange = {
                onSelectionChange(it)
                isSelectorVisible = false
            },
            label = label,
            items = items,
            selectedItem = selectedItem
        )
    }

    ElevatedCard(
        modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                if (enabled) {
                    isSelectorVisible = true
                }
            }
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label)
            Spacer(Modifier.weight(1f))
            Text(
                selectedItem,
                color = if (enabled) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface
            )
        }
    }
}