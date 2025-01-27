package ru.eng.ai.tool

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

actual fun copyText(text: String) {
    val appContext = AppContext.get()
    val clipboardManager = appContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("Copied Text", text)
    clipboardManager.setPrimaryClip(clipData)
}