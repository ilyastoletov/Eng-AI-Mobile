package ru.eng.ai.data.storage.provider

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory
import ru.eng.ai.data.storage.EngAppDatabase

actual fun getRoomDatabaseBuilder(): RoomDatabase.Builder<EngAppDatabase> {
    val databasePath = NSHomeDirectory() + "/eng.db"
    return Room.databaseBuilder(name = databasePath)
}