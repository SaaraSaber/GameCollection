package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameConfig
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.TeamModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.GameSessionRepository
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.SettingRepository
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.GameDialogState
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StartGameEffect
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StartGameIntent
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StartGameState
import kotlinx.coroutines.Job
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
            StartGameIntent.OnCardsItemClicked -> setDialog(GameDialogState.Cards)
            StartGameIntent.OnCubeConfirmed -> handleCubeConfirm()
            StartGameIntent.OnCubeItemClicked -> setDialog(GameDialogState.Cube)
            is StartGameIntent.OnCubeNumberSelected -> setDialog(GameDialogState.ConfirmCube(intent.number))
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
        }
    }

    private fun toggleTimer() {
        TODO("Not yet implemented")
    }

    private fun handleShahGoal(goalFound: Boolean) {}

    private fun handleRoundResult(winnerTeamId: Int?) {}

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

    private fun useEmptyHand() {}

    private fun handleCubeConfirm() {}

    private fun disableCard(cardId: Int) {}

    private fun setDialog(dialog: GameDialogState) {
        _state.update { it.copy(activeDialog = dialog) }
    }

    private fun sendEffect(ef: StartGameEffect) {
        viewModelScope.launch { _effect.emit(ef) }
    }
}