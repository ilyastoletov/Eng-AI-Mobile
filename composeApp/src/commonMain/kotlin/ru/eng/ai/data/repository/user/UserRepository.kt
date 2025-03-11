package ru.eng.ai.data.repository.user

import io.ktor.client.call.body
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.eng.ai.data.network.KtorClient
import ru.eng.ai.data.repository.user.dto.AuthTokenResponse
import ru.eng.ai.data.repository.user.dto.LoginRequest
import ru.eng.ai.data.repository.user.mapper.toLoginRequest
import ru.eng.ai.data.storage.room.dao.TokenDao
import ru.eng.ai.data.storage.room.entity.TokenEntity

interface UserRepository {
    suspend fun registerOrLoginWithDeviceId(deviceId: String): Result<Unit>
}

internal class UserRepositoryImpl(
    private val httpClient: KtorClient,
    private val tokenDao: TokenDao
) : UserRepository {

    override suspend fun registerOrLoginWithDeviceId(deviceId: String): Result<Unit> {
        if (tokenDao.getToken() != null) {
            return Result.success(Unit)
        }

        val loginRequest = deviceId.toLoginRequest()
        val registrationResult = tryRetrieveUserToken("register", loginRequest)
        val authTokenResult = if (registrationResult.isFailure) {
            tryRetrieveUserToken("login", loginRequest)
        } else registrationResult
        authTokenResult.onSuccess { authToken ->
            tokenDao.insertToken(TokenEntity(token = authToken))
        }
        return authTokenResult.mapCatching {}
    }

    private suspend fun tryRetrieveUserToken(endpoint: String, payload: LoginRequest): Result<String> {
        return httpClient.post(endpoint) {
            contentType(ContentType.Application.Json)
            setBody(payload)
        }.mapCatching { it.body<AuthTokenResponse>().accessToken }
    }


}