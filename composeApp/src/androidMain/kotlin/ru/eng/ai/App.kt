package ru.eng.ai

import android.app.Application
import ru.eng.ai.tool.AppContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.setup(this)
    }

}