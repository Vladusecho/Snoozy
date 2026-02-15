package com.wem.snoozy.domain.navigation

sealed class Screen(
    val route: String
) {

    object Home: Screen(ROUTE_HOME)

    object Settings: Screen(ROUTE_SETTINGS)


    companion object {
        const val ROUTE_HOME = "home"
        const val ROUTE_SETTINGS = "settings"
    }
}