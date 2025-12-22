package ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository

import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameConfig
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    suspend fun saveGameConfig(config: GameConfig)

    fun getGameConfig(): Flow<GameConfig>
}