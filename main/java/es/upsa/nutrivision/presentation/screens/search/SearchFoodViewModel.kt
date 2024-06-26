package es.upsa.nutrivision.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.upsa.nutrivision.data.api.FoodApi
import es.upsa.nutrivision.data.model.Food
import es.upsa.nutrivision.domain.usecase.FetchFoodSuggestionsUseCase
import es.upsa.nutrivision.domain.usecase.SearchFoodUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchFoodViewModel @Inject constructor(
    private val fetchFoodSuggestionsUseCase: FetchFoodSuggestionsUseCase,
    private val searchFoodUseCase: SearchFoodUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _foodList = MutableStateFlow<List<Food>>(emptyList())
    val foodList: StateFlow<List<Food>> = _foodList

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        fetchSuggestions(newQuery)
    }

    fun searchFood() {
        viewModelScope.launch {
            _foodList.value = searchFoodUseCase.execute(query.value)
        }
    }

    private fun fetchSuggestions(query: String) {
        viewModelScope.launch {
            _suggestions.value = fetchFoodSuggestionsUseCase.execute(query)
        }
    }

    fun onSuggestionClick(suggestion: String) {
        _query.value = suggestion
        searchFood()
    }

    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }
}