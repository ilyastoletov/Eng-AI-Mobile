package ru.eng.ai.tool

import platform.UIKit.UIPasteboard

actual fun copyText(text: String) {
    UIPasteboard.generalPasteboard.string = text
}