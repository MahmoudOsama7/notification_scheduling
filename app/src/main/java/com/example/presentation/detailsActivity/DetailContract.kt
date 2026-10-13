package com.example.presentation.detailsActivity

import com.example.data.model.NotificationItemDto

object DetailContract {
    sealed interface Intent {
        data class SetNotificationItem(val item: NotificationItemDto?) : Intent
        object ScheduleNotification : Intent
        object CancelNotification : Intent
        object NavigateBack : Intent
    }

    data class State(
        val notificationItemDto: NotificationItemDto? = null
    )

    sealed interface Effect {
        data class ShowScheduleDialog(val delayInSeconds: Long) : Effect
        data class ShowToast(val messageResId: Int) : Effect
        object FinishActivity : Effect
    }
}