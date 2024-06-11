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

    @Query("SELECT * FROM emails")
    fun getAllEmails(): Flow<List<EmailEntity>>
}