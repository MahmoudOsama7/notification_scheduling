package com.example.presentation.mainActivity

import com.example.data.model.NotificationItemDto

object MainContract {

    sealed interface Intent {
        object LoadData : Intent
        data class SelectNotification(val item: NotificationItemDto) : Intent
        object CancelAllNotifications : Intent
        object OnActivityStart : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val notifications: List<NotificationItemDto> = emptyList(),
        val isNavigatingToDetail: Boolean = false
    )

    sealed interface Effect {
        data class NavigateToDetail(val item: NotificationItemDto) : Effect
        data class ShowToast(val messageResId: Int) : Effect
    }
}