package com.example.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationItemDto(
    val id: Int,
    val title: String,
    val delayInSeconds: Long
) : Parcelable
