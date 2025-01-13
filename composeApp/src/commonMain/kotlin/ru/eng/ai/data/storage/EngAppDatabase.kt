package ru.eng.ai.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.eng.ai.data.storage.dao.TokenDao
import ru.eng.ai.data.storage.entity.TokenEntity

@Database(entities = [TokenEntity::class], version = 1)
abstract class EngAppDatabase : RoomDatabase() {

    abstract fun getTokenDao(): TokenDao

}