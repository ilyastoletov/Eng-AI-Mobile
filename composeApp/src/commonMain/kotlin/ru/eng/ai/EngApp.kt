package ru.eng.ai

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import org.kodein.di.DI
import org.kodein.di.instance
import org.kodein.di.newInstance
import ru.eng.ai.data.di.DataModule
import ru.eng.ai.view.chat.screen.ChatScreen
import ru.eng.ai.view.chat.viewmodel.ChatViewModel
import ru.eng.ai.view.theme.EngCustomTheme

private val di = DI {
    import(DataModule)
}

@Composable
fun EngApp() {
    EngCustomTheme {
        val chatViewModel by di.newInstance { ChatViewModel(userRepository = instance()) }

        ChatScreen(
            viewModel = viewModel { chatViewModel }
        )
    }
}