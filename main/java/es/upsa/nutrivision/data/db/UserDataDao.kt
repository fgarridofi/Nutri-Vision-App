package es.upsa.nutrivision.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.upsa.nutrivision.data.model.UserData

@Dao
interface UserDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserData(userData: UserData)

    @Query("SELECT * FROM user_data WHERE id = 0")
    suspend fun getUserData(): UserData?
}