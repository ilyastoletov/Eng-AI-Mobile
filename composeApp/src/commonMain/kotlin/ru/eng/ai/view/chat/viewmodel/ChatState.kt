package ru.eng.ai.view.chat.viewmodel

import ru.eng.ai.model.Character
import ru.eng.ai.model.Message

private val defaultFastReplies = listOf("Hello!", "What about can I talk with you today?")

data class ChatState(
    val selectedCharacter: Character = Character.Traveler,
    val messages: List<Message> = emptyList(),
    val fastReplyOptions: List<String> = defaultFastReplies
)
