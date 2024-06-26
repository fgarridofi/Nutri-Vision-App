package es.upsa.nutrivision.presentation.screens.userdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.upsa.nutrivision.data.model.UserData
import es.upsa.nutrivision.domain.usecase.GetUserDataUseCase
import es.upsa.nutrivision.domain.usecase.SaveUserDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val saveUserDataUseCase: SaveUserDataUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> get() = _userData.asStateFlow()

    init {
        loadUserData()
    }

    fun saveUserData(weight: Int, height: Int, gender: String, age: Int, physicalActivity: String, goal: String) {
        viewModelScope.launch {
            val userData = UserData(
                weight = weight, height = height, gender = gender, age = age,
                physicalActivity = physicalActivity, goal = goal
            )
            saveUserDataUseCase.execute(userData)
            loadUserData()  // Reload data after saving
        }
    }

    fun loadUserData() {
        viewModelScope.launch {
            _userData.value = getUserDataUseCase.execute()
        }
    }

    fun isUserDataSaved(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val userData = getUserDataUseCase.execute()
            onResult(userData != null)
        }
    }
}