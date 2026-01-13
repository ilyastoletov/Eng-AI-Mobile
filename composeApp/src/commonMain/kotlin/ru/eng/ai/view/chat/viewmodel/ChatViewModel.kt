package ru.eng.ai.view.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.StringResource
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
import ru.eng.ai.tool.Logger
import ru.eng.ai.tool.copyText
import ru.eng.ai.tool.getDeviceIdentifier
import ru.eng.ai.view.chat.viewmodel.enumeration.ChatStatus

private const val SESSION_RESTART_TIMEOUT = 5000L

class ChatViewModel(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) : ContainerHost<ChatState, ChatEffect>, ViewModel() {

    override val container: Container<ChatState, ChatEffect> = viewModelScope.container(ChatState())

    init {
        loadSavedMessages()
        collectIncomingMessages()
        watchIsSendingAllowed()
    }

    fun dispatch(action: ChatAction) {
        when(action) {
            is ChatAction.RegisterOrLogin -> registerOrLogin()
            is ChatAction.CheckLimitReached -> checkLimitReached()
            is ChatAction.SelectCharacter -> changeCharacter(action.newCharacter)
            is ChatAction.SendMessage -> sendMessage(action.text)
            is ChatAction.CopyMessageText -> copyMessageText(action.text)
            is ChatAction.PinMessage -> toggleMessagePin(action.messageId)
            is ChatAction.ClearChatHistory -> clearChatHistory()
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
            reduce { state.copy(isLimitReached = isReached) }
        }
    }

    private fun collectIncomingMessages() {
        chatRepository.incomingMessages
            .onEach { handleIncomingMessage(it) }
            .launchIn(viewModelScope)
    }

    private fun watchIsSendingAllowed() {
        viewModelScope.launch {
            container.stateFlow.collect { state ->
                val chatStatus = state.chatStatus
                val hasNoUndeliveredMessage = state.messages.none { it.isUndelivered }
                val isDeferredMessageAllowed = chatStatus == ChatStatus.WRITING && hasNoUndeliveredMessage
                val isSendingEnabled = chatStatus == ChatStatus.NONE || isDeferredMessageAllowed
                intent {
                    reduce { state.copy(isSendingAllowed = isSendingEnabled) }
                }
            }
        }
    }

    private fun handleIncomingMessage(messageResult: Result<Message>) = intent {
        messageResult.fold(
            onSuccess = { message ->
                chatRepository.saveMessage(state.selectedCharacter, message)
                chatRepository.incrementMessagesCount()
                appendMessage(message, status = ChatStatus.NONE)
            },
            onFailure = { handleChatError(it) }
        )
    }

    private fun loadSavedMessages() = intent {
        viewModelScope.launch(Dispatchers.IO) {
            val character = state.selectedCharacter
            val messages = chatRepository.getSavedMessages(character)
            reduce {
                if (messages.isNotEmpty()) {
                    state.copy(
                        messages = messages,
                        fastReplyOptions = Character.languageCheckFastReplies
                    )
                } else {
                    state.copy(
                        fastReplyOptions = character.fastReplies
                    )
                }
            }
        }
    }

    private fun sendMessage(text: String) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            val isOffline = state.chatStatus == ChatStatus.RECONNECT
            val sentMessage = chatRepository.sendMessage(state.selectedCharacter, text, isOffline)
            val newStatus = if (isOffline) state.chatStatus else ChatStatus.WRITING
            appendMessage(sentMessage, status = newStatus)
        }
    }

    private fun changeCharacter(newCharacter: Character) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            val savedMessages = chatRepository.getSavedMessages(newCharacter)
            val fastReplies = if (savedMessages.isEmpty()) {
                newCharacter.fastReplies
            } else {
                Character.languageCheckFastReplies
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
        postSideEffect(ChatEffect.ShowSnackbar(Res.string.copy_success_message))
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
                    postSideEffect(
                        ChatEffect.ShowSnackbar(Res.string.message_pin_error, error.message.orEmpty())
                    )
                }
            )
        }
    }

    private fun clearChatHistory() = intent {
        chatRepository.deleteChatHistory(character = state.selectedCharacter)
            .fold(
                onSuccess = {
                    reduce { state.copy(messages = emptyList()) }
                    postSideEffect(
                        sideEffect = ChatEffect.ShowSnackbar(
                            messageResource = Res.string.chat_delete_success,
                        )
                    )
                },
                onFailure = { error ->
                    Logger.d("ERROR", "$error ${error.message}")
                    postSideEffect(
                        sideEffect = ChatEffect.ShowSnackbar(
                            messageResource = Res.string.error_server_connection,
                            formatArg = error.message.orEmpty()
                        )
                    )
                }
            )
    }

    private suspend fun Syntax<ChatState, ChatEffect>.appendMessage(message: Message, status: ChatStatus) {
        reduce {
            val mutableMessages = state.messages.toMutableList()
            mutableMessages.add(message)
            state.copy(
                messages = mutableMessages,
                fastReplyOptions = Character.languageCheckFastReplies,
                chatStatus = status
            )
        }
    }

    private suspend fun Syntax<ChatState, ChatEffect>.handleChatError(error: Throwable) {
        val message: StringResource
        when(error) {
            is ChatClosedException -> {
                message = Res.string.error_connection_lost
                waitAndRestartSession()
                reduce { state.copy(chatStatus = ChatStatus.RECONNECT) }
            }
            is MessageLimitReachedException -> {
                message = Res.string.error_limit_exceeded
            }
            else -> {
                reduce { state.copy(chatStatus = ChatStatus.ERROR) }
                postSideEffect(
                    ChatEffect.ShowSnackbar(
                        messageResource = Res.string.error_server_connection,
                        formatArg = error.message.orEmpty()
                    )
                )
                return
            }
        }
        postSideEffect(ChatEffect.ShowSnackbar(message))
    }

    private fun waitAndRestartSession() {
        viewModelScope.launch {
            delay(SESSION_RESTART_TIMEOUT)
            handleUserUndeliveredMessage()
            getUndeliveredServerMessages()
            collectIncomingMessages()
        }.invokeOnCompletion {
            intent {
                reduce { state.copy(chatStatus = ChatStatus.NONE) }
            }
        }
    }

    private fun getUndeliveredServerMessages() = intent {
        withContext(Dispatchers.IO) {
            chatRepository.getUndeliveredMessages(state.selectedCharacter)
                .onSuccess { undelivered ->
                    reduce {
                        state.copy(
                            messages = state.messages
                                .toMutableList()
                                .apply { addAll(undelivered) }
                                .distinct()
                        )
                    }
                }
        }
    }

    private fun handleUserUndeliveredMessage() = intent {
        withContext(Dispatchers.IO) {
            chatRepository.sendUndeliveredUserMessage()
                .onSuccess {
                    reduce {
                        val newMessagesList = state.messages.map { it.copy(isUndelivered = false) }
                        state.copy(messages = newMessagesList)
                    }
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