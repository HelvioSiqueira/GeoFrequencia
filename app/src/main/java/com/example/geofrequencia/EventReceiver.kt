package com.example.geofrequencia

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager

class EventReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val action = intent.action

        val workManager: WorkManager

        if(Intent.ACTION_BOOT_COMPLETED == action){
            val testRequest = OneTimeWorkRequest.Builder(TestWork::class.java)
                .setExpedited(OutOfQuotaPolicy.DROP_WORK_REQUEST)
                .build()

            workManager = WorkManager.getInstance(context)

            workManager.beginWith(testRequest).enqueue()
        }
    }
}