package ru.eng.ai.data.storage.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "token_store",
    indices = [Index("token", unique = true)]
)
data class TokenEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val token: String
)
