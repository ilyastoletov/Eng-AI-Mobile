package ru.eng.ai.model

data class Message(
    val text: String,
    val sendingTime: String,
    val isOwn: Boolean,
)
