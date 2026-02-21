package com.wem.snoozy.domain.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
        startDestination = Screen.Home.route,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 0))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 0))
        }
    ) {

        composable(
            Screen.Settings.route
        ) {
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