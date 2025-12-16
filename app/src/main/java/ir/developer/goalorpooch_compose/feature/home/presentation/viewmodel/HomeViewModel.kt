package ir.developer.goalorpooch_compose.feature.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.developer.goalorpooch_compose.feature.home.domain.repository.HomeRepository
import ir.developer.goalorpooch_compose.feature.home.presentation.utils.HomeDialogType
import ir.developer.goalorpooch_compose.feature.home.presentation.utils.HomeEffect
import ir.developer.goalorpooch_compose.feature.home.presentation.utils.HomeIntent
import ir.developer.goalorpooch_compose.feature.home.presentation.utils.HomeState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeUserCoin()
        loadGames()
        loadOthers()
        loadAppItems()
    }

    private fun loadOthers() {
        viewModelScope.launch {
            val others = repository.othersItems()
            _state.update { it.copy(otherItems = others) }
        }
    }

    private fun loadGames() {
        viewModelScope.launch {
            val games = repository.gamesItems()
            _state.update { it.copy(gameItems = games) }
        }
    }

    private fun observeUserCoin() {
        viewModelScope.launch {
            repository.getUserCoinBalance()
                .collect { currentCoin ->
                    _state.update { it.copy(coinBalance = currentCoin) }
                }
        }
    }

    fun homeIntentHandel(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.OnExitConfirmed -> {
                setDialog(HomeDialogType.NONE)
                sendEffect(HomeEffect.FinishApp)
            }

            is HomeIntent.ChangeDialogState -> {
                setDialog(intent.dialogType)
            }

            is HomeIntent.OnRateClicked -> {
                setDialog(HomeDialogType.NONE)
                sendEffect(HomeEffect.OpenMarketForRate)
            }

            is HomeIntent.OnEmailClicked -> {
                setDialog(HomeDialogType.NONE)
                sendEffect(HomeEffect.OpenEmail)
            }

            is HomeIntent.OnGameClicked -> {
                sendEffect(HomeEffect.Navigation(intent.route))
            }

            is HomeIntent.OnDialogDismissed -> setDialog(HomeDialogType.NONE)
            is HomeIntent.OnAppItemClicked -> {
                setDialog(HomeDialogType.NONE)
                sendEffect(HomeEffect.OpenExternalApp(intent.packageName))
            }

            is HomeIntent.OnBuyCoinClicked -> updateCoin(intent.amount)

        }
    }

    private fun setDialog(type: HomeDialogType) {
        _state.update { it.copy(activeDialog = type) }
    }

    private fun updateCoin(amount: Int) {
        viewModelScope.launch {
            repository.updateCoinBalance(amount)
        }
    }

    private fun sendEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun loadAppItems() {
        viewModelScope.launch {
            val apps = repository.getAppItems()
            _state.update { it.copy(appItems = apps) }
        }
    }
}