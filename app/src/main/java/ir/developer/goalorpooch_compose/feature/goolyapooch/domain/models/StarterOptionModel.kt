package ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models

import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterOptionType

data class StarterOptionModel(
    val id: Int,
    val title: Int,
    val icon: Int,
    val type: StarterOptionType
)