package ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models

data class GameCardModel(
    val id: Int,
    val title: Int = 0,
    val description: Int = 0,
    val imageRes: Int = 0,
    val isSelected: Boolean = false,
    val isUsed: Boolean = false
)
