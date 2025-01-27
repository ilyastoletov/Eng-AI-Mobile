package ru.eng.ai.view.chat.viewmodel

sealed interface ChatEffect {
    data object NoEffect : ChatEffect
    data class ShowSnackbar(val message: String) : ChatEffect
}