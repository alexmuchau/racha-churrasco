package com.example.racha_churrasco.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.racha_churrasco.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE sessionId = :sessionId")
    suspend fun getUsersBySession(sessionId: Int): List<User>

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Insert
    suspend fun insertUser(user: User)

    @Query("UPDATE users SET sessionId = :session_id WHERE id_user = :user_id")
    suspend fun updateUser(user_id: Int, session_id: Int)
}
