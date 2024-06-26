package es.upsa.nutrivision

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import es.upsa.nutrivision.domain.model.AlarmReceiver
import es.upsa.nutrivision.domain.model.ResetCaloriesWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class NutriVisionApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val caloriesResetReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            context?.let {
                val resetIntent = Intent("es.upsa.nutrivision.CALORIES_RESET")
                context.sendBroadcast(resetIntent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate() {
        super.onCreate()
        setupPeriodicWork()
        setupAlarmManager()

        val filter = IntentFilter("es.upsa.nutrivision.CALORIES_RESET")
        registerReceiver(caloriesResetReceiver, filter,RECEIVER_NOT_EXPORTED)
    }

    override fun onTerminate() {
        super.onTerminate()
        // Desregistrar el receptor de broadcast
        unregisterReceiver(caloriesResetReceiver)
    }

    private fun setupPeriodicWork() {
        val workRequest = PeriodicWorkRequestBuilder<ResetCaloriesWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build())
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "resetCaloriesWork",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
        Log.d("NutriVisionApplication", "setupPeriodicWork")
    }

    private fun setupAlarmManager() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY,1) // Ajusta esto a la hora que prefieras
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun calculateInitialDelay(): Long {
        val now = java.util.Calendar.getInstance()
        val resetTime = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 1)
            set(java.util.Calendar.MINUTE,0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
            if (before(now)) {
                add(java.util.Calendar.DAY_OF_MONTH, 1)
            }
        }
        Log.d("NutriVisionApplication", "Initial delay: ${resetTime.timeInMillis - now.timeInMillis} ms")

        return resetTime.timeInMillis - now.timeInMillis
    }
    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
