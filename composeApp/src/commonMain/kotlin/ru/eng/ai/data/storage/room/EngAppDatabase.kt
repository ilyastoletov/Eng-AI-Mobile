package ru.eng.ai.data.storage.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import ru.eng.ai.data.storage.room.dao.MessageDao
import ru.eng.ai.data.storage.room.dao.TokenDao
import ru.eng.ai.data.storage.room.entity.MessageEntity
import ru.eng.ai.data.storage.room.entity.TokenEntity
import ru.eng.ai.data.storage.room.provider.DatabaseConstructor

@Database(
    entities = [TokenEntity::class, MessageEntity::class],
    version = 4
)
@ConstructedBy(DatabaseConstructor::class)
abstract class EngAppDatabase : RoomDatabase() {

    abstract fun getTokenDao(): TokenDao

    abstract fun getMessagesDao(): MessageDao

}