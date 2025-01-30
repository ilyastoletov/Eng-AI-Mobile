package ru.eng.ai

import android.app.Application
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import ru.eng.ai.tool.AppContext
import ru.eng.ai.tool.AppSecrets

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val appmetricaConfig =
            AppMetricaConfig.newConfigBuilder(AppSecrets.appmetricaKey).build()
        AppMetrica.activate(this, appmetricaConfig)
        AppContext.setup(this)
    }

}