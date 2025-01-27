package ru.eng.ai.data.repository.chat.ws

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import ru.eng.ai.data.network.KtorClient
import ru.eng.ai.data.network.WebSocketSession
import ru.eng.ai.data.repository.chat.mapper.getRandomUUIDString
import ru.eng.ai.model.Message
import ru.eng.ai.tool.getCurrentTimeAsClock
import kotlin.coroutines.cancellation.CancellationException

internal class ChatWebsocketSession(private val client: KtorClient) : WebSocketSession<Message> {

    private var innerSession: DefaultClientWebSocketSession? = null

    override val incoming: Flow<Result<Message>> = flow {
        val ws = client.openWebsocketSession(WS_URL)
        innerSession = ws
        emitAll(
            ws.incoming.receiveAsFlow()
                .map { it.toMessageResult() }
        )
    }.catch { emit(Result.failure(it)) }

    override suspend fun send(content: String) {
        innerSession?.send(content)
    }

    override suspend fun close() {
        innerSession?.close()
    }

    private fun Frame.toMessageResult(): Result<Message> =
        decodeToStringResult().mapCatching { text ->
            Message(
                id = getRandomUUIDString(),
                text = text,
                sendingTime = getCurrentTimeAsClock(),
                isOwn = false
            )
        }

    private fun Frame.decodeToStringResult(): Result<String> =
        runCatching {
            when (this) {
                is Frame.Close -> throw CancellationException(this.data.decodeToString())
                is Frame.Ping -> throw NoData
                is Frame.Pong -> throw NoData
                is Frame.Binary -> this.data.decodeToString()
                is Frame.Text -> this.readText()
                else -> throw NoData
            }
        }

    companion object {
        private const val WS_URL = "ws://103.90.75.40:6464/api/ws/chat"
    }

}