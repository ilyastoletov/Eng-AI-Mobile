package ru.eng.ai.data.storage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.eng.ai.data.storage.room.entity.TokenEntity

@Dao
interface TokenDao {

    @Insert(entity = TokenEntity::class)
    suspend fun insertToken(entity: TokenEntity)

    @Query("SELECT * FROM token_store LIMIT 1")
    suspend fun getToken(): TokenEntity?

    @Query("DELETE FROM token_store")
    suspend fun clear()

}