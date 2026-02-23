package com.wem.snoozy.presentation.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.wem.snoozy.data.local.UserPreferencesManager
import com.wem.snoozy.presentation.navigation.AppNavGraph
import com.wem.snoozy.presentation.navigation.BottomBarTabs
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

            val themeState by settingsViewModel.themeState.collectAsState()

            SnoozyTheme(
                darkTheme = themeState
            ) {

                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .padding(bottom = 60.dp)
                                .fillMaxWidth()
                                .height(64.dp)
                        ) {
                            BottomBarTabs(navController)
                        }
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        AppNavGraph(navController)
                    }
                }
            }
        }
    }
}