package es.upsa.nutrivision.data.repository

import android.util.Log
import es.upsa.nutrivision.data.db.FoodWithQuantityDao
import es.upsa.nutrivision.data.model.FoodWithQuantity
import es.upsa.nutrivision.domain.repository.DailyFoodRepository
import javax.inject.Inject

class DailyFoodRepositoryImpl @Inject constructor (
    private val foodWithQuantityDao: FoodWithQuantityDao
) : DailyFoodRepository{


    override suspend fun insert(food: FoodWithQuantity) {
        foodWithQuantityDao.insert(food)
    }

    override suspend fun getAll(): List<FoodWithQuantity> {
        return foodWithQuantityDao.getAll()
    }

    override suspend fun resetCalories() {
        foodWithQuantityDao.deleteAll()
        Log.d("DailyFoodRepositoryImpl", "resetCalories")
    }

}