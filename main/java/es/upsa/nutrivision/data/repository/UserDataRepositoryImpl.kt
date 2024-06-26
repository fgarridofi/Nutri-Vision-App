package es.upsa.nutrivision.data.repository

import es.upsa.nutrivision.data.db.UserDataDao
import es.upsa.nutrivision.data.model.UserData
import es.upsa.nutrivision.domain.repository.UserDataRepository
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val userDataDao: UserDataDao
) : UserDataRepository {

    override suspend fun saveUserData(userData: UserData) {
        userDataDao.saveUserData(userData)
    }

    override suspend fun getUserData(): UserData? {
        return userDataDao.getUserData()
    }

    override fun deleteUserData() {
        TODO("Not yet implemented")
    }

    override fun updateUserData() {
        TODO("Not yet implemented")
    }


}