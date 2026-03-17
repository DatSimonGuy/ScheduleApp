package com.example.scheduleapp.elements.home.parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CountDownBlock(
    modifier: Modifier,
    text: String,
    progress: (() -> Float)? = null,
    timer: (() -> String)? = null
) {
    ElevatedCard(
        modifier,
    ) {
        Column(
            Modifier.padding(8.dp).fillMaxSize()
        ) {
            Text(text, Modifier.fillMaxWidth())
            if (progress != null) {
                LinearProgressIndicator(
                    progress = { progress() }
                )
            }
            if (timer != null) {
                Text(timer(), Modifier.fillMaxWidth())
            }
        }
    }
}