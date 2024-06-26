package es.upsa.nutrivision.domain.usecase

import es.upsa.nutrivision.data.api.FoodApi
import es.upsa.nutrivision.data.model.Food
import javax.inject.Inject

class SearchFoodUseCase @Inject constructor(
    private val apiService: FoodApi
) {
    suspend fun execute(query: String): List<Food> {
        val response = apiService.searchFood(query)
        return response.hints.map { hint ->
            hint.food.copy(measures = hint.measures)
        }
    }
}