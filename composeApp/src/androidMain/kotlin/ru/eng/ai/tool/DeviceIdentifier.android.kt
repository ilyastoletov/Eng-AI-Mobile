package ru.eng.ai.tool

import android.annotation.SuppressLint
import android.provider.Settings

@SuppressLint("HardwareIds")
actual fun getDeviceIdentifier(): String {
    val contentResolver = AppContext.get().contentResolver
    val androidIdKey = Settings.Secure.ANDROID_ID
    return Settings.Secure.getString(contentResolver, androidIdKey)
}