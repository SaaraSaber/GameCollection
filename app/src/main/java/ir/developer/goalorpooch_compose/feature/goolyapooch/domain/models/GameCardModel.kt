package ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models

import ir.developer.goalorpooch_compose.R

data class GameCardModel(
    val id: Int,
    val title: Int = R.string.free_stone_free_sparrow,
    val description: Int = R.string.description_free_stone_free_sparrow,
    val imageRes: Int,
    val isSelected: Boolean = false
)
