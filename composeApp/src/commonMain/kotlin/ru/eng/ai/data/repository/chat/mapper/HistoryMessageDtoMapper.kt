package ru.eng.ai.data.repository.chat.mapper

import ru.eng.ai.data.repository.chat.remote.dto.MessageDto
import ru.eng.ai.model.Message
import ru.eng.ai.tool.getCurrentTimeAsClock

fun List<MessageDto>.toHistoryMessages(): List<Message> {
    return this.map { messageDto ->
        Message(
            id = getRandomUUIDString(),
            text = messageDto.message,
            sendingTime = getCurrentTimeAsClock(),
            isOwn = messageDto.role == "user",
            isPinned = false
        )
    }
}