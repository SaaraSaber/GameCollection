package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils

import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameCardModel

data class CardDisplayState(
    val displayedCards: List<GameCardModel> = emptyList(),
    val teamNameRes: Int = R.string.team_one,
    val teamImageRes: Int = R.drawable.pic_team_one,
    val showExitDialog: Boolean = false,
    val nextButtonTextRes: Int = R.string.next_level
)

sealed interface CardDisplayIntent {
    data object OnNextStageClicked : CardDisplayIntent
    data object OnExitConfirmed : CardDisplayIntent
    data object OnExitDismissed : CardDisplayIntent
    data object OnBackClicked : CardDisplayIntent
}

sealed interface CardDisplayEffect {
    data class NavigateToNextTeamSelection(val nextTeamId: Int, val starterTeamId: Int) :
        CardDisplayEffect

    data object NavigateToMainGame : CardDisplayEffect
    data object NavigateToHome : CardDisplayEffect
}