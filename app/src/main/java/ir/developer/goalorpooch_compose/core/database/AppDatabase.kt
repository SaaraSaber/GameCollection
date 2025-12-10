package ir.developer.goalorpooch_compose.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.developer.goalorpooch_compose.core.database.dao.UserDao
import ir.developer.goalorpooch_compose.core.database.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}