package ru.eng.ai.data.storage.room.provider

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import ru.eng.ai.data.storage.room.EngAppDatabase

expect fun getRoomDatabaseBuilder(): RoomDatabase.Builder<EngAppDatabase>

fun getRoomDatabase(): EngAppDatabase {
    return getRoomDatabaseBuilder()
        .fallbackToDestructiveMigration(dropAllTables = true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}