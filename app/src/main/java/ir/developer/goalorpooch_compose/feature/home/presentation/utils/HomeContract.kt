package ir.developer.goalorpooch_compose.feature.home.presentation.utils

import ir.developer.goalorpooch_compose.feature.home.domain.models.AppItemModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.GameModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.OtherModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.ShopItemModel

sealed interface HomeIntent {
    data class OnGameClicked(val route: String) : HomeIntent
    data class ChangeDialogState(val dialogType: HomeDialogType) : HomeIntent
    data class OnAppItemClicked(val packageName: String) : HomeIntent
    data object OnDialogDismissed : HomeIntent
    data object OnEmailClicked : HomeIntent
    data object OnExitConfirmed : HomeIntent
    data object OnRateClicked : HomeIntent
    data class OnBuyCoinClicked(val amount: Int) : HomeIntent
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
    data object FinishApp : HomeEffect
    data object OpenMarketForRate : HomeEffect
    data object OpenEmail : HomeEffect
    data class OpenExternalApp(val packageName: String) : HomeEffect
}

enum class HomeDialogType {
    NONE,  //no dialog
    EXIT,  // open dialog exit
    INFO,  // open dialog info
    SHOP,  // open dialog shop
    APPS  // open dialog apps
}