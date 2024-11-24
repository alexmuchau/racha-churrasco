package com.example.racha_churrasco.daos

import androidx.lifecycle.LiveData
import androidx.room.*

import com.example.racha_churrasco.models.Item

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM items")
    fun getAllItems(): LiveData<List<Item>>
}
