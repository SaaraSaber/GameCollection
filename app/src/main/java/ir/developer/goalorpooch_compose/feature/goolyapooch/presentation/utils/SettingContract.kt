package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils

data class SettingState(
    val uiItems: List<SettingUiItem> = emptyList(),
    val playerCount: Int = 6,
    val score: Int = 9,
    val goalTime: Int = 90,
    val shahGoalTime: Int = 180,
    val cardCount: Int = 5,
)

enum class SettingType {
    PLAYER_COUNT,
    SCORE,
    GOAL_TIME,
    SHAH_GOAL_TIME,
    CARD_COUNT
}

sealed interface SettingIntent {
    data class OnChangeValue(val type: SettingType, val isIncrease: Boolean) : SettingIntent
    data object OnBackClicked : SettingIntent
    data object OnNextLevelClicked : SettingIntent
}

sealed interface SettingEffect {
    data object NavigateBack : SettingEffect
    data object NavigateNextLevel : SettingEffect
}

data class SettingUiItem(
    val type: SettingType,
    val title: Int,
    val value: Int,
    val minValue: Int,
    val maxValue: Int
)