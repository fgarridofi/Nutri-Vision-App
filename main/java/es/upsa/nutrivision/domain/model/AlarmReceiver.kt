package es.upsa.nutrivision.domain.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

public class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val workRequest = OneTimeWorkRequestBuilder<ResetCaloriesWorker>().build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}