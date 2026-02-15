package com.wem.snoozy.domain.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wem.snoozy.domain.navigation.Screen

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    homeScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit
) {

    NavHost(
        navHostController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Settings.route) {
            settingsScreenContent()
        }
        composable(Screen.Home.route) {
            homeScreenContent()
        }
    }

}