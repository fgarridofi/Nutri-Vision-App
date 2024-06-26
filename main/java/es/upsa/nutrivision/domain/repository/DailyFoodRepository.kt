package es.upsa.nutrivision.domain.repository

import es.upsa.nutrivision.data.model.FoodWithQuantity

interface DailyFoodRepository {
    suspend fun insert(food: FoodWithQuantity)
    suspend fun getAll(): List<FoodWithQuantity>
    suspend fun resetCalories()
}