package ru.eng.ai.data.storage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ru.eng.ai.data.storage.room.entity.MessageEntity

@Dao
interface MessageDao {

    @Insert(entity = MessageEntity::class)
    suspend fun putMessage(entity: MessageEntity)

    @Query("SELECT * FROM messages WHERE character_name = :name")
    suspend fun getMessagesByCharacterName(name: String): List<MessageEntity>

    @Query("SELECT expiration_timestamp FROM messages ORDER BY id desc LIMIT 1")
    suspend fun getLatestExpirationTime(): Long?

    @Query("SELECT * FROM messages WHERE id = :messageId LIMIT 1")
    suspend fun getMessageById(messageId: String): MessageEntity?

    @Query("UPDATE messages SET is_pinned = :newValue WHERE id = :messageId")
    suspend fun setIsPinnedOnMessageById(messageId: String, newValue: Boolean)

    @Query("DELETE FROM messages WHERE character_name = :character")
    suspend fun deleteMessagesByCharacter(character: String)

    @Transaction
    suspend fun togglePinOnMessage(messageId: String) {
        val messageEntity = getMessageById(messageId) ?: return
        setIsPinnedOnMessageById(
            messageId = messageId,
            newValue = !messageEntity.isPinned
        )
    }

    @Query("DELETE FROM messages")
    suspend fun clear()

}