package ru.eng.ai.data.repository.user.mapper

import ru.eng.ai.data.repository.user.dto.LoginRequest

internal fun String.toLoginRequest(): LoginRequest {
    return LoginRequest(
        username = "ilya",
        password = this
    )
}