package ru.eng.ai.view.chat.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.chat_delete_confirm
import engai.composeapp.generated.resources.chat_delete_decline
import engai.composeapp.generated.resources.chat_delete_dialog_text
import engai.composeapp.generated.resources.chat_delete_dialog_title
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import ru.eng.ai.model.Message
import ru.eng.ai.view.chat.screen.bottomsheet.FeedbackBottomSheet
import ru.eng.ai.view.chat.screen.components.ChatTopBar
import ru.eng.ai.view.chat.screen.components.MessageBar
import ru.eng.ai.view.chat.screen.components.MessageItem
import ru.eng.ai.view.chat.screen.bottomsheet.SelectCharacterBottomSheet
import ru.eng.ai.view.chat.screen.components.LimitReachedNotice
import ru.eng.ai.view.chat.screen.components.PinnedMessagesBar
import ru.eng.ai.view.chat.viewmodel.ChatAction
import ru.eng.ai.view.chat.viewmodel.ChatEffect
import ru.eng.ai.view.chat.viewmodel.ChatState
import ru.eng.ai.view.chat.viewmodel.ChatViewModel
import ru.eng.ai.view.theme.EngTheme

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val state by viewModel.container.stateFlow.collectAsState()
    val sideEffect by viewModel.container.sideEffectFlow.collectAsState(initial = ChatEffect.NoEffect)
    val handleIntent = remember { { intent: ChatAction -> viewModel.dispatch(intent) } }

    Screen(
        state = state,
        sideEffect = sideEffect,
        onIntent = handleIntent
    )

    LaunchedEffect(Unit) {
        handleIntent.invoke(ChatAction.RegisterOrLogin)
        handleIntent.invoke(ChatAction.CheckLimitReached)
    }
}

@Composable
private fun Screen(
    state: ChatState,
    sideEffect: ChatEffect,
    onIntent: (ChatAction) -> Unit
) {
    var characterChangeBottomSheetExpanded by remember { mutableStateOf(false) }
    var feedbackBottomSheetExpanded by remember { mutableStateOf(false) }
    var clearChatConfirmationDialogShown by remember { mutableStateOf(false) }

    val pinnedMessagesBarEnabled = remember(state.messages) {
        state.messages.any { it.isPinned }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.ime),
        containerColor = EngTheme.colors.primaryVariant,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                ChatTopBar(
                    character = state.selectedCharacter,
                    chatStatus = state.chatStatus,
                    onClickChangeCharacter = { characterChangeBottomSheetExpanded = true },
                    onClickClearChatHistory = { clearChatConfirmationDialogShown = true }
                )
                if (pinnedMessagesBarEnabled) {
                    PinnedMessagesBar(
                        messages = state.messages
                    )
                }
            }
        },
        bottomBar = {
            if (!state.isLimitReached) {
                MessageBar(
                    fastReplyOptions = state.fastReplyOptions,
                    isSendingAllowed = state.isSendingAllowed,
                    onSendMessage = { onIntent.invoke(ChatAction.SendMessage(it)) },
                )
            } else {
                LimitReachedNotice()
            }
        }
    ) { scaffoldPadding ->
        MessagesList(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            messages = state.messages,
            onCopyMessage = { text -> onIntent(ChatAction.CopyMessageText(text)) },
            onPinMessage = { messageId -> onIntent(ChatAction.PinMessage(messageId)) }
        )
    }

    if (characterChangeBottomSheetExpanded) {
        SelectCharacterBottomSheet(
            selected = state.selectedCharacter,
            onDismiss = { characterChangeBottomSheetExpanded = false },
            onSelect = { onIntent.invoke(ChatAction.SelectCharacter(it)) },
            onClickFeedback = { feedbackBottomSheetExpanded = true }
        )
    }

    if (feedbackBottomSheetExpanded) {
        FeedbackBottomSheet(
            onDismiss = { feedbackBottomSheetExpanded = false }
        )
    }

    if (clearChatConfirmationDialogShown) {
        DeleteChatDialog(
            onConfirm = {
                onIntent(ChatAction.ClearChatHistory)
                clearChatConfirmationDialogShown = false
            },
            onDismiss = { clearChatConfirmationDialogShown = false }
        )
    }

    LaunchedEffect(sideEffect) {
        when(sideEffect) {
            is ChatEffect.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = getString(sideEffect.messageResource, sideEffect.formatArg)
                )
            }
            is ChatEffect.NoEffect -> {}
        }
    }
}

@Composable
private fun MessagesList(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    onCopyMessage: (text: String) -> Unit,
    onPinMessage: (id: String) -> Unit
) {
    val lastMessageIndex = remember(messages.size) {
        messages.lastIndex.coerceAtLeast(0)
    }
    val lazyListState = rememberLazyListState( // Scroll to last message at start
        initialFirstVisibleItemIndex = lastMessageIndex,
        initialFirstVisibleItemScrollOffset = 100
    )

    LaunchedEffect(messages.size) {
        lazyListState.animateScrollToItem(
            index = lastMessageIndex,
            scrollOffset = 100
        )
    }

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(all = 25.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        items(items = messages) { item ->
            MessageItem(
                text = item.text,
                sendingTime = item.sendingTime,
                isOwn = item.isOwn,
                isPinned = item.isPinned,
                isUndelivered = item.isUndelivered,
                onCopy = { onCopyMessage(item.text) },
                onPin = { onPinMessage(item.id) }
            )
        }
    }
}

@Composable
private fun DeleteChatDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.chat_delete_dialog_title)
            )
        },
        text = {
            Text(
                text = stringResource(Res.string.chat_delete_dialog_text)
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = stringResource(Res.string.chat_delete_confirm)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(Res.string.chat_delete_decline)
                )
            }
        }
    )
}