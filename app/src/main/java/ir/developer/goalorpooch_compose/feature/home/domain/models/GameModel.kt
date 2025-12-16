package ir.developer.goalorpooch_compose.feature.home.domain.models

import ir.developer.goalorpooch_compose.feature.home.presentation.utils.HomeDialogType


data class GameModel(
    val id: Int,
    val name: Int,
    val background: Int,
    val icon: Int,
    val route: String
)

data class OtherModel(
    val id: Int,
    val name: Int,
    val icon: Int,
    val targetDialog: HomeDialogType
)
