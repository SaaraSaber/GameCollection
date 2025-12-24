package ir.developer.goalorpooch_compose.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.screens.CardSelectionScreen
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.screens.DisplayCardScreen
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
        composable(
            route = Routes.GOOLYAPOOCH_CARD_SELECTION,
            arguments = listOf(
                navArgument("starterTeamId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            CardSelectionScreen(navController = navController)
        }

        composable(
            route = Routes.GOOLYAPOOCH_DISPLAY_CARDS,
            arguments = listOf(
                navArgument("currentTeamId") { type = NavType.IntType },
                navArgument("starterTeamId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            DisplayCardScreen(navController = navController)
        }
    }
}