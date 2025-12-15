package ir.developer.goalorpooch_compose.feature.home.presentation.utils

import ir.developer.goalorpooch_compose.feature.home.domain.models.AppItemModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.GameModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.OtherModel
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
    data class OnAppItemClicked(val packageName: String) : HomeIntent

    //action in dialog shop
    data class OnBuyCoinClicked(val amount: Int) : HomeIntent
    data class OnOtherItemClicked(val action: OtherItemAction) : HomeIntent
}

data class HomeState(
    val coinBalance: Int = 0,
    val activeDialog: HomeDialogType = HomeDialogType.NONE,
    val shopItems: List<ShopItemModel> = emptyList(),
    val appItems: List<AppItemModel> = emptyList(),
    val gameItems: List<GameModel> = emptyList(),
    val otherItems: List<OtherModel> = emptyList(),
)

sealed interface HomeEffect {
    data class Navigation(val route: String) : HomeEffect
    data object CloseApplication : HomeEffect
    data object OpenMarket : HomeEffect
    data object OpenEmail : HomeEffect
    data object OpenOtherApp : HomeEffect
    data class OpenMarketPage(val packageName: String) : HomeEffect
}

enum class HomeDialogType {
    NONE,  //no dialog
    EXIT,  // open dialog exit
    INFO,  // open dialog info
    SHOP,  // open dialog shop
    APPS  // open dialog apps
}

enum class OtherItemAction {
    OPEN_APP_DIALOG,
    OPEN_SHOP_DIALOG,
    NONE,
}