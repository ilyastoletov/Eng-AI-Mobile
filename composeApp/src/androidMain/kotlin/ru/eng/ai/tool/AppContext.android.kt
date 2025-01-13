package ru.eng.ai.tool

import android.app.Application
import android.content.Context

actual object AppContext {

    private lateinit var application: Application

    fun setup(context: Context) {
        application = context as Application
    }

    fun get(): Context {
        if (::application.isInitialized.not()) {
            throw IllegalStateException("Context is not initialized")
        }
        return application.applicationContext
    }

}