package com.example.racha_churrasco.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.racha_churrasco.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Insert
    suspend fun insertUser(user: User)
}
