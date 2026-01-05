package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils

import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.TeamModel


sealed interface StartGameIntent {
    // تایمر
    data object OnTimerToggleClicked : StartGameIntent

    // دیالوگ‌ها و نویگیشن
    data object OnBackClicked : StartGameIntent
    data object OnDismissDialog : StartGameIntent
    data object OnExitConfirmed : StartGameIntent
    data object OnRepeatGame : StartGameIntent
    data class OnBurnCard(val cardId: Int) : StartGameIntent

    // لاجیک بازی
    data class OnOpeningDuelWinner(val teamId: Int) : StartGameIntent
    data class OnRoundResult(val outcome: RoundOutcome) : StartGameIntent // null = پوچ/مساوی
    data class OnShahGoalResult(val isGoalFound: Boolean) : StartGameIntent // true = گل لو رفت
    data class OnOpenCardsDialog(val teamId: Int) : StartGameIntent
    data class OnDuelResult(val result: DuelResult) : StartGameIntent

    // ✅ کلیک روی کارت (فقط انتخاب، نه حذف)
    data class OnCardSelectedInDialog(val cardId: Int) : StartGameIntent

    // ✅ کلیک روی دکمه تایید نهایی
    data object OnConfirmCardUsage : StartGameIntent

    // آیتم‌ها
    data object OnEmptyHandClicked : StartGameIntent
    data object OnCardsItemClicked : StartGameIntent
    data class OnCardSelected(val cardId: Int) : StartGameIntent
    data object OnCubeItemClicked : StartGameIntent
    data class OnCubeNumberSelected(val number: Int) : StartGameIntent
    data object OnCubeConfirmed : StartGameIntent
}

data class StartGameState(
    // وضعیت تیم‌ها (امتیاز، نوبت، کارت‌ها و...)
    val team1: TeamModel = TeamModel(id = 0, name = "تیم اول"),
    val team2: TeamModel = TeamModel(id = 1, name = "تیم دوم"),
    val selectedCardId: Int? = null,
    val timerValue: Int = 90, // ثانیه
    val isTimerRunning: Boolean = false,
    val isShahGoalMode: Boolean = false, // آیا در حالت شاه‌گل هستیم؟
    val isDuelMode: Boolean = false, // آیا بازی به دوئل کشیده؟
    val starterTeamId: Int = 0,
    val activeDialog: GameDialogState = GameDialogState.OpeningDuel, // پیش‌فرض: دوئل اول بازی

    val emptyHandCount: Int = 3,

    // پیغام‌ها (Toast)
    val toastMessage: String? = null,
    val toastType: ToastType = ToastType.INFO,

    // متن دکمه تایمر (شروع زمان / نتیجه دور)
    val timerButtonTextRes: Int = R.string.start_time,
    val timerButtonIconRes: Int = R.drawable.time
)

enum class ToastType {
    SUCCESS,
    ERROR,
    INFO
}

enum class RoundOutcome {
    TECHNICAL_WIN, // گزینه اول (کاپ): برد ۲ امتیازی (یا با مکعب)
    SIMPLE_WIN,    // گزینه دوم (تیک): برد ساده (حفظ گل)
    LOSS           // گزینه سوم (ضربدر): باخت (از دست دادن گل و امتیاز دادن به حریف)
}

enum class DuelResult {
    KEPT_GOAL, // حفظ گل (موفقیت)
    LOST_GOAL  // از دست دادن گل (شکست)
}

sealed interface GameDialogState {
    data object None : GameDialogState
    data object ExitGame : GameDialogState
    data object OpeningDuel : GameDialogState // دوئل اول بازی
    data object RoundResult : GameDialogState // نتیجه این دور
    data object ShahGoalResult : GameDialogState // نتیجه شاه گل
    data object Winner : GameDialogState // برنده نهایی
    data object DuelResult : GameDialogState // نتیجه دوئل (اگر مد دوئل فعال شد)
    data class Cards(val teamId: Int) : GameDialogState // لیست کارت‌ها
    data object Cube : GameDialogState // انتخاب مکعب
    data class ConfirmCube(val number: Int) : GameDialogState // تایید مکعب
}

sealed interface StartGameEffect {
    data object NavigateToHome : StartGameEffect
    data object NavigateToSetting : StartGameEffect
}
