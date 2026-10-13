package com.example.domain.useCase

import com.example.data.model.NotificationItemDto
import com.example.domain.repository.NotificationRepository
import javax.inject.Inject

class ScheduleNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(item: NotificationItemDto) {
        repository.scheduleNotification(item)
    }
}