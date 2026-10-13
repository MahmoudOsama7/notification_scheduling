package com.example.domain.useCase

import com.example.data.model.NotificationItemDto
import com.example.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): List<NotificationItemDto> {
        return repository.getNotifications()
    }
}