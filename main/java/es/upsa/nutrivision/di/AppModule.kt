package es.upsa.nutrivision.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.Room
import androidx.work.WorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.upsa.nutrivision.data.api.FoodApi
import es.upsa.nutrivision.data.db.AppDatabase
import es.upsa.nutrivision.data.db.FoodWithQuantityDao
import es.upsa.nutrivision.data.db.UserDataDao
import es.upsa.nutrivision.data.repository.ApiRepositoryImpl
import es.upsa.nutrivision.data.repository.DailyFoodRepositoryImpl
import es.upsa.nutrivision.data.repository.UserDataRepositoryImpl
import es.upsa.nutrivision.domain.repository.ApiRepository
import es.upsa.nutrivision.domain.repository.DailyFoodRepository
import es.upsa.nutrivision.domain.repository.UserDataRepository
import es.upsa.nutrivision.domain.usecase.CalculateNutritionalGoalsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiRepository(
        api: FoodApi
    ): ApiRepository = ApiRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "nutrivision_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDataDao(database: AppDatabase): UserDataDao {
        return database.userDataDao()
    }


    @Provides
    @Singleton
    fun provideUserDataRepository(
        userDataDao: UserDataDao
    ): UserDataRepository = UserDataRepositoryImpl(userDataDao)

    @Provides
    @Singleton
    fun provideCalculateNutritionalGoalsUseCase(): CalculateNutritionalGoalsUseCase {
        return CalculateNutritionalGoalsUseCase()
    }

    @Provides
    @Singleton
    fun provideFoodWithQuantityDao(database: AppDatabase): FoodWithQuantityDao {
        return database.foodWithQuantityDao()
    }

    @Provides
    @Singleton
    fun provideDailyFoodRepository(
        foodWithQuantityDao: FoodWithQuantityDao
    ): DailyFoodRepository = DailyFoodRepositoryImpl(foodWithQuantityDao)

    @Provides
    @Singleton
    fun provideWorkerFactory(
        factory: HiltWorkerFactory
    ): WorkerFactory {
        return factory
    }
}


