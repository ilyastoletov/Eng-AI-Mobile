package ru.eng.ai.view.chat.viewmodel

import org.jetbrains.compose.resources.StringResource
import ru.eng.ai.model.Character
import ru.eng.ai.model.Message
import ru.eng.ai.view.chat.viewmodel.enumeration.ChatStatus

data class ChatState(
    val selectedCharacter: Character = Character.Traveler,
    val messages: List<Message> = emptyList(),
    val fastReplyOptions: List<StringResource> = listOf(),
    val chatStatus: ChatStatus = ChatStatus.NONE,
    val isLimitReached: Boolean = false,
    val isSendingAllowed: Boolean = true,
)
