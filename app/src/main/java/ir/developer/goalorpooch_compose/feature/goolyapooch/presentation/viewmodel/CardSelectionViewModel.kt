package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameCardModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.SettingRepository
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.CardSelectionEffect
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.CardSelectionIntent
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.CardSelectionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardSelectionViewModel @Inject constructor(
    private val settingRepo: SettingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(CardSelectionState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CardSelectionEffect>()
    val effect = _effect.asSharedFlow()

    private val currentTeamId = savedStateHandle.get<Int>("starterTeamId") ?: 0

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val config = settingRepo.getGameConfig().first()
            val cards = generateCards()
            val maxCards = config.cardCount
            val isTeamOne = (currentTeamId == 0)

            val nameRes = if (isTeamOne) R.string.team_one else R.string.team_two
            val imageRes = if (isTeamOne) R.drawable.pic_team_one else R.drawable.pic_team_two
            val teamNameStr = if (isTeamOne) "تیم اول" else "تیم دوم"
            val desc = "$teamNameStr بازی کارت های خود را انتخاب کند. شما باید $maxCards کارت از کارت های زیر را انتخاب کنید."

            _state.update {
                it.copy(
                    cards = cards,
                    maxSelectionCount = maxCards,
                    teamName = nameRes,
                    teamImageRes = imageRes,
                    description = desc
                )
            }
        }
    }

    fun handleIntent(intent: CardSelectionIntent) {
        when (intent) {
            is CardSelectionIntent.OnCardClicked -> toggleCard(intent.cardId)
            is CardSelectionIntent.OnConfirmClicked -> sendEffect(CardSelectionEffect.NavigateToDisplay)
            is CardSelectionIntent.OnExitConfirmed -> {
                _state.update { it.copy(showExitDialog = false) }
                sendEffect(CardSelectionEffect.NavigateToHome)
            }

            is CardSelectionIntent.OnExitDismissed -> _state.update { it.copy(showExitDialog = false) }
            is CardSelectionIntent.OnBackClicked -> _state.update { it.copy(showExitDialog = true) }
        }
    }

    private fun toggleCard(cardId: Int) {
        _state.update { currentState ->
            val cards = currentState.cards.toMutableList()
            val index = cards.indexOfFirst { it.id == cardId }
            if (index == -1) return@update currentState

            val card = cards[index]
            val currentCount = currentState.currentSelectedCount

            if (card.isSelected) {
                cards[index] = card.copy(isSelected = false)
                currentState.copy(
                    cards = cards,
                    currentSelectedCount = currentCount - 1
                )
            } else {
                if (currentCount < currentState.maxSelectionCount) {
                    cards[index] = card.copy(isSelected = true)
                    currentState.copy(
                        cards = cards,
                        currentSelectedCount = currentCount + 1
                    )
                } else {
                    currentState
                }
            }
        }
    }

    private fun sendEffect(effect: CardSelectionEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    private fun generateCards(): List<GameCardModel> {
        return List(9) { index ->
            GameCardModel(id = index, imageRes = R.drawable.pic_card)
        }
    }
}