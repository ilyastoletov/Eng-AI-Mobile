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
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.eng.ai.model.Message
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
    var characterChangeBottomSheetExpanded by remember {
        mutableStateOf(false)
    }
    val pinnedMessagesBarEnabled = remember(state.messages) {
        state.messages.any { it.isPinned }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.ime),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                ChatTopBar(
                    character = state.selectedCharacter,
                    onClickChangeCharacter = { characterChangeBottomSheetExpanded = true },
                )
                if (pinnedMessagesBarEnabled) {
                    PinnedMessagesBar(
                        messages = state.messages
                    )
                }
            }
        },
        bottomBar = {
            if (!state.limitReached) {
                MessageBar(
                    fastReplyOptions = state.fastReplyOptions,
                    onSendMessage = { onIntent.invoke(ChatAction.SendMessage(it)) },
                )
            } else {
                LimitReachedNotice()
            }
        },
        backgroundColor = EngTheme.colors.primaryVariant
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
            onSelect = { onIntent.invoke(ChatAction.SelectCharacter(it)) }
        )
    }

    LaunchedEffect(sideEffect) {
        when(sideEffect) {
            is ChatEffect.ShowSnackbar -> {
                snackbarHostState.showSnackbar(message = sideEffect.message)
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
                onCopy = { onCopyMessage(item.text) },
                onPin = { onPinMessage(item.id) }
            )
        }
    }
}