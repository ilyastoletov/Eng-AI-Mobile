package ru.eng.ai.data.storage.datastore.provider

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import ru.eng.ai.tool.AppContext

actual fun createDataStore(): DataStore<Preferences> {
    val appContext = AppContext.get()
    val dataStoreFile = appContext.preferencesDataStoreFile("main")
    return PreferenceDataStoreFactory.create { dataStoreFile }
}