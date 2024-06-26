package es.upsa.nutrivision.domain.repository

import es.upsa.nutrivision.data.model.UserData

interface UserDataRepository {
    suspend fun saveUserData(userData: UserData)
    suspend fun getUserData(): UserData?
    fun deleteUserData()
    fun updateUserData()
}