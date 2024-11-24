package com.example.racha_churrasco.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.racha_churrasco.models.Session

@Dao
interface SessionDao {

    @Query("SELECT * FROM tb_session WHERE name = :sessionName LIMIT 1")
    suspend fun getSessionByName(sessionName: String): Session?

    @Query("SELECT * FROM tb_session WHERE id_session = :sessionId")
    suspend fun getSessionById(sessionId: Int): Session?

    @Query("SELECT * FROM tb_session")
    suspend fun getAllSessions(): List<Session>

    @Insert
    suspend fun insertSession(session: Session): Long
}
