package ru.eng.ai.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.eng.ai.tool.AppSecrets

internal class KtorClient {

    private val client = HttpClient {
        defaultRequest {
            url(AppSecrets.apiUrl)
        }
        expectSuccess = true

        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true }
            )
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.SIMPLE
        }
        install(WebSockets)
    }

    suspend fun get(url: String, block: HttpRequestBuilder.() -> Unit): Result<HttpResponse> =
        runCatching { client.get(url, block) }

    suspend fun post(url: String, block: HttpRequestBuilder.() -> Unit): Result<HttpResponse> =
        runCatching { client.post(url, block) }

    suspend fun openWebsocketSession(urlString: String): DefaultClientWebSocketSession =
        client.webSocketSession(urlString = urlString)

}