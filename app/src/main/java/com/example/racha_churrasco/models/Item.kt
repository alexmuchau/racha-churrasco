package com.example.racha_churrasco.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id_item: Int = 0,
    val name: String,
    val price: Double,
    val responsible: String,
    val sessionId: Int // Relacionamento com a sessão
)
