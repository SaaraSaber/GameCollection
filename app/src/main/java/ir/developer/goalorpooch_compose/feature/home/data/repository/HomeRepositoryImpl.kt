package ir.developer.goalorpooch_compose.feature.home.data.repository

import ir.developer.goalorpooch_compose.core.database.dao.UserDao
import ir.developer.goalorpooch_compose.feature.home.domain.models.AppItemModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.ShopItemModel
import ir.developer.goalorpooch_compose.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : HomeRepository {

    override fun getUserCoinBalance(): Flow<Int> {
        return userDao.getDataUser().map { user ->
            user?.coin ?: 0
        }
    }

    override suspend fun getShopItems(): List<ShopItemModel> {
        return listOf(
            ShopItemModel(id = 0, coinAmount = 200, price = 5000, discount = 4000, iconRes = 0),
            ShopItemModel(id = 1, coinAmount = 300, price = 5000, discount = 4000, iconRes = 0),
            ShopItemModel(id = 2, coinAmount = 400, price = 5000, discount = 4000, iconRes = 0)
        )
    }

    override suspend fun getAppItems(): List<AppItemModel> {
        return listOf(
            AppItemModel(id = 0, name = "", description = "", iconRes = 0),
            AppItemModel(id = 0, name = "", description = "", iconRes = 0),
            AppItemModel(id = 0, name = "", description = "", iconRes = 0)
        )
    }

    override suspend fun updateCoinBalance(amount: Int) {
        val currentCoin = userDao.getDataUser().firstOrNull()?.coin ?: 0
        val newCoin = currentCoin + amount

        userDao.updateCoin(newCoin = newCoin)
    }
}