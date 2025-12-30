package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameConfig
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.TeamModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.GameSessionRepository
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.SettingRepository
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.GameDialogState
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.GameDialogState.ConfirmCube
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StartGameEffect
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StartGameIntent
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StartGameState
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.ToastType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartGameViewModel @Inject constructor(
    private val settingRepo: SettingRepository,
    private val sessionRepo: GameSessionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(StartGameState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<StartGameEffect>()
    val effect = _effect.asSharedFlow()

    private var timerJob: Job? = null
    private var gameConfig: GameConfig? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val config = settingRepo.getGameConfig().first()
            gameConfig = config

            val t1 = sessionRepo.getTeam(1)
            val t2 = sessionRepo.getTeam(2)

            _state.update {
                it.copy(
                    team1 = t1,
                    team2 = t2,
                    timerValue = config.goalTime,
                    emptyHandCount = 3
                )
            }
        }
    }

    fun handleIntent(intent: StartGameIntent) {
        when (intent) {
            StartGameIntent.OnBackClicked -> setDialog(GameDialogState.ExitGame)
            is StartGameIntent.OnCardSelected -> disableCard(intent.cardId)
            is StartGameIntent.OnCardsItemClicked -> setDialog(GameDialogState.Card)
            StartGameIntent.OnCubeConfirmed -> handleCubeConfirm()
            StartGameIntent.OnCubeItemClicked -> setDialog(GameDialogState.Cube)
            is StartGameIntent.OnCubeNumberSelected -> setDialog(ConfirmCube(intent.number))
            StartGameIntent.OnDismissDialog -> setDialog(GameDialogState.None)
            StartGameIntent.OnEmptyHandClicked -> useEmptyHand()
            StartGameIntent.OnExitConfirmed -> {
                sessionRepo.clearSession()
                sendEffect(StartGameEffect.NavigateToHome)
            }

            is StartGameIntent.OnOpeningDuelWinner -> handleOpeningDuel(intent.teamId)
            StartGameIntent.OnRepeatGame -> {
                sessionRepo.clearSession()
                sendEffect(StartGameEffect.NavigateToSetting)
            }

            is StartGameIntent.OnRoundResult -> handleRoundResult(intent.winnerTeamId)
            is StartGameIntent.OnShahGoalResult -> handleShahGoal(intent.isGoalFound)
            StartGameIntent.OnTimerToggleClicked -> toggleTimer()
            is StartGameIntent.OnCardSelectedInDialog -> {
                _state.update {
                    it.copy(selectedCardId = intent.cardId)
                }
            }
            StartGameIntent.OnConfirmCardUsage -> {
                burnSelectedCard()
            }
            is StartGameIntent.OnOpenCardsDialog -> {
                _state.update {
                    it.copy(
                        activeDialog = GameDialogState.Cards(intent.teamId),
                        selectedCardId = null // ریست کردن انتخاب قبلی
                    )
                }
            }
        }
    }

    private fun burnSelectedCard() {
        val currentState = _state.value
        // چک میکنیم دیالوگ کارت باز باشه و یک کارت هم انتخاب شده باشه
        val dialogState = currentState.activeDialog as? GameDialogState.Cards ?: return
        val cardIdToDelete = currentState.selectedCardId ?: return

        updateTeamsStateAndRepo { t1, t2 ->
            // تشخیص میدیم کارت مال کدوم تیمه
            val targetTeam = if (dialogState.teamId == 0) t1 else t2

            // ✅ فیلتر کردن لیست: کارتی که آیدیش برابره رو حذف میکنیم (نگه نمیداریم)
            val newCards = targetTeam.cards.filter { it.id != cardIdToDelete }

            val updatedTeam = targetTeam.copy(cards = newCards)

            // برگرداندن جفت تیم‌ها
            if (updatedTeam.id == 0) {
                Pair(updatedTeam, t2)
            } else {
                Pair(t1, updatedTeam)
            }
        }

        // بستن دیالوگ و پاک کردن انتخاب
        _state.update {
            it.copy(
                activeDialog = GameDialogState.None,
                selectedCardId = null,
                toastMessage = "کارت با موفقیت استفاده شد",
                toastType = ToastType.SUCCESS
            )
        }
        // پاک کردن توست بعد از چند ثانیه
        clearToastAfterDelay()
    }

    private fun toggleTimer() {
        if (_state.value.isTimerRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    isTimerRunning = true,
                    timerButtonTextRes = R.string.result_of_this_round, // تغییر متن دکمه
                    timerButtonIconRes = R.drawable.result
                )
            }

            while (_state.value.timerValue > 0 && _state.value.isTimerRunning) {
                delay(1000L)
                _state.update { it.copy(timerValue = it.timerValue - 1) }
            }

            // زمان تمام شد
            if (_state.value.timerValue == 0) {
                _state.update { it.copy(isTimerRunning = false) }
                // باز کردن دیالوگ نتیجه مناسب
                val nextDialog =
                    if (_state.value.isShahGoalMode) GameDialogState.ShahGoalResult else GameDialogState.RoundResult
                setDialog(nextDialog)
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        _state.update { it.copy(isTimerRunning = false) }

        // وقتی دستی متوقف میکنیم، یعنی نتیجه مشخص شده
        val nextDialog =
            if (_state.value.isShahGoalMode) GameDialogState.ShahGoalResult else GameDialogState.RoundResult
        setDialog(nextDialog)
    }

    /**
     * نتیجه دورِ شاه‌گل
     * @param isGoalFound: آیا گل لو رفت؟ (true = گل پیدا شد/باخت، false = گل پیدا نشد/برد)
     */
    private fun handleShahGoal(isGoalFound: Boolean) {
        if (isGoalFound) {
            updateTeamsStateAndRepo { t1, t2 ->
                var newT1 = t1
                var newT2 = t2

                if (t1.hasGoal) {
                    // تیم ۱ گل داشت و لو رفت
                    val newScore = (t1.score - 3).coerceAtLeast(0)
                    newT1 = t1.copy(score = newScore, hasGoal = false)
                    newT2 = t2.copy(hasGoal = true)
                } else {
                    // تیم ۲ گل داشت و لو رفت
                    val newScore = (t2.score - 3).coerceAtLeast(0)
                    newT2 = t2.copy(score = newScore, hasGoal = false)
                    newT1 = t1.copy(hasGoal = true)
                }
                Pair(newT1, newT2)
            }

            // خروج از حالت شاه‌گل
            _state.update { it.copy(isShahGoalMode = false, activeDialog = GameDialogState.None) }

        } else {
            // 🏆 گل لو نرفت! (تیم صاحب گل برنده نهایی شد)
            setDialog(GameDialogState.Winner)
        }
    }

    /**
     * نتیجه هر دور بازی (عادی)
     * @param winnerTeamId: تیمی که امتیاز گرفته (null یعنی پوچ/مساوی)
     */
    private fun handleRoundResult(winnerTeamId: Int?) {
        val config = gameConfig ?: return
        updateTeamsStateAndRepo { t1, t2 ->
            var newT1 = t1
            var newT2 = t2
            if (winnerTeamId != null) {
                if (winnerTeamId == 0) {
                    newT1 = newT1.copy(score = newT1.score + 1, hasGoal = true)
                    newT2 = newT2.copy(hasGoal = false)
                } else {
                    newT2 = newT2.copy(score = newT2.score + 1, hasGoal = true)
                    newT1 = newT1.copy(hasGoal = false)
                }
            } else {
                // حالت پوچ/جابجایی گل (اختیاری: طبق قانون بازی خودت تنظیم کن)
                // فرض: گل جابجا میشه ولی امتیاز نمیدن
            }
            Pair(newT1, newT2)
        }
        checkGameStatus(config)
    }

    /**
     * بررسی وضعیت بازی (آیا شاه‌گل شده؟ آیا کسی برده؟)
     */
    private fun checkGameStatus(config: GameConfig) {
        val t1 = _state.value.team1
        val t2 = _state.value.team2
        val maxScore = config.score // مثلا ۱۰

        // ۱. چک کردن برنده نهایی
        if (t1.score >= maxScore || t2.score >= maxScore) {
            setDialog(GameDialogState.Winner)
            return
        }

        // ۲. چک کردن حالت شاه‌گل (امتیاز یکی مونده به آخر)
        val isShahGoal = (t1.score == maxScore - 1) || (t2.score == maxScore - 1)

        // ۳. ریست کردن تایمر برای دور بعدی
        val nextTime = if (isShahGoal) config.shahGoalTime else config.goalTime

        _state.update {
            it.copy(
                isShahGoalMode = isShahGoal,
                timerValue = nextTime,
                isTimerRunning = false,
                activeDialog = GameDialogState.None,
                // تغییر دکمه به حالت "شروع زمان"
                timerButtonTextRes = R.string.start_time,
                timerButtonIconRes = R.drawable.time
            )
        }
    }

    /**
     * تعیین برنده دوئل اول بازی (مشخص شدن صاحب گل)
     */
    private fun handleOpeningDuel(winnerTeamId: Int) {
        updateTeamsStateAndRepo { currentT1, currentT2 ->
            val newT1 = currentT1.copy(hasGoal = winnerTeamId == 1)
            val newT2 = currentT2.copy(hasGoal = winnerTeamId == 2)
            Pair(newT1, newT2)
        }
        setDialog(GameDialogState.None)
    }

    private fun updateTeamsStateAndRepo(
        updateBlock: (TeamModel, TeamModel) -> Pair<TeamModel, TeamModel>
    ) {
        _state.update { current ->
            val (newT1, newT2) = updateBlock(current.team1, current.team2)
            sessionRepo.updateTeam(newT1)
            sessionRepo.updateTeam(newT2)
            current.copy(team1 = newT1, team2 = newT2)
        }
    }

    private fun useEmptyHand() {
        if (_state.value.isTimerRunning) {
            if (_state.value.emptyHandCount > 0) {
                _state.update {
                    it.copy(
                        emptyHandCount = it.emptyHandCount - 1,
                        toastMessage = "یک خالی‌بازی استفاده شد",
                        toastType = ToastType.SUCCESS
                    )
                }
                clearToastAfterDelay()
            } else {
                showToast("تعداد خالی‌بازی تمام شده", ToastType.ERROR)
            }
        } else {
            showToast("ابتدا زمان را شروع کنید", ToastType.ERROR)
        }
    }

    private fun handleCubeConfirm() {
        // منطق کم کردن تعداد مکعب و ...
        val currentDialog = _state.value.activeDialog
        if (currentDialog is GameDialogState.ConfirmCube) {
            val number = currentDialog.number
            // آپدیت تیم...
        }
        setDialog(GameDialogState.None)
    }

    private fun disableCard(cardId: Int) {
        updateTeamsStateAndRepo { t1, t2 ->
            // کارتی که انتخاب شده رو پیدا و غیرفعال میکنیم (مثلا از تیم مقابل)
            // اینجا منطق ساده شده: فقط چک میکنیم تو کدوم تیمه و حذفش میکنیم
            // در واقعیت باید کارت رو disable=true کنی

            // فرض: مدل کارت خاصیت disable داره یا حذفش میکنیم
            // پیاده‌سازی دقیق بستگی به مدل GameCardModel شما داره
            Pair(t1, t2)
        }
        setDialog(GameDialogState.None)
    }

    private fun showToast(msg: String, type: ToastType) {
        _state.update { it.copy(toastMessage = msg, toastType = type) }
        clearToastAfterDelay()
    }

    private fun clearToastAfterDelay() {
        viewModelScope.launch {
            delay(2000)
            _state.update { it.copy(toastMessage = null) }
        }
    }
    private fun setDialog(dialog: GameDialogState) {
        _state.update { it.copy(activeDialog = dialog) }
    }

    private fun sendEffect(ef: StartGameEffect) {
        viewModelScope.launch { _effect.emit(ef) }
    }
}