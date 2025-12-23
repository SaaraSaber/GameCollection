package ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.StarterOptionModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterEffect
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterIntent
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterOptionType
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterState
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterTeam
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random


class StarterViewModel() : ViewModel() {

    private val _state = MutableStateFlow(StarterState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<StarterEffect>()
    val effect = _effect.asSharedFlow()

    init {
        getOptions()
    }

    private fun getOptions() {
        val items = listOf(
            StarterOptionModel(
                1,
                R.string.start_team_one,
                R.drawable.pic_team_one,
                StarterOptionType.SELECT_TEAM_1
            ),
            StarterOptionModel(
                2,
                R.string.start_team_two,
                R.drawable.pic_team_two,
                StarterOptionType.SELECT_TEAM_2
            ),
            StarterOptionModel(
                3,
                R.string.start_random,
                R.drawable.pic_random_box,
                StarterOptionType.RANDOM
            )
        )
        _state.update { it.copy(options = items) }
    }

    fun handelIntent(intent: StarterIntent) {
        when (intent) {
            is StarterIntent.OnBackClicked -> sendEffect(StarterEffect.NavigateBack)
            is StarterIntent.OnBottomSheetDismiss -> {
                _state.update { it.copy(showBottomSheet = false) }
            }

            is StarterIntent.OnConfirmStarter -> {
                _state.update { it.copy(showBottomSheet = false) }
                saveAndNavigate()
            }

            is StarterIntent.OnOptionClicked -> {
                val selectedTeam = when (intent.type) {
                    StarterOptionType.SELECT_TEAM_1 -> StarterTeam.TEAM_1
                    StarterOptionType.SELECT_TEAM_2 -> StarterTeam.TEAM_2
                    StarterOptionType.RANDOM -> {
                        if (Random.nextBoolean()) StarterTeam.TEAM_1 else StarterTeam.TEAM_2
                    }
                }
                _state.update { it.copy(showBottomSheet = true, selectedTeam = selectedTeam) }
            }
        }
    }

    private fun saveAndNavigate() {
        viewModelScope.launch {
            val teamId = if (state.value.selectedTeam == StarterTeam.TEAM_1) 0 else 1
            sendEffect(StarterEffect.NavigateToCardSelection(teamId))
        }
    }

    private fun sendEffect(effect: StarterEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}