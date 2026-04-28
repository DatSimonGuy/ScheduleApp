package com.example.scheduleapp.elements.settings.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.ColorTheme
import com.example.scheduleapp.data.classes.getColors
import com.example.scheduleapp.utils.textColorForBackground

@Composable
fun ColorThemeCard(
    modifier: Modifier,
    colorTheme: ColorTheme,
    isFocused: Boolean,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            if(isActive) 2.dp else 1.dp,
            if (isFocused) {
                if (isActive) {
                    MaterialTheme.colorScheme.primary
                } else {
                    textColorForBackground(MaterialTheme.colorScheme.surface).copy(alpha = 0.25f)
                }
            } else {
                Color.Transparent
            }

        )
    ) {
        val colors = getColors(colorTheme, context).toList()
        Column {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(8.dp).height(210.dp)
            ) {
                items(colors) { (lessonType, color) ->
                    ColorBlock(stringResource(lessonType.displayName), color)
                }
            }
            if (isFocused) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(colorTheme.displayName),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun ColorBlock(label: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = label,
            color = textColorForBackground(color),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}