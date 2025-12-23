package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils

import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.StarterOptionModel

sealed interface StarterEffect {
    data class NavigateToCardSelection(val teamId: Int) : StarterEffect
    data object NavigateBack : StarterEffect
}

data class StarterState(
    val options: List<StarterOptionModel> = emptyList(),
    val showBottomSheet: Boolean = false,
    val selectedTeam: StarterTeam = StarterTeam.TEAM_1
)

enum class StarterTeam {
    TEAM_1,
    TEAM_2
}

sealed interface StarterIntent {
    data class OnOptionClicked(val type: StarterOptionType) : StarterIntent
    data object OnBottomSheetDismiss : StarterIntent
    data object OnConfirmStarter : StarterIntent
    data object OnBackClicked : StarterIntent
}

enum class StarterOptionType {
    SELECT_TEAM_1,
    SELECT_TEAM_2,
    RANDOM
}