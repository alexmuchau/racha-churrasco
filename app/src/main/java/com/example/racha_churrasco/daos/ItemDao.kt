package com.example.racha_churrasco.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.racha_churrasco.models.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM items WHERE sessionId = :sessionId")
    suspend fun getItemsBySession(sessionId: Int): List<Item>

    @Insert
    suspend fun insertItem(item: Item)
}
