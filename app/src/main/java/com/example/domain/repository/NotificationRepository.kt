package com.example.domain.repository

import com.example.data.model.NotificationItemDto

interface NotificationRepository {
    suspend fun getNotifications(): List<NotificationItemDto>
    fun scheduleNotification(item: NotificationItemDto)
    fun cancelNotification(notificationId: Int)
    fun cancelAllNotifications()
}