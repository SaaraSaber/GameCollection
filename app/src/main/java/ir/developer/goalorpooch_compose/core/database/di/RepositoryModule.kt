package ir.developer.goalorpooch_compose.core.database.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.developer.goalorpooch_compose.feature.home.data.repository.HomeRepositoryImpl
import ir.developer.goalorpooch_compose.feature.home.domain.repository.HomeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bundHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository
}