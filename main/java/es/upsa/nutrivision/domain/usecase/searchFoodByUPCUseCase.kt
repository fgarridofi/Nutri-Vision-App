package es.upsa.nutrivision.domain.usecase

import es.upsa.nutrivision.data.api.FoodApi
import es.upsa.nutrivision.data.model.FoodResponse
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SearchFoodByUPCUseCase @Inject constructor(
    private val foodApi: FoodApi
) {
    suspend fun searchFoodByUPC(upc: String, _foodSearchResult: MutableStateFlow<FoodResponse?>, _foodSearchError: MutableStateFlow<String?>) {
        try {
            val response = foodApi.searchFoodbyUPC(upc)
            _foodSearchResult.value = response
            _foodSearchError.value = null
        } catch (e: Exception) {
            if (e.message?.contains("404") == true) {
                _foodSearchError.value = "Can't find food with UPC code: $upc"
            } else {
                _foodSearchError.value = "Error searching food by UPC"
            }
        }
    }
}
