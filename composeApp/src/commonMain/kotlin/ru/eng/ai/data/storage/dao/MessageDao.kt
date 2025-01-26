package ru.eng.ai.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.eng.ai.data.storage.entity.MessageEntity

@Dao
interface MessageDao {

    @Insert(entity = MessageEntity::class)
    suspend fun putMessage(entity: MessageEntity)

    @Query("SELECT * FROM messages WHERE character_name = :name")
    suspend fun getMessagesByCharacterName(name: String): List<MessageEntity>

    @Query("SELECT expiration_timestamp FROM messages ORDER BY id desc LIMIT 1")
    suspend fun getLatestExpirationTime(): Long?

    @Query("DELETE FROM messages")
    suspend fun clear()

}