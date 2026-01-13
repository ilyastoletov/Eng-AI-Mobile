package ru.eng.ai.tool

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.core.view.WindowCompat

actual fun handleThemeChange(isDark: Boolean) {
    val applicationInstance = AppContext.get() as? Application ?: return
    applicationInstance.registerActivityLifecycleCallbacks(
        object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                setWindowStatusBarAppearance(activity = p0, isDark)
                applicationInstance.unregisterActivityLifecycleCallbacks(this)
            }
            override fun onActivityResumed(p0: Activity) {}
            override fun onActivityPaused(p0: Activity) {}
            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
            override fun onActivityStarted(p0: Activity) {}
            override fun onActivityStopped(p0: Activity) {}
            override fun onActivityDestroyed(p0: Activity) {}
        }
    )
}

private fun setWindowStatusBarAppearance(activity: Activity, isDark: Boolean) {
    WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
        isAppearanceLightStatusBars = isDark
    }
}