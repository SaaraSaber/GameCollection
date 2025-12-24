package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils

import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameCardModel

data class CardSelectionState(
    val cards: List<GameCardModel> = emptyList(),
    val maxSelectionCount: Int = 5,
    val currentSelectedCount: Int = 0,
    val teamName: Int = R.string.team_one,
    val showExitDialog: Boolean = false,
    val teamImageRes: Int = R.drawable.pic_team_one,
    val description: String = ""
)

sealed interface CardSelectionIntent {
    data class OnCardClicked(val cardId: Int) : CardSelectionIntent
    data object OnConfirmClicked : CardSelectionIntent
    data object OnExitConfirmed : CardSelectionIntent    // بله، خارج شو
    data object OnExitDismissed : CardSelectionIntent
    data object OnBackClicked : CardSelectionIntent
}

sealed interface CardSelectionEffect {
    data class NavigateToDisplay(val currentTeamId: Int, val starterTeamId: Int) : CardSelectionEffect
    data object NavigateToHome : CardSelectionEffect
}