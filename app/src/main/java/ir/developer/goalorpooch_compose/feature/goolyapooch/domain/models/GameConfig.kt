package ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models


data class GameConfig(
    val playerCount: Int,
    val score: Int,
    val goalTime: Int,
    val shahGoalTime: Int,
    val cardCount: Int
)

