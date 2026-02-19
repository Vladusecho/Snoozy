package com.wem.snoozy.presentation.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wem.snoozy.data.local.UserPreferencesManager
import com.wem.snoozy.presentation.screen.MainScreen
import com.wem.snoozy.presentation.viewModel.MainViewModel
import com.wem.snoozy.presentation.viewModel.MainViewModelFactory
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
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(userPreferencesManager)
            )
            val mainViewModel: MainViewModel = viewModel(
                factory = MainViewModelFactory(userPreferencesManager)
            )

            val uiState by settingsViewModel.uiState.collectAsState()

            SnoozyTheme(
                darkTheme = uiState.isDarkTheme
            ) {
                MainScreen(
                    mainViewModel,
                    settingsViewModel
                )
            }
        }
    }
}