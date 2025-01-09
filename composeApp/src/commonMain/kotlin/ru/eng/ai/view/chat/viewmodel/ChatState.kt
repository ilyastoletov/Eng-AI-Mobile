package ru.eng.ai.view.chat.viewmodel

import ru.eng.ai.model.Character
import ru.eng.ai.model.Message

data class ChatState(
    val selectedCharacter: Character,
    val messages: List<Message>,
    val fastReplyOptions: List<String>
) {
    companion object {
        val defaultState = ChatState(
            selectedCharacter = Character.Traveler,
            messages = emptyList(),
            fastReplyOptions = listOf("What can we talk with you today?")
        )
    }
}
