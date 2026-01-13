package ru.eng.ai.data.repository.chat.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatHistoryDto(
    @SerialName("native_speaker")
    val nativeSpeaker: List<MessageDto>,
    val professor: List<MessageDto>,
    val traveler: List<MessageDto>
)
