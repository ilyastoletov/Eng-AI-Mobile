package ru.eng.ai

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.eng.ai.view.chat.screen.ChatScreen
import ru.eng.ai.view.chat.viewmodel.ChatViewModel

import ru.eng.ai.view.theme.EngCustomTheme

@Composable
fun EngApp() {
    EngCustomTheme {
        ChatScreen(
            viewModel = viewModel { ChatViewModel() }
        )
    }
}