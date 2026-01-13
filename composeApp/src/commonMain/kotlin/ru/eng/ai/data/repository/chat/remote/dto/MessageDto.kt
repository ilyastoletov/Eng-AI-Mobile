package ru.eng.ai.data.repository.chat.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val role: String,
    val message: String
)
