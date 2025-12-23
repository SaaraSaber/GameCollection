package ir.developer.goalorpooch_compose.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.screens.SettingScreen
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.screens.StarterScreen
import ir.developer.goalorpooch_compose.feature.home.presentation.screen.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }
        composable(Routes.MAFIA) { }
        composable(Routes.GOOLYAPOOCH_SETTING) {
            SettingScreen(navController = navController)
        }
        composable(Routes.GOOLYAPOOCH_STARTER) {
            StarterScreen(navController = navController)
        }
    }
}