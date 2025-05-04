package ru.eng.ai.tool

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIKeyboardWillHideNotification
import platform.UIKit.UIKeyboardWillShowNotification

@Composable
actual fun keyboardAsState(): State<Boolean> {
    val isKeyboardOpen = remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val defaultNotificationCenter = NSNotificationCenter.defaultCenter

        val showListener = defaultNotificationCenter.addObserverForName(
            name = UIKeyboardWillShowNotification,
            `object` = null,
            queue = null
        ) { isKeyboardOpen.value = true }

        val hideListener = defaultNotificationCenter.addObserverForName(
            name = UIKeyboardWillHideNotification,
            `object` = null,
            queue = null
        ) { isKeyboardOpen.value = true }

        onDispose {
            with(defaultNotificationCenter) {
                removeObserver(showListener)
                removeObserver(hideListener)
            }
        }
    }

    return isKeyboardOpen
}