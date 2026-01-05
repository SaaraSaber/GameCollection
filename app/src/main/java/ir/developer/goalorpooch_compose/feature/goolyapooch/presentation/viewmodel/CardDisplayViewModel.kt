package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.GameSessionRepository
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.SettingRepository
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.CardDisplayEffect
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.CardDisplayIntent
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.CardDisplayState
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterTeam
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardDisplayViewModel @Inject constructor(
    private val sessionRepo: GameSessionRepository,
    private val settingRepo: SettingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentTeamId = savedStateHandle.get<Int>("currentTeamId") ?: 0
    private val starterTeamId = savedStateHandle.get<Int>("starterTeamId") ?: 0

    private val _state = MutableStateFlow(CardDisplayState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CardDisplayEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val config = settingRepo.getGameConfig().first()
            val pickCount = config.cardCount

            val currentTeam = if (currentTeamId == 0) StarterTeam.TEAM_1 else StarterTeam.TEAM_2

            val allCards = sessionRepo.getAllCards()
            val shuffledCards = allCards.shuffled().take(pickCount)
            sessionRepo.saveSelectedCards(currentTeam, shuffledCards)

            val btnText =
                if (currentTeamId == starterTeamId) R.string.next_level else R.string.start_game

            val isTeamOne = (currentTeamId == 0)
            _state.update {
                it.copy(
                    displayedCards = shuffledCards,
                    teamNameRes = if (isTeamOne) R.string.team_one else R.string.team_two,
                    teamImageRes = if (isTeamOne) R.drawable.pic_team_one else R.drawable.pic_team_two,
                    nextButtonTextRes = btnText
                )
            }
        }
    }

    fun handleIntent(intent: CardDisplayIntent) {
        when (intent) {
            CardDisplayIntent.OnNextStageClicked -> decideNextStep()
            CardDisplayIntent.OnExitConfirmed -> {
                _state.update { it.copy(showExitDialog = false) }
                sendEffect(CardDisplayEffect.NavigateToHome)
            }

            CardDisplayIntent.OnExitDismissed -> _state.update { it.copy(showExitDialog = false) }
            CardDisplayIntent.OnBackClicked -> _state.update { it.copy(showExitDialog = true) }
        }
    }

    private fun decideNextStep() {
        if (currentTeamId == starterTeamId) {
            val nextTeamId = if (starterTeamId == 0) 1 else 0
            sendEffect(CardDisplayEffect.NavigateToNextTeamSelection(nextTeamId, starterTeamId))
        } else {
            sendEffect(CardDisplayEffect.NavigateToMainGame(starterTeamId))
        }
    }

    private fun sendEffect(effect: CardDisplayEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}