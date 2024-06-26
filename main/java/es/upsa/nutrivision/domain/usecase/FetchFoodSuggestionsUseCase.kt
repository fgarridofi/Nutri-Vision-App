package es.upsa.nutrivision.domain.usecase

import es.upsa.nutrivision.data.api.FoodApi
import javax.inject.Inject

class FetchFoodSuggestionsUseCase @Inject constructor(
    private val apiService: FoodApi
) {
    suspend fun execute(query: String): List<String> {
        return if (query.isNotEmpty()) {
            apiService.autocomplete(query)
        } else {
            emptyList()
        }
    }
}