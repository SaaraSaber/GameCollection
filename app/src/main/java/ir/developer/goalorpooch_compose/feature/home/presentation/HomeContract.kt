package ir.developer.goalorpooch_compose.feature.home.presentation

import ir.developer.goalorpooch_compose.feature.home.domain.models.AppItemModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.ShopItemModel

sealed interface HomeIntent {
    data object OnGolYaPoochClicked : HomeIntent
    data object OnMafiaClicked : HomeIntent
    data object OnShopClicked : HomeIntent
    data object OnAppsClicked : HomeIntent
    data object OnExitConfirmed : HomeIntent
    data object OnInfoClicked : HomeIntent
    data object OnDialogDismissed : HomeIntent

    //action in dialog info
    data object OnEmailClicked : HomeIntent

    // action in dialog exit
    data object OnExitClicked : HomeIntent
    data object OnStarClicked : HomeIntent

    //action in dialog apps
    data class OnAppItemClicked(val item: String) : HomeIntent

    //action in dialog shop
    data class OnBuyCoinClicked(val amount: Int) : HomeIntent
}

data class HomeState(
    val coinBalance: Int = 0,
    val showExitDialog: Boolean = false,
    val showAppsDialog: Boolean = false,
    val showInfoDialog: Boolean = false,
    val showShopDialog: Boolean = false,
    val shopItems: List<ShopItemModel> = emptyList(),
    val appItems: List<AppItemModel> = emptyList()
)

sealed interface HomeEffect {
    data class Navigation(val route: String) : HomeEffect
    data object CloseApplication : HomeEffect
}