package ru.eng.ai.data.repository.chat.mapper

import ru.eng.ai.data.storage.entity.MessageEntity
import ru.eng.ai.model.Message
import ru.eng.ai.tool.getTomorrowTimestamp

internal fun Message.toEntity(characterName: String): MessageEntity {
    return MessageEntity(
        text = text,
        isOwn = isOwn,
        sendingTime = sendingTime,
        characterName = characterName,
        expirationTimestamp = getTomorrowTimestamp()
    )
}

internal fun MessageEntity.toMessage(): Message =
    Message(
        text = text,
        sendingTime = sendingTime,
        isOwn = isOwn
    )