package ir.developer.goalorpooch_compose.feature.goolyapooch.data.repositoryimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameConfig
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class SettingRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    SettingRepository {

    companion object {
        val PLAYER_COUNT_KEY = intPreferencesKey("player_count")
        val SCORE_KEY = intPreferencesKey("score")
        val GOAL_TIME_KEY = intPreferencesKey("goal_time")
        val SHAH_GOAL_TIME_KEY = intPreferencesKey("shah_goal_time")
        val CARD_COUNT_KEY = intPreferencesKey("card_count")
    }

    override suspend fun saveGameConfig(config: GameConfig) {
        dataStore.edit { prefs ->
            prefs[PLAYER_COUNT_KEY] = config.playerCount
            prefs[SCORE_KEY] = config.score
            prefs[GOAL_TIME_KEY] = config.goalTime
            prefs[SHAH_GOAL_TIME_KEY] = config.shahGoalTime
            prefs[CARD_COUNT_KEY] = config.cardCount
        }
    }

    override fun getGameConfig(): Flow<GameConfig> {
        return dataStore.data.map { prefs ->
            GameConfig(
                playerCount = prefs[PLAYER_COUNT_KEY] ?: 6,
                score = prefs[SCORE_KEY] ?: 9,
                goalTime = prefs[GOAL_TIME_KEY] ?: 90,
                shahGoalTime = prefs[SHAH_GOAL_TIME_KEY] ?: 180,
                cardCount = prefs[CARD_COUNT_KEY] ?: 5
            )
        }
    }

}