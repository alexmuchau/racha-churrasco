package com.example.racha_churrasco.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.racha_churrasco.models.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM items WHERE sessionId = :sessionId")
    suspend fun getItemsBySession(sessionId: Int): List<Item>

    @Insert
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM items")
    fun getAllItems(): LiveData<List<Item>>
}
