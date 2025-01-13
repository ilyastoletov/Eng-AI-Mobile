package ru.eng.ai.data.storage.provider

import androidx.room.Room
import androidx.room.RoomDatabase
import ru.eng.ai.data.storage.EngAppDatabase
import ru.eng.ai.tool.AppContext

actual fun getRoomDatabaseBuilder(): RoomDatabase.Builder<EngAppDatabase> {
    val context = AppContext.get()
    val databaseFile = context.getDatabasePath("eng.db")
    return Room.databaseBuilder(context, databaseFile.absolutePath)
}