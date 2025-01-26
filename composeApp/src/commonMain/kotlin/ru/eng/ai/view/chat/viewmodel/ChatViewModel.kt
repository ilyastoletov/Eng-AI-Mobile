package ru.eng.ai.view.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.Syntax
import ru.eng.ai.data.repository.chat.ChatRepository
import ru.eng.ai.data.repository.user.UserRepository
import ru.eng.ai.model.Character
import ru.eng.ai.model.Message
import ru.eng.ai.tool.getDeviceIdentifier

class ChatViewModel(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) : ContainerHost<ChatState, Nothing>, ViewModel() {

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
            userRepository.registerOrLoginWithDeviceId(deviceId)
        }
    }

    init {
        loadSavedMessages()
        chatRepository.incomingMessages
            .onEach { messageResult ->
                intent {
                    messageResult.fold(
                        onSuccess = { message ->
                            chatRepository.saveMessage(state.selectedCharacter, message)
                            appendMessage(message)
                        },
                        onFailure = { error ->
                            reduce { state.copy(snackbarMessage = error.message) }
                        }
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadSavedMessages() = intent {
        viewModelScope.launch(Dispatchers.IO) {
            val messages = chatRepository.getSavedMessages(state.selectedCharacter)
            if (messages.isNotEmpty()) {
                reduce {
                    state.copy(
                        messages = messages,
                        fastReplyOptions = emptyList()
                    )
                }
            }
        }
    }

    private fun sendMessage(text: String) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            val sentMessage = chatRepository.sendMessage(state.selectedCharacter, text)
            appendMessage(sentMessage)
        }
    }

    private fun changeCharacter(newCharacter: Character) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            val savedMessages = chatRepository.getSavedMessages(newCharacter)
            val fastReplies =
                if (savedMessages.isEmpty()) ChatState.defaultFastReplies else emptyList()

            reduce {
                state.copy(
                    selectedCharacter = newCharacter,
                    messages = savedMessages,
                    fastReplyOptions = fastReplies
                )
            }
        }
    }

    private suspend fun Syntax<ChatState, Nothing>.appendMessage(message: Message) {
        reduce {
            val mutableMessages = state.messages.toMutableList()
            mutableMessages.add(message)
            state.copy(
                messages = mutableMessages,
                fastReplyOptions = emptyList()
            )
        }
    }


}