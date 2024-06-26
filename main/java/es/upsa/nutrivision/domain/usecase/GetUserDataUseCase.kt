package es.upsa.nutrivision.domain.usecase

import es.upsa.nutrivision.data.model.UserData
import es.upsa.nutrivision.domain.repository.UserDataRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val repository: UserDataRepository
) {
    suspend fun execute(): UserData? {
        return repository.getUserData()
    }
}
