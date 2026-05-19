package com.example.scheduleapp.elements.settings.subpages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toColorLong
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.scheduleapp.R
import com.example.scheduleapp.data.classes.ColorTheme
import com.example.scheduleapp.data.classes.LessonType
import com.example.scheduleapp.data.datastore.themeDataStore
import com.example.scheduleapp.elements.formElements.choice.ColorPicker
import com.example.scheduleapp.elements.settings.SettingsViewModel
import com.example.scheduleapp.elements.settings.parts.ColorThemeCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSettingsPage(
    viewModel: SettingsViewModel,
    navController: NavController,
) {
    val ui by viewModel.uiState.collectAsStateWithLifecycle()
    val carouselState = rememberCarouselState(initialItem = ui.currentTheme.ordinal) { ColorTheme.entries.size }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val customColors by context.themeDataStore.data.collectAsStateWithLifecycle(
        emptyMap()
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.colors)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalCenteredHeroCarousel(
                carouselState,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                maxItemWidth = 220.dp,
                itemSpacing = 8.dp,
            ) { i ->
                ColorThemeCard(
                    Modifier
                        .wrapContentHeight()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Transparent),
                    ColorTheme.entries[i],
                    carouselState.currentItem == i,
                    ColorTheme.entries[carouselState.currentItem] == ui.currentTheme,
                    onClick = {
                        scope.launch {
                            carouselState.animateScrollToItem(i)
                        }
                    }
                )
            }
            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    viewModel.onCurrentThemeChange(ColorTheme.entries[carouselState.currentItem])
                },
                enabled = ColorTheme.entries[carouselState.currentItem] != ui.currentTheme
            ) {
                Icon(Icons.Default.Check, "")
                Text(stringResource(R.string.selectTheme))
            }
            if (ColorTheme.entries[carouselState.currentItem] == ColorTheme.CUSTOM) {
                LazyColumn {
                    items(LessonType.entries) {
                        ColorPicker(
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .padding(bottom = 8.dp),
                            label = stringResource(it.displayName),
                            selectedColor = customColors[it] ?: it.color.toColorLong(),
                            onColorSelected = { color ->
                                scope.launch {
                                    context.themeDataStore.updateData { currentMap ->
                                        currentMap.toMutableMap().apply {
                                            this[it] = color.toColorLong()
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}