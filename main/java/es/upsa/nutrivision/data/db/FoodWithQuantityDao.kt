package es.upsa.nutrivision.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.upsa.nutrivision.data.model.FoodWithQuantity

@Dao
interface FoodWithQuantityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: FoodWithQuantity)

    @Query("SELECT * FROM food_with_quantity")
    suspend fun getAll(): List<FoodWithQuantity>

    @Query("DELETE FROM food_with_quantity")
    suspend fun deleteAll()
}