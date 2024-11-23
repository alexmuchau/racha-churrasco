package com.example.racha_churrasco.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_session")
data class Session(
    @PrimaryKey(autoGenerate = true) val id_session: Int = 0,
    val name: String
)
