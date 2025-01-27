package ru.eng.ai.data.repository.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import ru.eng.ai.data.network.WebSocketSession
import ru.eng.ai.data.repository.chat.mapper.getRandomUUIDString
import ru.eng.ai.data.repository.chat.mapper.toEntity
import ru.eng.ai.data.repository.chat.mapper.toMessage
import ru.eng.ai.data.storage.dao.MessageDao
import ru.eng.ai.data.storage.dao.TokenDao
import ru.eng.ai.model.Character
import ru.eng.ai.model.Message
import ru.eng.ai.tool.getCurrentTimeAsClock

interface ChatRepository {
    val incomingMessages: Flow<Result<Message>>
    suspend fun getSavedMessages(character: Character): List<Message>
    suspend fun sendMessage(character: Character, text: String): Message
    suspend fun saveMessage(character: Character, message: Message)
    suspend fun togglePinOnMessage(messageId: String): Result<Unit>
}

internal class ChatRepositoryImpl(
    private val tokenDao: TokenDao,
    private val messagesDao: MessageDao,
    private val webSocketSession: WebSocketSession<Message>
) : ChatRepository {

    override val incomingMessages: Flow<Result<Message>>
        get() = webSocketSession.incoming

    override suspend fun getSavedMessages(character: Character): List<Message> {
        val now = Clock.System.now()
        val expirationTimestamp = messagesDao.getLatestExpirationTime() ?: now.epochSeconds
        return if (now.epochSeconds > expirationTimestamp) {
            messagesDao.clear()
            emptyList()
        } else {
            val characterName = character.getInternalName()
            messagesDao.getMessagesByCharacterName(characterName)
                .map { it.toMessage() }
        }
    }

    override suspend fun sendMessage(character: Character, text: String): Message {
        val authToken = tokenDao.getToken()?.token.orEmpty()
        val composedMessage = "$authToken:${character.getInternalName()}:$text"
        val message = buildOwnMessage(text)
        webSocketSession.send(composedMessage)
        saveMessage(character, message)
        return message
    }

    override suspend fun saveMessage(character: Character, message: Message) {
        val entity = message.toEntity(character.getInternalName())
        messagesDao.putMessage(entity)
    }

    override suspend fun togglePinOnMessage(messageId: String): Result<Unit> {
        return runCatching { messagesDao.togglePinOnMessage(messageId) }
    }

    private fun Character.getInternalName() =
         when(this) {
            Character.NativeSpeaker -> "Native Speaker"
            Character.Scientist -> "Professor"
            Character.Traveler -> "Traveler"
        }

    private fun buildOwnMessage(text: String): Message {
        return Message(
            id = getRandomUUIDString(),
            text = text,
            isOwn = true,
            sendingTime = getCurrentTimeAsClock(),
            isPinned = false
        )
    }

}