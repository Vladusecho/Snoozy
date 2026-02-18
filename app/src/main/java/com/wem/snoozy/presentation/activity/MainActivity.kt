package com.wem.snoozy.presentation.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wem.snoozy.data.local.UserPreferencesManager
import com.wem.snoozy.presentation.screen.MainScreen
import com.wem.snoozy.presentation.viewModel.SettingsViewModel
import com.wem.snoozy.presentation.viewModel.SettingsViewModelFactory
import com.wem.snoozy.ui.theme.SnoozyTheme

class MainActivity : ComponentActivity() {

    private lateinit var userPreferencesManager: UserPreferencesManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userPreferencesManager = UserPreferencesManager(this)

        setContent {
            val viewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(userPreferencesManager)
            )

            val uiState by viewModel.uiState.collectAsState()

            SnoozyTheme(
                darkTheme = uiState.isDarkTheme
            ) {
                MainScreen(
                    viewModel
                )
            }
        }
    }
}