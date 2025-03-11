package ru.eng.ai.data.storage.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages"
)
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    @ColumnInfo("is_own") val isOwn: Boolean,
    @ColumnInfo("is_pinned") val isPinned: Boolean,
    @ColumnInfo("sending_time") val sendingTime: String,
    @ColumnInfo("character_name") val characterName: String,
    @ColumnInfo("expiration_timestamp") val expirationTimestamp: Long,
)
