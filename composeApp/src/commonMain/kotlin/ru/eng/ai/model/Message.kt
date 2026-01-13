package ru.eng.ai.model

data class Message(
    val id: String,
    val text: String,
    val sendingTime: String,
    val isOwn: Boolean,
    val isPinned: Boolean = false,
    val isUndelivered: Boolean = false,
)
