package com.example.scheduleapp.elements.settings.subpages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.scheduleapp.R
import com.example.scheduleapp.elements.formElements.choice.ToggleCard
import com.example.scheduleapp.elements.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibilitySettingsPage(
    viewModel: SettingsViewModel,
    navController: NavController
) {
    val ui by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.accessibility)) },
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
            ToggleCard(
                Modifier.fillMaxWidth(0.95f).align(Alignment.CenterHorizontally),
                stringResource(R.string.bigButton),
                checked = ui.bigButton,
                onCheckedChange = {
                    viewModel.onBigButtonChange(it)
                }
            )
        }
    }
}