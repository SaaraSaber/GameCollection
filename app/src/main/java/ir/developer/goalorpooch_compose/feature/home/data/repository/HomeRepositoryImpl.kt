package ir.developer.goalorpooch_compose.feature.home.data.repository

import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.core.database.dao.UserDao
import ir.developer.goalorpooch_compose.feature.home.domain.models.AppItemModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.GameModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.OtherModel
import ir.developer.goalorpooch_compose.feature.home.domain.models.ShopItemModel
import ir.developer.goalorpooch_compose.feature.home.domain.repository.HomeRepository
import ir.developer.goalorpooch_compose.feature.home.presentation.utils.OtherItemAction
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
            AppItemModel(
                id = 0,
                name = R.string.open_chest,
                description = R.string.description_open_chest,
                iconRes = R.drawable.pic_open_chest,
                packageName = "ir.developre.chistangame"
            ),
            AppItemModel(
                id = 1,
                name = R.string.pro_wallpaper,
                description = R.string.description_pro_wallpaper,
                iconRes = R.drawable.pic_pro_wallpaper,
                packageName = "ir.forrtestt.wall1"
            ),
            AppItemModel(
                id = 2,
                name = R.string.intelligence_test,
                description = R.string.description_intelligence_test,
                iconRes = R.drawable.pic_intelligence_test,
                packageName = "com.example.challenginquestions"
            ),
            AppItemModel(
                id = 3,
                name = R.string.check_list,
                description =R.string.description_check_list,
                iconRes = R.drawable.pic_check_list,
                packageName = "ir.developer.todolist"
            ),
            AppItemModel(
                id = 4,
                name = R.string.dare_or_truth,
                description = R.string.description_dare_or_truth,
                iconRes = R.drawable.pic_dare_or_truth,
                packageName = "ir.codesphere.truthordare"
            )
        )
    }

    override suspend fun updateCoinBalance(amount: Int) {
        val currentCoin = userDao.getDataUser().firstOrNull()?.coin ?: 0
        val newCoin = currentCoin + amount

        userDao.updateCoin(newCoin = newCoin)
    }

    override suspend fun gamesItems(): List<GameModel> {
        return listOf(
            GameModel(
                id = 1,
                name = R.string.manege_goolyapooch,
                background = R.drawable.background_golyapooch,
                icon = R.drawable.hand_icon
            ),
            GameModel(
                id = 2,
                name = R.string.mafia,
                background = R.drawable.background_mafia,
                icon = R.drawable.mafia_icon
            )
        )
    }

    override suspend fun othersItems(): List<OtherModel> {
        return listOf(
            OtherModel(id = 1, name = R.string.shop, icon = R.drawable.shop_icon, action = OtherItemAction.OPEN_SHOP_DIALOG),
            OtherModel(id = 2, name = R.string.apps, icon = R.drawable.apps_icon, action = OtherItemAction.OPEN_APP_DIALOG)
        )
    }
}