package ru.eng.ai.data.repository.chat.remote

import io.ktor.client.call.body
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.eng.ai.data.network.KtorClient
import ru.eng.ai.data.repository.chat.remote.dto.AuthTokenDto
import ru.eng.ai.data.repository.chat.remote.dto.ChatHistoryDto
import ru.eng.ai.data.repository.chat.remote.dto.DeleteChatRequest
import ru.eng.ai.data.repository.chat.remote.dto.MessageDto
import ru.eng.ai.model.Character

private const val HISTORY_ENDPOINT = "get_user_chats"
private const val DELETE_CHAT_ENDPOINT = "delete_chat"

interface ChatRemoteDataSource {
    suspend fun getChatHistory(token: String, character: Character): Result<List<MessageDto>>
    suspend fun deleteChatHistory(token: String, character: Character): Result<Unit>
}

internal class ChatRemoteDataSourceImpl(
    private val ktorClient: KtorClient
) : ChatRemoteDataSource {

    override suspend fun getChatHistory(token: String, character: Character): Result<List<MessageDto>> =
        ktorClient.get(HISTORY_ENDPOINT) {
            contentType(ContentType.Application.Json)
            setBody(
                AuthTokenDto(token)
            )
        }.mapCatching { it.body<ChatHistoryDto>().getMessagesByCharacter(character) }

    override suspend fun deleteChatHistory(token: String, character: Character): Result<Unit> =
        ktorClient.delete(DELETE_CHAT_ENDPOINT) {
            contentType(ContentType.Application.Json)
            setBody(
                DeleteChatRequest(
                    token = token,
                    personalityName = character.internalName
                )
            )
        }.mapCatching {}

    private fun ChatHistoryDto.getMessagesByCharacter(character: Character): List<MessageDto> =
        when(character) {
            is Character.Traveler -> traveler
            is Character.Scientist -> professor
            is Character.NativeSpeaker -> nativeSpeaker
        }
}