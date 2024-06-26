package es.upsa.nutrivision.domain.usecase

import es.upsa.nutrivision.data.model.UserData
import es.upsa.nutrivision.domain.repository.UserDataRepository
import javax.inject.Inject

class SaveUserDataUseCase @Inject constructor(
    private val repository: UserDataRepository
) {
    suspend fun execute(userData: UserData) {
        repository.saveUserData(userData)
    }
}