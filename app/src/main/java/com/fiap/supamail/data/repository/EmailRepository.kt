package com.fiap.supamail.data.repository

import com.fiap.supamail.data.datasource.EmailDao
import com.fiap.supamail.data.entity.EmailEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface EmailRepository {

    suspend fun insert(emailEntity: EmailEntity)

    suspend fun delete(emailEntity: EmailEntity)

    suspend fun update(emailEntity: EmailEntity)

    suspend fun getAllEmails(): Flow<List<EmailEntity>>
}

class RepositoryImpl @Inject constructor(
    private val dao: EmailDao
) : EmailRepository {
    override suspend fun insert(emailEntity: EmailEntity) {
        withContext(IO) {
            dao.insert(emailEntity)
        }
    }

    override suspend fun delete(emailEntity: EmailEntity) {
        withContext(IO) {
            dao.delete(emailEntity)
        }
    }

    override suspend fun update(emailEntity: EmailEntity) {
        withContext(IO) {
            dao.update(emailEntity)
        }
    }

    override suspend fun getAllEmails(): Flow<List<EmailEntity>> {
        return withContext(IO) {
            dao.getAllEmails()
        }
    }
}