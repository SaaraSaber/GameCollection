package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameConfig
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.TeamModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.GameSessionRepository
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.SettingRepository
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.DuelResult
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.GameDialogState
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.GameDialogState.Cards
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.GameDialogState.ConfirmCube
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.RoundOutcome
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StartGameEffect
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StartGameIntent
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StartGameState
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterTeam
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
    private val sessionRepo: GameSessionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(StartGameState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<StartGameEffect>()
    val effect = _effect.asSharedFlow()

    private var timerJob: Job? = null
    private var gameConfig: GameConfig? = null

    private val starterIdArg = savedStateHandle.get<Int>("starterTeamId") ?: 0

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val config = settingRepo.getGameConfig().first()
            gameConfig = config
            val rawT1 = sessionRepo.getTeam(0)
            val rawT2 = sessionRepo.getTeam(1)
            val t1Cards = sessionRepo.getSelectedCards(team = StarterTeam.TEAM_1)
            val t2Cards = sessionRepo.getSelectedCards(team = StarterTeam.TEAM_2)
            val t1 = rawT1.copy(cards = t1Cards)
            val t2 = rawT2.copy(cards = t2Cards)

            _state.update {
                it.copy(
                    team1 = t1,
                    team2 = t2,
                    timerValue = config.goalTime,
                    emptyHandCount = 3,
                    starterTeamId = starterIdArg
                )
            }
        }
    }

    fun handleIntent(intent: StartGameIntent) {
        when (intent) {
            StartGameIntent.OnBackClicked -> setDialog(GameDialogState.ExitGame)
//            is StartGameIntent.OnCardSelected -> disableCard()
            is StartGameIntent.OnCardsItemClicked -> {
                val currentTeamHasGoal = if (_state.value.team1.hasGoal) 0 else 1
                val targetTeamId = if (currentTeamHasGoal == 0) 1 else 0
                onOpenCardDialog(targetTeamId)
            }

            is StartGameIntent.OpenCards -> onOpenCardDialog(intent.teamId)
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

            is StartGameIntent.OnRoundResult -> handleRoundResult(intent.outcome)
            is StartGameIntent.OnShahGoalResult -> handleShahGoal(intent.isGoalFound)
            StartGameIntent.OnTimerToggleClicked -> toggleTimer()
            is StartGameIntent.OnCardSelectedInDialog -> {
                _state.update {
                    it.copy(selectedCardId = intent.cardId)
                }
            }

            is StartGameIntent.OnConfirmCardUsage -> {
                burnSelectedCard(cardId = intent.cardId, teamId = intent.teamId)
            }

            is StartGameIntent.OnOpenCardsDialog -> {
                _state.update {
                    it.copy(
                        activeDialog = Cards(intent.teamId),
                        selectedCardId = null // ریست کردن انتخاب قبلی
                    )
                }
            }

            is StartGameIntent.OnDuelResult -> handleDuelResult(intent.result)
        }
    }

    private fun onOpenCardDialog(teamId: Int) {
        val currentState = _state.value

        // قانون ۱: اگر در این دور کارت زده، اجازه نده
        if (currentState.hasUsedCardInCurrentRound) {
            showToast("در هر دور فقط یک کارت می‌توانید استفاده کنید", ToastType.ERROR)
            return
        }

        // پیدا کردن تیمی که درخواست داده
        val targetTeam =
            if (currentState.team1.id == teamId) currentState.team1 else currentState.team2

        // قانون ۲: اگر لیست کارتش خالیه، باز نکن
        if (targetTeam.cards.isEmpty()) {
            showToast("کارت‌های این تیم تمام شده است!", ToastType.ERROR)
            return
        }

        // همه چی اوکی بود -> دیالوگ رو باز کن
        _state.update {
            it.copy(activeDialog = GameDialogState.Cards(teamId))
        }
    }

    private fun burnSelectedCard(cardId: Int,teamId: Int) {
        _state.update { currentState ->
            val t1 = currentState.team1
            val t2 = currentState.team2

            val targetTeam = if (t1.id == teamId) t1 else t2

            // ۲. ⚠️ تغییر مهم: به جای حذف، وضعیتش را True میکنیم
            val newCards = targetTeam.cards.map { card ->
                if (card.id == cardId) {
                    card.copy(isUsed = true) // 🚩 پرچم بالا: این کارت سوخت
                } else {
                    card // بقیه کارت‌ها دست نخورند
                }
            }

            // ۳. ساخت تیم جدید با لیست آپدیت شده
            val updatedTeam = targetTeam.copy(cards = newCards)

            // ۴. ذخیره در دیتابیس
            sessionRepo.updateTeam(updatedTeam)

            // ۵. آپدیت استیت (با همان روش امن ID Matching)
            currentState.copy(
                team1 = if (t1.id == targetTeam.id) updatedTeam else t1,
                team2 = if (t2.id == targetTeam.id) updatedTeam else t2,
                activeDialog = GameDialogState.None,
                hasUsedCardInCurrentRound = true,
                toastMessage = "کارت اعمال شد",
                toastType = ToastType.SUCCESS
            )
        }
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
//    private fun handleRoundResult(outcome: RoundOutcome) {
//        updateTeamsStateAndRepo { t1, t2 ->
//
//            val isT1Holder = t1.hasGoal
//            val holder = if (isT1Holder) t1 else t2
//            val opponent = if (isT1Holder) t2 else t1
//            var newHolder = holder
//            var newOpponent = opponent
//
//            when (outcome) {
//                RoundOutcome.TAK_ZARB -> {
//                    newOpponent = opponent.copy(
//                        score = opponent.score + 2,
//                        hasGoal = true
//                    )
//                    newHolder = holder.copy(hasGoal = false)
//                }
//
//                RoundOutcome.TOOK_GOAL -> {
//                    newOpponent = opponent.copy(hasGoal = true)
//                    newHolder = holder.copy(hasGoal = false)
//                }
//
//                RoundOutcome.DID_NOT_TAKE -> {
//                    newHolder = holder.copy(score = holder.score + 1)
//                }
//            }
//
//            if (isT1Holder) {
//                Pair(newHolder, newOpponent)
//            } else {
//                Pair(newOpponent, newHolder)
//            }
//        }
//
//        _state.update {
//            it.copy(
//                activeDialog = GameDialogState.None,
//                timerButtonTextRes = R.string.start_time,
//                timerButtonIconRes = R.drawable.time,
//                hasUsedCardInCurrentRound = false,
//                emptyHandCount = 3
//            )
//        }
//    }
    private fun handleRoundResult(outcome: RoundOutcome) {
        _state.update { currentState ->
            // ۱. گرفتن وضعیت فعلی تیم‌ها
            val t1 = currentState.team1
            val t2 = currentState.team2

            // ۲. تشخیص اینکه الان توپ دست کیه (Holder) و کی داره دفاع میکنه (Opponent)
            val isT1Holder = t1.hasGoal
            val holder = if (isT1Holder) t1 else t2
            val opponent = if (isT1Holder) t2 else t1

            // متغیرهای موقت برای نسخه جدید تیم‌ها
            var newHolder = holder
            var newOpponent = opponent

            // ۳. اعمال سناریوی شما
            when (outcome) {
                RoundOutcome.TAK_ZARB -> {
                    // گزینه اول: تیم مقابل (Opponent) ۲ امتیاز میگیره و گل هم مال اون میشه
                    newOpponent = opponent.copy(
                        score = opponent.score + 2,
                        hasGoal = true // ⚽ گل جابجا شد
                    )
                    newHolder = holder.copy(hasGoal = false)
                }

                RoundOutcome.TOOK_GOAL -> {
                    // گزینه دوم: تیم مقابل (Opponent) فقط گل رو میگیره (بدون امتیاز)
                    newOpponent = opponent.copy(
                        hasGoal = true // ⚽ گل جابجا شد
                    )
                    newHolder = holder.copy(hasGoal = false)
                }

                RoundOutcome.DID_NOT_TAKE -> {
                    // گزینه سوم: تیم صاحب توپ (Holder) ۱ امتیاز میگیره و گل دستش میمونه
                    newHolder = holder.copy(
                        score = holder.score + 1,
                        hasGoal = true // ⚽ گل سر جاش موند
                    )
                    // حریف هیچ تغییری نمیکنه (نه امتیاز، نه گل)
                    newOpponent = opponent.copy(hasGoal = false)
                }
            }

            // ۴. ذخیره در دیتابیس (هر دو تیم رو آپدیت میکنیم)
            sessionRepo.updateTeam(newHolder)
            sessionRepo.updateTeam(newOpponent)

            // ۵. ⚠️ بخش حیاتی برای جلوگیری از جابجایی اشتباه (Fix Swapping Bug)
            // تمام تیم‌های آپدیت شده رو میریزیم تو یه کیسه
            val allUpdatedTeams = listOf(newHolder, newOpponent)

            // حالا با دقتِ جراحی، تیم‌ها رو میذاریم سر جاشون:

            // "بگرد ببین کدوم تیم، آی‌دی تیم ۱ رو داره؟ همونو بذار جای team1"
            val finalTeam1 = allUpdatedTeams.find { it.id == t1.id } ?: t1

            // "بگرد ببین کدوم تیم، آی‌دی تیم ۲ رو داره؟ همونو بذار جای team2"
            val finalTeam2 = allUpdatedTeams.find { it.id == t2.id } ?: t2

            // ۶. آپدیت نهایی استیت
            currentState.copy(
                team1 = finalTeam1,
                team2 = finalTeam2,

                // ریست کردن دیالوگ و تنظیمات دور بعد
                activeDialog = GameDialogState.None,
                timerButtonTextRes = R.string.start_time,
                hasUsedCardInCurrentRound = false, // 🔓 اجازه استفاده از کارت برای دور جدید
                emptyHandCount = 3
            )
        }
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
    private fun handleOpeningDuel(winnerId: Int) {
        _state.update { currentState ->
            currentState.copy(
                team1 = currentState.team1.copy(
                    hasGoal = (currentState.team1.id == winnerId)
                ),
                team2 = currentState.team2.copy(
                    hasGoal = (currentState.team2.id == winnerId)
                ),
                activeDialog = GameDialogState.None,
                timerButtonTextRes = R.string.start_time
            )
        }
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

    private fun handleDuelResult(result: DuelResult) {
        updateTeamsStateAndRepo { t1, t2 ->
            // پیدا کردن تیمی که گل داره (صاحب دوئل)
            val holder = if (t1.hasGoal) t1 else t2
            val opponent = if (t1.hasGoal) t2 else t1

            var newHolder = holder
            var newOpponent = opponent

            when (result) {
                DuelResult.KEPT_GOAL -> {
                    // موفقیت: گل رو حفظ کرده -> gotGoalDuel زیاد میشه
                    // گل دست خودش میمونه (تغییری در hasGoal نداریم)
                    newHolder = holder.copy(
                        gotGoalDuel = holder.gotGoalDuel + 1
                    )
                }

                DuelResult.LOST_GOAL -> {
                    // شکست: گل رو از دست داده -> notGotGoalDuel زیاد میشه
                    // گل میره تیم حریف
                    newHolder = holder.copy(
                        hasGoal = false,
                        notGotGoalDuel = holder.notGotGoalDuel + 1
                    )
                    newOpponent = opponent.copy(hasGoal = true)
                }
            }

            if (newHolder.id == 0) Pair(newHolder, newOpponent) else Pair(newOpponent, newHolder)
        }

        setDialog(GameDialogState.None)
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
        val selectedValue =
            (_state.value.activeDialog as? GameDialogState.ConfirmCube)?.number ?: return

        updateTeamsStateAndRepo { t1, t2 ->
            // پیدا کردن تیمی که گل داره (چون فقط اون میتونه مکعب بزنه)
            val holder = if (t1.hasGoal) t1 else t2

            // آپدیت تیم: کم کردن تعداد مکعب و ست کردن امتیاز این دور
            val newHolder = holder.copy(
                numberCubes = holder.numberCubes - 1,
                selectedCubeValue = selectedValue // ✅ ذخیره 2, 4 یا 6
            )

            if (newHolder.id == 0) Pair(newHolder, t2) else Pair(t1, newHolder)
        }

        // بستن دیالوگ و نمایش پیام موفقیت
        setDialog(GameDialogState.None)
        showToast("امتیاز این دور ${selectedValue} برابر شد", ToastType.SUCCESS)
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