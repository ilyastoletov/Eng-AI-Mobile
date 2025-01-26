package ru.eng.ai.data.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages"
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    @ColumnInfo("is_own") val isOwn: Boolean,
    @ColumnInfo("sending_time") val sendingTime: String,
    @ColumnInfo("character_name") val characterName: String,
    @ColumnInfo("expiration_timestamp") val expirationTimestamp: Long,
)
