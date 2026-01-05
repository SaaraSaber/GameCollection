package ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models

data class TeamModel(
    val id: Int,
    val name: String,
    val score: Int = 0,
    val hasGoal: Boolean = false,
    val cards: List<GameCardModel> = emptyList(), // استفاده از مدل جدید کارت
    val numberCubes: Int = 2, // مقدار پیش‌فرض طبق منطق بازی
    val selectedCube: Boolean = false,
    val selectedCubeValue: Int = 1,
    // آمارهای دوئل
    val gotGoalDuel: Int = 0,
    val notGotGoalDuel: Int = 0
)
