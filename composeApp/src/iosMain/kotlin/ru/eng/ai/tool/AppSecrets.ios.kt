package ru.eng.ai.tool

import platform.Foundation.NSBundle
import platform.Foundation.NSDictionary
import platform.Foundation.dictionaryWithContentsOfFile

actual object AppSecrets {

    actual val apiUrl: String = loadFromAppVariables("apiUrl").orEmpty()

    actual val chatWebsocketsUrl: String = loadFromAppVariables("websocketsUrl").orEmpty()

    actual val appmetricaKey: String = loadFromAppVariables("appmetricaKey").orEmpty()
}

private fun loadFromAppVariables(key: String): String? {
    val result = NSBundle.mainBundle.pathForResource("AppVariables", "plist")?.let {
        val map = NSDictionary.dictionaryWithContentsOfFile(it)
        map?.get(key) as? String?
    }
    return result
}