package com.example.notification

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.core.constants.AppConstants
import com.example.data.model.NotificationItemDto
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkNotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun scheduleNotification(item: NotificationItemDto) {
        val inputData = Data.Builder()
            .putString(AppConstants.EXTRA_TITLE, item.title)
            .putInt(AppConstants.EXTRA_ID, item.id)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(item.delayInSeconds, TimeUnit.SECONDS)
            .setInputData(inputData)
            .addTag(AppConstants.WORK_TAG_NOTIFICATION)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "${AppConstants.WORK_UNIQUE_PREFIX}${item.id}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelNotification(notificationId: Int) {
        WorkManager.getInstance(context).cancelUniqueWork("${AppConstants.WORK_UNIQUE_PREFIX}$notificationId")
    }

    fun cancelAllNotifications() {
        WorkManager.getInstance(context).cancelAllWorkByTag(AppConstants.WORK_TAG_NOTIFICATION)
    }
}