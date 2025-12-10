package ir.developer.goalorpooch_compose.feature.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.developer.goalorpooch_compose.feature.home.domain.repository.HomeRepository
import ir.developer.goalorpooch_compose.feature.home.presentation.HomeIntent
import ir.developer.goalorpooch_compose.feature.home.presentation.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        observeUserCoin()
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
            is HomeIntent.OnAppsClicked -> _state.update { it.copy(showAppsDialog = true) }
            is HomeIntent.OnExitClicked -> _state.update { it.copy(showExitDialog = true) }
            is HomeIntent.OnInfoClicked -> _state.update { it.copy(showInfoDialog = true) }
            is HomeIntent.OnShopClicked -> _state.update { it.copy(showShopDialog = true) }
            is HomeIntent.OnStarClicked -> {}
            is HomeIntent.OnEmailClicked -> {}
            is HomeIntent.OnMafiaClicked -> {}
            is HomeIntent.OnExitConfirmed -> {}
            is HomeIntent.OnDialogDismissed -> closeDialogs()
            is HomeIntent.OnAppItemClicked -> {}
            is HomeIntent.OnBuyCoinClicked -> updateCoin(intent.amount)
            is HomeIntent.OnGolYaPoochClicked -> {}
        }
    }

    private fun updateCoin(amount: Int) {
        viewModelScope.launch {
            repository.updateCoinBalance(amount)
        }
    }

    private fun closeDialogs() {
        _state.update {
            it.copy(
                showAppsDialog = false,
                showExitDialog = false,
                showInfoDialog = false,
                showShopDialog = false
            )
        }
    }
}