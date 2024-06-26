package es.upsa.nutrivision.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_with_quantity")
data class FoodWithQuantity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val food: Food,
    val quantity: Float
)