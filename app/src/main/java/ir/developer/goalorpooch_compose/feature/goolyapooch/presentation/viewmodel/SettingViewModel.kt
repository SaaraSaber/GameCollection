package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameConfig
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.SettingRepository
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.SettingEffect
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.SettingIntent
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.SettingState
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.SettingType
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.SettingUiItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(val repository: SettingRepository) : ViewModel() {
    private val _state = MutableStateFlow(SettingState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SettingEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadSavedConfig()
    }

    private fun loadSavedConfig() {
        viewModelScope.launch {
            repository.getGameConfig().collect { savedConfig ->
                _state.update {
                    it.copy(
                        playerCount = savedConfig.playerCount,
                        score = savedConfig.score,
                        goalTime = savedConfig.goalTime,
                        shahGoalTime = savedConfig.shahGoalTime,
                        cardCount = savedConfig.cardCount
                    )
                }
                updateUiList()
            }
        }
    }

    private fun updateUiList() {
        val current = _state.value

        val newList = listOf(
            SettingUiItem(
                type = SettingType.PLAYER_COUNT,
                title = R.string.number_player,
                value = current.playerCount,
                minValue = 4,
                maxValue = 50
            ),
            SettingUiItem(
                type = SettingType.SCORE,
                title = R.string.victory_points,
                value = current.score,
                minValue = 4,
                maxValue = 21
            ),
            SettingUiItem(
                type = SettingType.GOAL_TIME,
                title = R.string.time_get_goal,
                value = current.goalTime,
                minValue = 30,
                maxValue = 600
            ),
            SettingUiItem(
                type = SettingType.SHAH_GOAL_TIME,
                title = R.string.time_get_king_goal,
                value = current.shahGoalTime,
                minValue = 30,
                maxValue = 600
            ),
            SettingUiItem(
                type = SettingType.CARD_COUNT,
                title = R.string.number_cards,
                value = current.cardCount,
                minValue = 5,
                maxValue = 9
            )
        )
        _state.update { it.copy(uiItems = newList) }
    }

    fun handelIntent(intent: SettingIntent) {
        when (intent) {
            is SettingIntent.OnBackClicked -> sendEffect(SettingEffect.NavigateBack)
            is SettingIntent.OnChangeValue -> {
                changeRawValue(intent.type, intent.isIncrease)
                updateUiList()
            }

            is SettingIntent.OnNextLevelClicked -> saveAndNavigate()
        }
    }

    private fun saveAndNavigate() {
        viewModelScope.launch {
            val currentState = _state.value

            val configToSave = GameConfig(
                playerCount = currentState.playerCount,
                score = currentState.score,
                goalTime = currentState.goalTime,
                shahGoalTime = currentState.shahGoalTime,
                cardCount = currentState.cardCount
            )

            repository.saveGameConfig(configToSave)

            sendEffect(SettingEffect.NavigateNextLevel)
        }
    }

    private fun sendEffect(effect: SettingEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun changeRawValue(
        type: SettingType,
        increase: Boolean
    ) {
        _state.update { current ->
            val step = when (type) {
                SettingType.PLAYER_COUNT -> 2
                SettingType.SCORE -> 1
                SettingType.GOAL_TIME -> 10
                SettingType.SHAH_GOAL_TIME -> 10
                SettingType.CARD_COUNT -> 1
            }

            val changeAmount = if (increase) step else -step

            when (type) {
                SettingType.PLAYER_COUNT -> {
                    val newValue = (current.playerCount + changeAmount).coerceIn(4, 50)
                    current.copy(playerCount = newValue)
                }

                SettingType.SCORE -> {
                    val newValue = (current.score + changeAmount).coerceIn(4, 21)
                    current.copy(score = newValue)
                }

                SettingType.GOAL_TIME -> {
                    val newValue = (current.goalTime + changeAmount).coerceIn(30, 600)
                    current.copy(goalTime = newValue)
                }

                SettingType.SHAH_GOAL_TIME -> {
                    val newValue = (current.shahGoalTime + changeAmount).coerceIn(30, 600)
                    current.copy(shahGoalTime = newValue)
                }

                SettingType.CARD_COUNT -> {
                    val newValue = (current.cardCount + changeAmount).coerceIn(5, 9)
                    current.copy(cardCount = newValue)
                }
            }
        }
    }

}