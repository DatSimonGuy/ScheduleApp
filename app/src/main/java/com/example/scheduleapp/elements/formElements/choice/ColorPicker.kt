package com.example.scheduleapp.elements.formElements.choice

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.fromColorLong
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.scheduleapp.R
import com.kborowy.colorpicker.KolorPicker

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    selectedColor: Long,
    onColorSelected: (Color) -> Unit
) {
    var showColorPicker by rememberSaveable { mutableStateOf(false) }
    var tempColor by remember { mutableLongStateOf(selectedColor) }

    LaunchedEffect(selectedColor) {
        tempColor = selectedColor
    }

    if (showColorPicker) {
        Dialog(
            onDismissRequest = {
                showColorPicker = false
            }
        ) {
            Column {
                KolorPicker(
                    modifier = Modifier.fillMaxHeight(0.4f).padding(bottom = 16.dp),
                    onColorSelected = { pickedColor ->
                        tempColor = pickedColor.value.toLong()
                    },
                    initialColor = Color.fromColorLong(selectedColor)
                )
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = {
                            tempColor = selectedColor
                            showColorPicker = false
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Button(
                        onClick = {
                            onColorSelected(Color.fromColorLong(tempColor))
                            showColorPicker = false
                        }
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                }
            }
        }
    }

    ElevatedCard(
        modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                showColorPicker = true
            }
    ) {
        Row(
            Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label)
            Spacer(Modifier.weight(1f))
            OutlinedButton (
                enabled = enabled,
                onClick = {
                    showColorPicker = true
                }
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .border(1.dp, androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), androidx.compose.foundation.shape.CircleShape)
                    ) {
                        drawCircle(color = Color.fromColorLong(tempColor))
                    }
                    Icon(Icons.Default.KeyboardArrowDown, "")
                }
            }
        }
    }
}