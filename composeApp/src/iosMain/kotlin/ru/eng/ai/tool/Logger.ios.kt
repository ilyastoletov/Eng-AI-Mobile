package ru.eng.ai.tool

import platform.Foundation.NSLog

actual object Logger {

    actual fun d(tag: String, message: String) {
        NSLog("DEBUG [$tag] $message")
    }

    actual fun i(tag: String, message: String) {
        NSLog("INFO [$tag] $message")
    }

    actual fun e(tag: String, message: String, error: Throwable?) {
        val formattedMessage = StringBuilder("ERROR [$tag] $message.")
        error?.let { formattedMessage.append("THROWABLE $error CAUSE ${error.cause}") }
        NSLog(formattedMessage.toString())
    }

}