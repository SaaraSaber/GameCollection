package ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models


data class GameConfig(
//    val starterTeam: StarterTeam = StarterTeam.TEAM_1,
    val playerCount: Int,
    val score: Int,
    val goalTime: Int,
    val shahGoalTime: Int,
    val cardCount: Int
)

