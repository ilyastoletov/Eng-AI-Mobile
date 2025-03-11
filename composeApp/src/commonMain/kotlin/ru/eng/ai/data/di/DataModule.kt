package ru.eng.ai.data.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.eng.ai.data.network.KtorClient
import ru.eng.ai.data.network.WebSocketSession
import ru.eng.ai.data.repository.chat.ChatRepository
import ru.eng.ai.data.repository.chat.ChatRepositoryImpl
import ru.eng.ai.data.repository.chat.storage.MessageLimitController
import ru.eng.ai.data.repository.chat.storage.MessageLimitControllerImpl
import ru.eng.ai.data.repository.chat.ws.ChatWebsocketSession
import ru.eng.ai.data.repository.user.UserRepository
import ru.eng.ai.data.repository.user.UserRepositoryImpl
import ru.eng.ai.data.storage.datastore.DataStoreClient
import ru.eng.ai.data.storage.datastore.DataStorePreferencesClient
import ru.eng.ai.data.storage.datastore.provider.createDataStore
import ru.eng.ai.data.storage.room.EngAppDatabase
import ru.eng.ai.data.storage.room.dao.MessageDao
import ru.eng.ai.data.storage.room.dao.TokenDao
import ru.eng.ai.data.storage.room.provider.getRoomDatabase
import ru.eng.ai.model.Message

val DataModule = DI.Module(name = "data") {
    bindSingleton<KtorClient> { KtorClient() }
    bindSingleton<WebSocketSession<Message>> {
        ChatWebsocketSession(
            client = instance(),
            limitController = instance()
        )
    }
    bindSingleton<DataStoreClient> {
        DataStorePreferencesClient(preferencesDataStore = createDataStore())
    }
    bindSingleton<EngAppDatabase> { getRoomDatabase() }
    bindSingleton<TokenDao> { instance<EngAppDatabase>().getTokenDao() }
    bindSingleton<MessageDao> { instance<EngAppDatabase>().getMessagesDao() }
    bindSingleton<MessageLimitController> { MessageLimitControllerImpl(dataStoreClient = instance()) }

    bindSingleton<UserRepository> {
        UserRepositoryImpl(
            httpClient = instance(),
            tokenDao = instance()
        )
    }
    bindSingleton<ChatRepository> {
        ChatRepositoryImpl(
            tokenDao = instance(),
            messagesDao = instance(),
            webSocketSession = instance(),
            messageLimitController = instance()
        )
    }
}