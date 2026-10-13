package com.example.presentation.detailsActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.useCase.CancelNotificationUseCase
import com.example.domain.useCase.ScheduleNotificationUseCase
import com.example.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val scheduleNotificationUseCase: ScheduleNotificationUseCase,
    private val cancelNotificationUseCase: CancelNotificationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailContract.State())
    val uiState: StateFlow<DetailContract.State> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<DetailContract.Effect>()
    val effect: SharedFlow<DetailContract.Effect> = _effect.asSharedFlow()

    fun processIntent(intent: DetailContract.Intent) {
        when (intent) {
            is DetailContract.Intent.SetNotificationItem -> {
                _uiState.update { it.copy(notificationItemDto = intent.item) }
            }
            is DetailContract.Intent.ScheduleNotification -> scheduleNotification()
            is DetailContract.Intent.CancelNotification -> cancelNotification()
            is DetailContract.Intent.NavigateBack -> {
                viewModelScope.launch {
                    _effect.emit(DetailContract.Effect.FinishActivity)
                }
            }
        }
    }

    private fun scheduleNotification() {
        val item = _uiState.value.notificationItemDto ?: return
        scheduleNotificationUseCase(item)

        viewModelScope.launch {
            _effect.emit(DetailContract.Effect.ShowScheduleDialog(item.delayInSeconds))
        }
    }

    private fun cancelNotification() {
        val item = _uiState.value.notificationItemDto ?: return
        cancelNotificationUseCase(item.id)

        viewModelScope.launch {
            _effect.emit(DetailContract.Effect.ShowToast(R.string.toast_notification_cancelled))
        }
    }
}