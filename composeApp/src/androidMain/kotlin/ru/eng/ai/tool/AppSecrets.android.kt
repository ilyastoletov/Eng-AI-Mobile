package ru.eng.ai.tool

import ru.eng.ai.BuildConfig

actual object AppSecrets {
    actual val apiUrl = BuildConfig.API_URL
    actual val chatWebsocketsUrl = BuildConfig.WEBSOCKETS_URL
    actual val appmetricaKey = BuildConfig.APPMETRICA_KEY
}