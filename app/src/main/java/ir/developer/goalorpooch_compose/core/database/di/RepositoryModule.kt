package ir.developer.goalorpooch_compose.core.database.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.developer.goalorpooch_compose.feature.goolyapooch.data.repository.GameSessionRepositoryImpl
import ir.developer.goalorpooch_compose.feature.goolyapooch.data.repository.SettingRepositoryImpl
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.GameSessionRepository
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.SettingRepository
import ir.developer.goalorpooch_compose.feature.home.data.repository.HomeRepositoryImpl
import ir.developer.goalorpooch_compose.feature.home.domain.repository.HomeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindSettingRepository(
        settingRepositoryImpl: SettingRepositoryImpl
    ): SettingRepository

    @Binds
    @Singleton
    abstract fun bindGameSessionRepository(
        impl: GameSessionRepositoryImpl
    ): GameSessionRepository
}