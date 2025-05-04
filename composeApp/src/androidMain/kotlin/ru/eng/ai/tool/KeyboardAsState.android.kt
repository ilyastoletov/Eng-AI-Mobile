package ru.eng.ai.tool

import android.app.Activity
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import android.graphics.Rect
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun keyboardAsState(): State<Boolean> {
    val activity = LocalContext.current as Activity
    val isKeyboardVisible = remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val rootView = activity.window.decorView
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            isKeyboardVisible.value = keypadHeight > (screenHeight * 0.15)
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)

        onDispose {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    return isKeyboardVisible
}