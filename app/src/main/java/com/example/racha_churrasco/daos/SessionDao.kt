package com.example.racha_churrasco.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.racha_churrasco.models.Session

@Dao
interface SessionDao {

    @Query("SELECT * FROM tb_session WHERE id_session = :id LIMIT 1")
    suspend fun getSessionById(id: Int): Session?

    @Query("SELECT * FROM tb_session WHERE name = :name LIMIT 1")
    suspend fun getSessionByName(name: String): Session?

    @Insert
    suspend fun insertSession(session: Session): Long

    @Query("SELECT * FROM tb_session")
    suspend fun getAllSessions(): List<Session>
}
