package es.upsa.nutrivision.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserData(
    @PrimaryKey val id: Int = 0,
    val weight: Int,
    val height: Int,
    val gender: String,
    val age: Int,
    val physicalActivity: String,
    val goal: String
)

