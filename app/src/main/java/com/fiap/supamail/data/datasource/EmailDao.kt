package com.fiap.supamail.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fiap.supamail.data.entity.EmailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(emailEntity: EmailEntity)

    @Delete
    suspend fun delete(emailEntity: EmailEntity)

    @Update
    suspend fun update(emailEntity: EmailEntity)

    @Query("SELECT * FROM emails ORDER BY id DESC")
    fun getAllEmails(): Flow<List<EmailEntity>>

    @Query("SELECT * FROM emails where id = :id")
    fun getEmail(id: Int): EmailEntity

    @Query("SELECT * FROM emails WHERE subject LIKE :subject")
    fun getEmailsBySubject(subject: String): Flow<List<EmailEntity>>

    @Query("SELECT * FROM emails WHERE favorite = 1")
    fun getFavoriteEmails(): Flow<List<EmailEntity>>

    @Query("SELECT * FROM emails ORDER BY sentDate ASC")
    fun getEmailsAscByDate(): Flow<List<EmailEntity>>

    @Query("SELECT * FROM emails ORDER BY sentDate DESC")
    fun getEmailsDescByDate(): Flow<List<EmailEntity>>

    @Query("UPDATE emails SET alreadyOpened = 1 WHERE id = :emailId")
    suspend fun updateEmailAsOpened(emailId: Int)

    @Query("UPDATE emails SET favorite = :isFavorite WHERE id = :emailId")
    suspend fun updateEmailFavoriteStatus(emailId: Int, isFavorite: Int)
}