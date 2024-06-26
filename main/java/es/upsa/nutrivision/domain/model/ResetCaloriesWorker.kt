package es.upsa.nutrivision.domain.model


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import es.upsa.nutrivision.domain.repository.DailyFoodRepository


@HiltWorker
class ResetCaloriesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dailyFoodRepository: DailyFoodRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            dailyFoodRepository.resetCalories()
            Log.d("ResetCaloriesWorker", "Calories reset successfully")

            val intent = Intent("es.upsa.nutrivision.CALORIES_RESET")
            applicationContext.sendBroadcast(intent)

            Result.success()
        } catch (e: Exception) {
            Log.e("ResetCaloriesWorker", "Error resetting calories", e)
            Result.failure()
        }
    }
}