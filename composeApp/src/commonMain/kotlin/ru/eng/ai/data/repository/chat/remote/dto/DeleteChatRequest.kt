package ru.eng.ai.data.repository.chat.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DeleteChatRequest(
    val token: String,
    @SerialName("personality_name")
    val personalityName: String
)
