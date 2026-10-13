package com.example.data.repository

import com.example.data.model.NotificationItemDto
import com.example.network.XmlParser
import com.example.notification.WorkNotificationScheduler
import com.example.domain.repository.NotificationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val xmlParser: XmlParser,
    private val scheduler: WorkNotificationScheduler
) : NotificationRepository {

    override suspend fun getNotifications(): List<NotificationItemDto> {
        return xmlParser.fetchAndParseXml()
    }

    override fun scheduleNotification(item: NotificationItemDto) {
        scheduler.scheduleNotification(item)
    }

    override fun cancelNotification(notificationId: Int) {
        scheduler.cancelNotification(notificationId)
    }

    override fun cancelAllNotifications() {
        scheduler.cancelAllNotifications()
    }
}