package ru.eng.ai.data.network

import kotlinx.coroutines.flow.Flow

interface WebSocketSession<T> {
    val incoming: Flow<Result<T>>
    suspend fun send(content: String)
    suspend fun close()
}