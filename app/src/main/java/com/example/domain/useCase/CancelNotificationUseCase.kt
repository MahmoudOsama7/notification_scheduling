package com.example.domain.useCase

import com.example.domain.repository.NotificationRepository
import javax.inject.Inject

class CancelNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(notificationId: Int) {
        repository.cancelNotification(notificationId)
    }
}