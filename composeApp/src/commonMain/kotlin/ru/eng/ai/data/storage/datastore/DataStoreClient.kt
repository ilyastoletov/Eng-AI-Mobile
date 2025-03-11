package ru.eng.ai.data.storage.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface DataStoreClient {
    suspend fun getInt(key: String, default: Int): Int
    suspend fun setInt(key: String, value: Int)
    suspend fun getLong(key: String, default: Long): Long
    suspend fun setLong(key: String, value: Long)
    suspend fun clear()
}

internal class DataStorePreferencesClient(
    private val preferencesDataStore: DataStore<Preferences>
) : DataStoreClient {

    override suspend fun getInt(key: String, default: Int): Int {
        return getValue(intPreferencesKey(key), default)
    }

    override suspend fun setInt(key: String, value: Int) {
        setValue(intPreferencesKey(key), value)
    }

    override suspend fun getLong(key: String, default: Long): Long {
        return getValue(longPreferencesKey(key), default)
    }

    override suspend fun setLong(key: String, value: Long) {
        setValue(longPreferencesKey(key), value)
    }

    override suspend fun clear() {
        preferencesDataStore.edit { it.clear() }
    }

    private suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        preferencesDataStore.edit { it[key] = value }
    }

    private suspend fun <T> getValue(key: Preferences.Key<T>, default: T): T {
        return preferencesDataStore.data.map { it[key] ?: default }.first()
    }

}