package ru.eng.ai.view.chat.viewmodel

import ru.eng.ai.model.Character

sealed interface ChatAction {
    data object RegisterOrLogin : ChatAction
    data class SelectCharacter(val newCharacter: Character) : ChatAction
    data class SendMessage(val text: String) : ChatAction
}