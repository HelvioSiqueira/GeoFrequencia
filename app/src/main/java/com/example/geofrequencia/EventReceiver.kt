package com.example.geofrequencia

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager

class EventReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {

        val testRequest = OneTimeWorkRequest.Builder(TestWork::class.java)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        val workManager: WorkManager = WorkManager.getInstance(context)
        workManager.beginWith(testRequest).enqueue()
    }
}