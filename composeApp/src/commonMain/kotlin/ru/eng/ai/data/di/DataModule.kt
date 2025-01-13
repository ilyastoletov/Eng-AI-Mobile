package ru.eng.ai.data.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.eng.ai.data.network.KtorClient
import ru.eng.ai.data.repository.user.UserRepository
import ru.eng.ai.data.repository.user.UserRepositoryImpl
import ru.eng.ai.data.storage.EngAppDatabase
import ru.eng.ai.data.storage.dao.TokenDao
import ru.eng.ai.data.storage.provider.getRoomDatabase

val DataModule = DI.Module(name = "data") {
    bindSingleton<KtorClient> { KtorClient() }
    bindSingleton<EngAppDatabase> { getRoomDatabase() }
    bindSingleton<TokenDao> {
        val database: EngAppDatabase = instance()
        database.getTokenDao()
    }
    bindSingleton<UserRepository> {
        UserRepositoryImpl(
            httpClient = instance(),
            tokenDao = instance()
        )
    }
}