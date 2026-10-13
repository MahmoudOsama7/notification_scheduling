package com.example.core.utils

import android.os.Build
import android.os.Bundle

@Suppress("DEPRECATION")
inline fun <reified T> Bundle.getParcelableExt(
    key: String,
    androidVersion: Int = Build.VERSION_CODES.UPSIDE_DOWN_CAKE
): T? {
    return if (Build.VERSION.SDK_INT >= androidVersion) {
        this.getParcelable(key, T::class.java)
    } else {
        this.getParcelable(key)
    }
}