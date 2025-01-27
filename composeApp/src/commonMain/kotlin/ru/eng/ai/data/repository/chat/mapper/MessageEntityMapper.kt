package ru.eng.ai.data.repository.chat.mapper

import ru.eng.ai.data.storage.entity.MessageEntity
import ru.eng.ai.model.Message
import ru.eng.ai.tool.getTomorrowTimestamp
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal fun Message.toEntity(characterName: String): MessageEntity {
    return MessageEntity(
        id = id,
        text = text,
        isOwn = isOwn,
        sendingTime = sendingTime,
        characterName = characterName,
        isPinned = false,
        expirationTimestamp = getTomorrowTimestamp()
    )
}

internal fun MessageEntity.toMessage(): Message =
    Message(
        id = id,
        text = text,
        sendingTime = sendingTime,
        isOwn = isOwn,
        isPinned = isPinned
    )

@OptIn(ExperimentalUuidApi::class)
internal fun getRandomUUIDString(): String = Uuid.random().toHexString()