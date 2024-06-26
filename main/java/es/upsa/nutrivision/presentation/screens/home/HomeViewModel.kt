package es.upsa.nutrivision.presentation.screens.home



import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.upsa.nutrivision.data.model.Food
import es.upsa.nutrivision.data.model.FoodWithQuantity
import es.upsa.nutrivision.data.model.UserData
import es.upsa.nutrivision.domain.repository.DailyFoodRepository
import es.upsa.nutrivision.domain.repository.UserDataRepository
import es.upsa.nutrivision.domain.usecase.CalculateNutritionalGoalsUseCase
import es.upsa.nutrivision.domain.usecase.NutritionalGoals
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val calculateNutritionalGoalsUseCase: CalculateNutritionalGoalsUseCase,
    private val foodRepository: DailyFoodRepository,
    private val userRepository: UserDataRepository

) : ViewModel() {

    private val _nutritionalGoals = MutableStateFlow<NutritionalGoals?>(null)
    val nutritionalGoals: StateFlow<NutritionalGoals?> get() = _nutritionalGoals.asStateFlow()

    private val _caloriesConsumed = MutableStateFlow(0.0)
    val caloriesConsumed: StateFlow<Double> get() = _caloriesConsumed.asStateFlow()

    private val _proteinsConsumed = MutableStateFlow(0.0)
    val proteinsConsumed: StateFlow<Double> get() = _proteinsConsumed.asStateFlow()

    private val _fatsConsumed = MutableStateFlow(0.0)
    val fatsConsumed: StateFlow<Double> get() = _fatsConsumed.asStateFlow()

    private val _carbsConsumed = MutableStateFlow(0.0)
    val carbsConsumed: StateFlow<Double> get() = _carbsConsumed.asStateFlow()

    private val _dailyFoods = MutableStateFlow<List<FoodWithQuantity>>(emptyList())
    val dailyFoods: StateFlow<List<FoodWithQuantity>> get() = _dailyFoods.asStateFlow()

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> get() = _userData.asStateFlow()

    private val _resetEvent = MutableSharedFlow<Unit>(replay = 1)
    val resetEvent: SharedFlow<Unit> get() = _resetEvent.asSharedFlow()

    init {
        loadDailyFoods()
        loadUserData()

    }

    fun addFood(food: Food, quantity: Float) {
        viewModelScope.launch {
            val foodEntity = FoodWithQuantity(
                food = food,
                quantity = quantity
            )
            foodRepository.insert(foodEntity)
            loadDailyFoods()
        }
    }


    fun loadDailyFoods() {
        viewModelScope.launch {
            _dailyFoods.value = foodRepository.getAll()
            updateNutritionalConsumption()
        }
    }

    fun loadUserData() {
        viewModelScope.launch {
            _userData.value = userRepository.getUserData() 
            _userData.value?.let { userData ->
                calculateGoals(
                    weight = userData.weight,
                    height = userData.height,
                    age = userData.age,
                    gender = userData.gender,
                    activityLevel = userData.physicalActivity,
                    goal = userData.goal
                )
            }
        }
    }

    private fun updateNutritionalConsumption() {
        val foods = _dailyFoods.value
        _caloriesConsumed.value = foods.sumOf { it.food.nutrients.ENERC_KCAL.toDouble() / 100 * it.quantity }
        _proteinsConsumed.value = foods.sumOf { it.food.nutrients.PROCNT.toDouble() / 100 * it.quantity }
        _fatsConsumed.value = foods.sumOf { it.food.nutrients.FAT.toDouble() / 100 * it.quantity }
        _carbsConsumed.value = foods.sumOf { it.food.nutrients.CHOCDF.toDouble() / 100 * it.quantity }
    }

    fun calculateGoals(weight: Int, height: Int, age: Int, gender: String, activityLevel: String, goal: String) {
        val goals = calculateNutritionalGoalsUseCase.calculate(weight, height, age, gender, activityLevel, goal)
        _nutritionalGoals.value = goals
    }

    fun addCaloriesConsumed(calories: Double) {
        _caloriesConsumed.value += calories
        Log.d("HomeViewModel", "Calories consumed: ${_caloriesConsumed.value}")
        Log.d("HomeViewModel", "Calories total: ${calories}")
    }
    fun addProteinsConsumed(proteins: Double) {
        _proteinsConsumed.value += proteins
    }

    fun addFatsConsumed(fats: Double) {
        _fatsConsumed.value += fats
    }

    fun addCarbsConsumed(carbs: Double) {
        _carbsConsumed.value += carbs
    }

    fun resetCalories() {
        viewModelScope.launch {
            _resetEvent.emit(Unit)
            loadDailyFoods()
        }
    }
}
