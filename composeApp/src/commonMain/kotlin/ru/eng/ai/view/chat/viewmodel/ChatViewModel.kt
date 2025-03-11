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
import ru.eng.ai.tool.copyText
import ru.eng.ai.tool.getDeviceIdentifier

class ChatViewModel(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) : ContainerHost<ChatState, ChatEffect>, ViewModel() {

    override val container: Container<ChatState, ChatEffect> = viewModelScope.container(ChatState())

    fun dispatch(action: ChatAction) {
        when(action) {
            is ChatAction.RegisterOrLogin -> registerOrLogin()
            is ChatAction.CheckLimitReached -> checkLimitReached()
            is ChatAction.SelectCharacter -> changeCharacter(action.newCharacter)
            is ChatAction.SendMessage -> sendMessage(action.text)
            is ChatAction.CopyMessageText -> copyMessageText(action.text)
            is ChatAction.PinMessage -> toggleMessagePin(action.messageId)
        }
    }

    private fun registerOrLogin() = intent {
        withContext(Dispatchers.IO) {
            val deviceId = getDeviceIdentifier()
            userRepository.registerOrLoginWithDeviceId(deviceId)
        }
    }

    private fun checkLimitReached() = intent {
        withContext(Dispatchers.IO) {
            val isReached = chatRepository.isMessageLimitReached()
            reduce { state.copy(limitReached = isReached) }
        }
    }

    init {
        loadSavedMessages()
        chatRepository.incomingMessages
            .onEach { handleIncomingMessage(it) }
            .launchIn(viewModelScope)
    }

    private fun handleIncomingMessage(messageResult: Result<Message>) = intent {
        messageResult.fold(
            onSuccess = { message ->
                chatRepository.saveMessage(state.selectedCharacter, message)
                chatRepository.incrementMessagesCount()
                appendMessage(message)
            },
            onFailure = { error ->
                val message = "Ошибка соединения с сервером: ${error.message}"
                postSideEffect(ChatEffect.ShowSnackbar(message))
            }
        )
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

    private fun copyMessageText(text: String) = intent {
        copyText(text)
        postSideEffect(ChatEffect.ShowSnackbar("Текст сообщения скопирован"))
    }

    private fun toggleMessagePin(messageId: String) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.togglePinOnMessage(messageId).fold(
                onSuccess = {
                    reduce {
                        val updatedList = togglePinOnMessage(state.messages, messageId)
                        state.copy(messages = updatedList)
                    }
                },
                onFailure = { error ->
                    postSideEffect(ChatEffect.ShowSnackbar("Ошибка закрепления: ${error.message}"))
                }
            )
        }
    }

    private suspend fun Syntax<ChatState, ChatEffect>.appendMessage(message: Message) {
        reduce {
            val mutableMessages = state.messages.toMutableList()
            mutableMessages.add(message)
            state.copy(
                messages = mutableMessages,
                fastReplyOptions = emptyList()
            )
        }
    }

    private fun togglePinOnMessage(messages: List<Message>, id: String): List<Message> {
        val mutableMessages = messages.toMutableList()
        val message = mutableMessages.find { it.id == id } ?: return messages
        val itemIndex = mutableMessages.indexOf(message)
        val updatedMessage = message.copy(isPinned = !message.isPinned)
        mutableMessages.set(itemIndex, updatedMessage)
        return mutableMessages
    }


}