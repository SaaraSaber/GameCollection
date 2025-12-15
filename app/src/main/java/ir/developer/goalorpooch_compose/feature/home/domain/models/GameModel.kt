package ir.developer.goalorpooch_compose.feature.home.domain.models

import ir.developer.goalorpooch_compose.feature.home.presentation.utils.OtherItemAction

data class GameModel(
    val id:Int,
    val name: Int,
    val background:Int,
    val icon: Int
)
data class OtherModel(
    val id:Int,
    val name: Int,
    val icon: Int,
    val action: OtherItemAction
)
