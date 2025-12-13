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
        loadGames()
        loadOthers()
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

/**
 * private fun loadData() {
 *     viewModelScope.launch {
 *         // شروع لودینگ
 *         _state.update { it.copy(isLoading = true) }
 *
 *         try {
 *             // استفاده از async برای اینکه هر دو همزمان (موازی) دانلود شوند
 *             // اگر پشت سر هم بنویسید، دومی منتظر اولی می‌ماند (که کندتر است)
 *             val gamesDeferred = async { repository.gamesItems() }
 *             val othersDeferred = async { repository.othersItems() }
 *
 *             // منتظر می‌مانیم تا هر دو تمام شوند
 *             val games = gamesDeferred.await()
 *             val others = othersDeferred.await()
 *
 *             // آپدیت نهایی
 *             _state.update {
 *                 it.copy(
 *                     isLoading = false,
 *                     gameItems = games,
 *                     otherItems = others
 *                 )
 *             }
 *         } catch (e: Exception) {
 *             // هندل کردن خطا (مثلا نمایش اسنک‌بار)
 *             _state.update { it.copy(isLoading = false) }
 *         }
 *     }
 * }
 */