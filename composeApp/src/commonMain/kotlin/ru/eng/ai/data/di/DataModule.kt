package ru.eng.ai.data.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.eng.ai.data.network.KtorClient
import ru.eng.ai.data.network.WebSocketSession
import ru.eng.ai.data.repository.chat.ChatRepository
import ru.eng.ai.data.repository.chat.ChatRepositoryImpl
import ru.eng.ai.data.repository.chat.ws.ChatWebsocketSession
import ru.eng.ai.data.repository.user.UserRepository
import ru.eng.ai.data.repository.user.UserRepositoryImpl
import ru.eng.ai.data.storage.EngAppDatabase
import ru.eng.ai.data.storage.dao.MessageDao
import ru.eng.ai.data.storage.dao.TokenDao
import ru.eng.ai.data.storage.provider.getRoomDatabase
import ru.eng.ai.model.Message

val DataModule = DI.Module(name = "data") {
    bindSingleton<KtorClient> { KtorClient() }
    bindSingleton<WebSocketSession<Message>> { ChatWebsocketSession(client = instance()) }

    bindSingleton<EngAppDatabase> { getRoomDatabase() }
    bindSingleton<TokenDao> { instance<EngAppDatabase>().getTokenDao() }
    bindSingleton<MessageDao> { instance<EngAppDatabase>().getMessagesDao() }

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
            webSocketSession = instance()
        )
    }
}