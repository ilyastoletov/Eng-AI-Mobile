package ru.eng.ai.data.repository.chat.storage

import ru.eng.ai.data.storage.datastore.DataStoreClient

interface MessageLimitController {
    suspend fun incrementMessagesCount()
    suspend fun isMessageLimitReached(): Boolean
    suspend fun reset()
}

private const val MESSAGE_LIMIT_KEY = "message_limit"
private const val MESSAGE_LIMIT = 100

internal class MessageLimitControllerImpl(
    private val dataStoreClient: DataStoreClient
) : MessageLimitController {

    override suspend fun incrementMessagesCount() {
        val value = dataStoreClient.getInt(MESSAGE_LIMIT_KEY, default = 0)
        dataStoreClient.setInt(MESSAGE_LIMIT_KEY, value + 1)
    }

    override suspend fun isMessageLimitReached(): Boolean {
        val value = dataStoreClient.getInt(MESSAGE_LIMIT_KEY, default = 0)
        return value >= MESSAGE_LIMIT
    }

    override suspend fun reset() {
        dataStoreClient.setInt(MESSAGE_LIMIT_KEY, value = 0)
    }
}