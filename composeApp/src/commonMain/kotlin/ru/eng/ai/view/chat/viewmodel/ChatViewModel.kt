package ru.eng.ai.view.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import ru.eng.ai.data.repository.user.UserRepository
import ru.eng.ai.model.Character
import ru.eng.ai.model.Message
import ru.eng.ai.tool.Logger
import ru.eng.ai.tool.getDeviceIdentifier

class ChatViewModel(
    private val userRepository: UserRepository
) : ContainerHost<ChatState, Nothing>, ViewModel() {

    private val messageStorage = listOf(
        Message(text = "I think perfect topic for today is a trip to Rome. What do you think about this city?", sendingTime = "11:52", isOwn = false),
        Message(text = "Okay, let's just chat about something", sendingTime = "20:00", isOwn = false),
        Message(text = "As an artificial intelligence model I cannot provide you with some topics to chat with me about. Choose something you want.", sendingTime = "14:11", isOwn = false),
        Message(text = "We can talk about astrophysics and catholic churches. Also I am currently studying quantum mechanics in my university and since that very curious about this topic", sendingTime = "12:01", isOwn = false),
    )

    override val container: Container<ChatState, Nothing> = viewModelScope.container(ChatState())

    fun dispatch(action: ChatAction) {
        when(action) {
            is ChatAction.RegisterOrLogin -> registerOrLogin()
            is ChatAction.SendMessage -> sendMessage(action.text)
            is ChatAction.SelectCharacter -> changeCharacter(action.newCharacter)
        }
    }

    private fun registerOrLogin() = intent {
        withContext(Dispatchers.IO) {
            val deviceId = getDeviceIdentifier()
            Logger.i("Device_ID", deviceId)
            userRepository.registerOrLoginWithDeviceId(deviceId)
        }
    }

    private fun sendMessage(text: String) = intent {
        viewModelScope.launch {
            reduce {
                val updatedMessages = state.messages.toMutableList()
                    .apply { add(Message(text = text, sendingTime = "14:36", isOwn = true)) }
                state.copy(messages = updatedMessages, fastReplyOptions = emptyList())
            }
            delay(1600)
            reduce {
                val updatedMessages = state.messages.toMutableList().apply {
                    add(messageStorage.shuffled().first())
                }
                state.copy(messages = updatedMessages)
            }
        }
    }

    private fun changeCharacter(newCharacter: Character) = intent {
        reduce {
            state.copy(
                selectedCharacter = newCharacter,
                messages = emptyList()
            )
        }
    }


}