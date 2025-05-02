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
import ru.eng.ai.data.repository.chat.storage.MessageLimitController
import ru.eng.ai.exception.EmptyMessageException
import ru.eng.ai.exception.MessageLimitReachedException
import ru.eng.ai.model.Message
import ru.eng.ai.tool.AppSecrets
import ru.eng.ai.tool.getCurrentTimeAsClock
import kotlin.coroutines.cancellation.CancellationException

internal class ChatWebsocketSession(
    private val client: KtorClient,
    private val limitController: MessageLimitController
) : WebSocketSession<Message> {

    private var innerSession: DefaultClientWebSocketSession? = null

    override val incoming: Flow<Result<Message>> = flow {
        val ws = client.openWebsocketSession(WS_URL)
        innerSession = ws
        emitAll(
            ws.incoming.receiveAsFlow().map(::checkLimitAndMap)
        )
    }.catch { emit(Result.failure(it)) }

    override suspend fun send(content: String) {
        innerSession?.send(content)
    }

    override suspend fun close() {
        innerSession?.close()
    }

    private suspend fun checkLimitAndMap(frame: Frame): Result<Message> =
        if (limitController.isMessageLimitReached()) {
            Result.failure(MessageLimitReachedException)
        } else {
            frame.toMessageResult()
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
                is Frame.Ping -> throw EmptyMessageException
                is Frame.Pong -> throw EmptyMessageException
                is Frame.Binary -> this.data.decodeToString()
                is Frame.Text -> this.readText()
                else -> throw EmptyMessageException
            }
        }

    companion object {
        private val WS_URL = AppSecrets.chatWebsocketsUrl
    }

}