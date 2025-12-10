package ir.developer.goalorpooch_compose.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.developer.goalorpooch_compose.core.database.AppDatabase
import ir.developer.goalorpooch_compose.core.database.dao.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "game_collection_db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("INSERT INTO user (id, coin) VALUES (1, 100)")
                }
            })
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

}