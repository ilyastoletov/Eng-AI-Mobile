package ru.eng.ai.view.chat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.eng.ai.model.Message
import ru.eng.ai.view.chat.screen.components.ChatTopBar
import ru.eng.ai.view.chat.screen.components.MessageBar
import ru.eng.ai.view.chat.screen.components.SelectCharacterBottomSheet
import ru.eng.ai.view.chat.viewmodel.ChatAction
import ru.eng.ai.view.chat.viewmodel.ChatState
import ru.eng.ai.view.chat.viewmodel.ChatViewModel
import ru.eng.ai.view.theme.EngTheme

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val state by viewModel.container.refCountStateFlow.collectAsState()
    val handleIntent = remember { { intent: ChatAction -> viewModel.dispatch(intent) } }

    Screen(
        state = state,
        onIntent = handleIntent
    )

    LaunchedEffect(Unit) {
        handleIntent.invoke(ChatAction.RegisterOrLogin)
    }
}

@Composable
private fun Screen(
    state: ChatState,
    onIntent: (ChatAction) -> Unit
) {
    var characterChangeBottomSheetExpanded by remember {
        mutableStateOf(false)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.ime),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ChatTopBar(
                character = state.selectedCharacter,
                onClickChangeCharacter = { characterChangeBottomSheetExpanded = true },
            )
        },
        bottomBar = {
            MessageBar(
                fastReplyOptions = state.fastReplyOptions,
                onSendMessage = { onIntent.invoke(ChatAction.SendMessage(it)) },
            )
        },
        backgroundColor = EngTheme.colors.primaryVariant
    ) { scaffoldPadding ->
        MessagesList(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            messages = state.messages
        )
    }

    if (characterChangeBottomSheetExpanded) {
        SelectCharacterBottomSheet(
            selected = state.selectedCharacter,
            onDismiss = { characterChangeBottomSheetExpanded = false },
            onSelect = { onIntent.invoke(ChatAction.SelectCharacter(it)) }
        )
    }

    LaunchedEffect(state.snackbarMessage) {
        if (state.snackbarMessage != null) {
            snackbarHostState.showSnackbar(state.snackbarMessage)
        }
    }
}

@Composable
private fun MessagesList(
    modifier: Modifier = Modifier,
    messages: List<Message>
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
                isOwn = item.isOwn
            )
        }
    }
}

@Composable
private fun MessageItem(
    text: String,
    isOwn: Boolean,
    sendingTime: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isOwn) Alignment.End else Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = if (isOwn) EngTheme.colors.secondary else EngTheme.colors.primary,
                    shape = RoundedCornerShape(30.dp)
                )
                .padding(
                    vertical = 8.dp,
                    horizontal = 19.dp
                )
        ) {
            Text(
                text = text,
                style = EngTheme.typography.medium14,
                color = EngTheme.colors.dimSecondary,
                modifier = Modifier
            )
        }
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        Text(
            text = sendingTime,
            style = EngTheme.typography.medium12,
            color = EngTheme.colors.dimTertiary,
            textAlign = if (isOwn) TextAlign.End else TextAlign.Start,
            modifier = Modifier.padding(horizontal = 19.dp)
        )
    }
}