package com.example.racha_churrasco.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var nomeItem: String,
    var valorItem: Int,
)
