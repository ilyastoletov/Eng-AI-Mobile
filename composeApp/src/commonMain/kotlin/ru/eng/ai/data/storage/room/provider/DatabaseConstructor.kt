package ru.eng.ai.data.storage.room.provider

import androidx.room.RoomDatabaseConstructor
import ru.eng.ai.data.storage.room.EngAppDatabase

expect object DatabaseConstructor : RoomDatabaseConstructor<EngAppDatabase> {
    override fun initialize(): EngAppDatabase
}