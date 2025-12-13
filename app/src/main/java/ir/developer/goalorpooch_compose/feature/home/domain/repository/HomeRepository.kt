package ir.developer.goalorpooch_compose.feature.home.domain.repository

import ir.developer.goalorpooch_compose.feature.home.domain.models.AppItemModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.GameModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.OtherModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.ShopItemModel
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getUserCoinBalance(): Flow<Int>
    suspend fun getShopItems(): List<ShopItemModel>
    suspend fun getAppItems(): List<AppItemModel>
    suspend fun updateCoinBalance(amount: Int)
    suspend fun gamesItems(): List<GameModel>
    suspend fun othersItems(): List<OtherModel>
}