package ru.eng.ai.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.eng.ai.data.storage.dao.MessageDao
import ru.eng.ai.data.storage.dao.TokenDao
import ru.eng.ai.data.storage.entity.MessageEntity
import ru.eng.ai.data.storage.entity.TokenEntity

@Database(
    entities = [TokenEntity::class, MessageEntity::class],
    version = 4
)
abstract class EngAppDatabase : RoomDatabase() {

    abstract fun getTokenDao(): TokenDao

    abstract fun getMessagesDao(): MessageDao

}