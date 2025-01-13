package ru.eng.ai.tool

import platform.UIKit.UIDevice
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
actual fun getDeviceIdentifier(): String {
    val fallbackUUID = Uuid.random().toHexString()
    return UIDevice.currentDevice.identifierForVendor?.UUIDString ?: fallbackUUID
}