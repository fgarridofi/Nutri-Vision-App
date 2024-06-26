package es.upsa.nutrivision.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.upsa.nutrivision.data.mapper.FoodTypeConverters
import es.upsa.nutrivision.data.model.FoodWithQuantity
import es.upsa.nutrivision.data.model.UserData

@Database(entities = [UserData::class, FoodWithQuantity::class], version = 3, exportSchema = false)
@TypeConverters(FoodTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDataDao(): UserDataDao
    abstract fun foodWithQuantityDao(): FoodWithQuantityDao


}
