package ru.eng.ai.data.storage.provider

import androidx.room.RoomDatabaseConstructor
import ru.eng.ai.data.storage.EngAppDatabase

expect object DatabaseConstructor : RoomDatabaseConstructor<EngAppDatabase> {
    override fun initialize(): EngAppDatabase
}