package com.example.domain.useCase

import com.example.domain.repository.NotificationRepository
import javax.inject.Inject

class CancelAllNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke() {
        repository.cancelAllNotifications()
    }
}