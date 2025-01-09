package ru.eng.ai.tool

import android.util.Log

actual object Logger {

    actual fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    actual fun e(tag: String, message: String, error: Throwable?) {
        if (error != null) {
            Log.e(tag, message, error)
        } else {
            Log.e(tag, message)
        }
    }

}