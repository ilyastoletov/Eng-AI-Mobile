package ru.eng.ai.view.chat.viewmodel

import org.jetbrains.compose.resources.StringResource

sealed interface ChatEffect {
    data object NoEffect : ChatEffect
    data class ShowSnackbar(
        val messageResource: StringResource,
        val formatArg: String = ""
    ) : ChatEffect
}