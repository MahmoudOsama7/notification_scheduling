package com.example.presentation.mainActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.useCase.CancelAllNotificationsUseCase
import com.example.domain.useCase.GetNotificationsUseCase
import com.example.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val cancelAllNotificationsUseCase: CancelAllNotificationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainContract.State())
    val uiState: StateFlow<MainContract.State> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<MainContract.Effect>()
    val effect: SharedFlow<MainContract.Effect> = _effect.asSharedFlow()

    fun processIntent(intent: MainContract.Intent) {
        when (intent) {
            is MainContract.Intent.LoadData -> loadData()
            is MainContract.Intent.SelectNotification -> {
                viewModelScope.launch {
                    _effect.emit(MainContract.Effect.NavigateToDetail(intent.item))
                }
            }
            is MainContract.Intent.CancelAllNotifications -> cancelAllNotifications()
        }
    }

    private fun loadData() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            val data = getNotificationsUseCase()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    notifications = data
                )
            }
        }
    }

    private fun cancelAllNotifications() {
        cancelAllNotificationsUseCase()

        viewModelScope.launch {
            _effect.emit(MainContract.Effect.ShowToast(R.string.toast_all_scheduled_cancelled))
        }
    }
}