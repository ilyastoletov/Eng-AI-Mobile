package ru.eng.ai.data.repository.chat

import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import ru.eng.ai.data.network.WebSocketSession
import ru.eng.ai.data.repository.chat.mapper.getRandomUUIDString
import ru.eng.ai.data.repository.chat.mapper.toEntity
import ru.eng.ai.data.repository.chat.mapper.toHistoryMessages
import ru.eng.ai.data.repository.chat.mapper.toMessage
import ru.eng.ai.data.repository.chat.remote.ChatRemoteDataSource
import ru.eng.ai.data.repository.chat.storage.MessageLimitController
import ru.eng.ai.data.storage.room.dao.MessageDao
import ru.eng.ai.data.storage.room.dao.TokenDao
import ru.eng.ai.model.Character
import ru.eng.ai.model.Message
import ru.eng.ai.tool.getCurrentTimeAsClock

interface ChatRepository {
    val incomingMessages: Flow<Result<Message>>
    suspend fun getSavedMessages(character: Character): List<Message>
    suspend fun sendMessage(character: Character, text: String, isOffline: Boolean): Message
    suspend fun saveMessage(character: Character, message: Message)
    suspend fun deleteChatHistory(character: Character): Result<Unit>
    suspend fun togglePinOnMessage(messageId: String): Result<Unit>
    suspend fun sendUndeliveredUserMessage(): Result<Unit>
    suspend fun getUndeliveredMessages(character: Character): Result<List<Message>>
    suspend fun isMessageLimitReached(): Boolean
    suspend fun incrementMessagesCount()
}

internal class ChatRepositoryImpl(
    private val tokenDao: TokenDao,
    private val messagesDao: MessageDao,
    private val webSocketSession: WebSocketSession<Message>,
    private val remoteDataSource: ChatRemoteDataSource,
    private val messageLimitController: MessageLimitController
) : ChatRepository {

    private var undeliveredUserMessage: String? = null

    override val incomingMessages: Flow<Result<Message>>
        get() = webSocketSession.incoming

    override suspend fun getSavedMessages(character: Character): List<Message> {
        val now = Clock.System.now()
        val expirationTimestamp = messagesDao.getLatestExpirationTime() ?: now.epochSeconds
        return if (now.epochSeconds > expirationTimestamp) {
            messageLimitController.reset()
            messagesDao.clear()
            emptyList()
        } else {
            val characterName = character.getInternalName()
            messagesDao.getMessagesByCharacterName(characterName)
                .map { it.toMessage() }
        }
    }

    override suspend fun sendMessage(character: Character, text: String, isOffline: Boolean): Message {
        val authToken = tokenDao.getToken()?.token.orEmpty()
        val chatType = getChatType(messageText = text)
        val composedMessage = "$authToken:${character.getInternalName()}:$text:$chatType"
        val message = buildOwnMessage(text, isOffline)
        if (!isOffline) {
            webSocketSession.send(composedMessage)
        } else {
            undeliveredUserMessage = composedMessage
        }
        saveMessage(character, message)
        return message
    }

    override suspend fun saveMessage(character: Character, message: Message) {
        val entity = message.toEntity(character.getInternalName())
        messagesDao.putMessage(entity)
    }

    override suspend fun deleteChatHistory(character: Character): Result<Unit> {
        val token = tokenDao.getToken()?.token.orEmpty()
        return remoteDataSource.deleteChatHistory(token, character)
            .onSuccess {
                messagesDao.deleteMessagesByCharacter(
                    character = character.getInternalName()
                )
            }
    }

    override suspend fun togglePinOnMessage(messageId: String): Result<Unit> {
        return runCatching { messagesDao.togglePinOnMessage(messageId) }
    }

    override suspend fun sendUndeliveredUserMessage(): Result<Unit> = runCatching {
        undeliveredUserMessage?.let {
            webSocketSession.send(it)
            undeliveredUserMessage = null
        }
    }

    override suspend fun getUndeliveredMessages(character: Character): Result<List<Message>> {
        val authToken = tokenDao.getToken()?.token ?: return Result.success(emptyList())
        return remoteDataSource.getChatHistory(authToken, character)
            .mapCatching { it.toHistoryMessages() }
            .onSuccess { mappedMessages ->
                val messageEntities = mappedMessages.map { it.toEntity(character.getInternalName()) }
                messageEntities.forEach { msg -> messagesDao.putMessage(msg) }
            }
    }

    override suspend fun isMessageLimitReached(): Boolean {
        return messageLimitController.isMessageLimitReached()
    }

    override suspend fun incrementMessagesCount() {
        messageLimitController.incrementMessagesCount()
    }

    private fun Character.getInternalName() =
         when(this) {
            Character.NativeSpeaker -> "Native Speaker"
            Character.Scientist -> "Professor"
            Character.Traveler -> "Traveler"
        }

    private fun buildOwnMessage(text: String, isOffline: Boolean): Message {
        return Message(
            id = getRandomUUIDString(),
            text = text,
            isOwn = true,
            sendingTime = getCurrentTimeAsClock(),
            isPinned = false,
            isUndelivered = isOffline
        )
    }

    private fun getChatType(messageText: String): String = when(messageText) {
        "Grammar Check" -> "grammar"
        "Spelling Check" -> "spelling"
        else -> "chat"
    }
}