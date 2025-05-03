package ru.eng.ai.view.chat.viewmodel

import ru.eng.ai.model.Character
import ru.eng.ai.model.Message
import ru.eng.ai.view.chat.viewmodel.enumeration.ChatStatus

data class ChatState(
    val selectedCharacter: Character = Character.Traveler,
    val messages: List<Message> = emptyList(),
    val fastReplyOptions: List<String> = defaultFastReplies,
    val limitReached: Boolean = false,
    val chatStatus: ChatStatus = ChatStatus.NONE,
) {
    companion object {
        val defaultFastReplies = listOf("Hello!", "What about can I talk with you today?")
        val activeDialogFastReplies = listOf("Grammar Check", "Spelling Check")
    }
}
