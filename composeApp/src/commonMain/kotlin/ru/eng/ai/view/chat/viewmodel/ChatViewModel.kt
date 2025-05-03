package ru.eng.ai.view.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
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
import ru.eng.ai.exception.ChatClosedException
import ru.eng.ai.exception.MessageLimitReachedException
import ru.eng.ai.model.Character
import ru.eng.ai.model.Message
import ru.eng.ai.tool.copyText
import ru.eng.ai.tool.getDeviceIdentifier
import ru.eng.ai.view.chat.viewmodel.enumeration.ChatStatus

private const val SESSION_RESTART_TIMEOUT = 5000L

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
        collectIncomingMessages()
    }

    private fun collectIncomingMessages() {
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
                reduce { state.copy(chatStatus = ChatStatus.NONE) }
            },
            onFailure = { handleChatError(it) }
        )
    }

    private fun loadSavedMessages() = intent {
        viewModelScope.launch(Dispatchers.IO) {
            val messages = chatRepository.getSavedMessages(state.selectedCharacter)
            if (messages.isNotEmpty()) {
                reduce {
                    state.copy(
                        messages = messages,
                        fastReplyOptions = ChatState.activeDialogFastReplies
                    )
                }
            }
        }
    }

    private fun sendMessage(text: String) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            val sentMessage = chatRepository.sendMessage(state.selectedCharacter, text)
            appendMessage(sentMessage)
            reduce { state.copy(chatStatus = ChatStatus.WRITING) }
        }
    }

    private fun changeCharacter(newCharacter: Character) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            val savedMessages = chatRepository.getSavedMessages(newCharacter)
            val fastReplies = if (savedMessages.isEmpty()) {
                ChatState.defaultFastReplies
            } else {
                ChatState.activeDialogFastReplies
            }

            reduce {
                state.copy(
                    selectedCharacter = newCharacter,
                    messages = savedMessages,
                    fastReplyOptions = fastReplies,
                    chatStatus = ChatStatus.NONE
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
                fastReplyOptions = ChatState.activeDialogFastReplies
            )
        }
    }

    private suspend fun Syntax<ChatState, ChatEffect>.handleChatError(error: Throwable) {
        val message: String
        when(error) {
            is ChatClosedException -> {
                message = "Потеряно соединение с сервером.\nПовторное подключение..."
                waitAndRestartSession()
                reduce { state.copy(chatStatus = ChatStatus.RECONNECT) }
            }
            is MessageLimitReachedException -> {
                message = "Превышен лимит отправки сообщений.\nВозвращайтесь через 24 часа"
            }
            else -> {
                message = "Ошибка соединения с сервером: ${error.message}"
                reduce { state.copy(chatStatus = ChatStatus.ERROR) }
            }
        }
        postSideEffect(ChatEffect.ShowSnackbar(message))
    }

    private fun waitAndRestartSession() {
        viewModelScope.launch {
            delay(SESSION_RESTART_TIMEOUT)
            collectIncomingMessages()
        }.invokeOnCompletion {
            intent {
                reduce { state.copy(chatStatus = ChatStatus.NONE) }
            }
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