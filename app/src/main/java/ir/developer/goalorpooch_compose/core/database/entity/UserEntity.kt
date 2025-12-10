package ir.developer.goalorpooch_compose.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val coin: Int = 0
)