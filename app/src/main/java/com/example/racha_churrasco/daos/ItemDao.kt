package com.example.racha_churrasco.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.racha_churrasco.models.Item

data class ItemWithResponsible(
    val id_item: Int,
    val name: String,
    val price: Double,
    val id_user: Int,
    val userName: String
)

@Dao
interface ItemDao {
    @Query("SELECT i.id_item, i.name, i.price, u.id_user AS id_user, u.name AS userName FROM items i JOIN users u ON i.responsible = u.id_user WHERE i.sessionId = :sessionId")
    suspend fun getItemsBySession(sessionId: Int): List<ItemWithResponsible>

    @Query("SELECT i.id_item, i.name, i.price, u.id_user AS id_user, u.name AS userName FROM items i JOIN users u ON i.responsible = u.id_user WHERE responsible = :responsible")
    suspend fun getItemsByResponsible(responsible: Int): List<ItemWithResponsible>

    @Insert
    suspend fun insert(item: Item)

    @Query("UPDATE items SET name = :newName AND price = :newPrice WHERE id_item = :id_item")
    suspend fun update(id_item: Int, newName: String, newPrice: Double)

    @Query("DELETE FROM items WHERE id_item = :id_item")
    suspend fun delete(id_item: Int)

    @Query("SELECT * FROM items")
    fun getAllItems(): LiveData<List<Item>>
}
