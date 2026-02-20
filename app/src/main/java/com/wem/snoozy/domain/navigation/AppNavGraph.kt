package com.wem.snoozy.domain.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wem.snoozy.domain.navigation.Screen

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    homeScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
    groupsScreenContent: @Composable () -> Unit
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
        composable(Screen.Groups.route) {
            groupsScreenContent()
        }
        composable(Screen.Profile.route) {
            profileScreenContent()
        }
    }

}