package com.example.racha_churrasco.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id_user: Int = 0,
    val name: String,
    val username: String,
    val sessionId: Int? = null // Relacionamento com a sessão
)
