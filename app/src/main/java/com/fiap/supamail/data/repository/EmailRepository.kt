package com.fiap.supamail.data.repository

import com.fiap.supamail.data.datasource.EmailDao
import com.fiap.supamail.data.entity.EmailEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface EmailRepository {
    suspend fun getEmail(id: Int): EmailEntity

    suspend fun getEmailsBySubject(subject: String): Flow<List<EmailEntity>>

    suspend fun insert(emailEntity: EmailEntity)

    suspend fun delete(emailEntity: EmailEntity)

    suspend fun update(emailEntity: EmailEntity)

    suspend fun getAllEmails(): Flow<List<EmailEntity>>

    suspend fun getFavoriteEmails(): Flow<List<EmailEntity>>

    suspend fun getEmailsAscByDate(): Flow<List<EmailEntity>>

    suspend fun getEmailsDescByDate(): Flow<List<EmailEntity>>

    suspend fun updateEmailAsOpened(emailId: Int)

    suspend fun setFavorite(emailId: Int, isFavorite: Int)
}

class RepositoryImpl @Inject constructor(
    private val dao: EmailDao
) : EmailRepository {
    override suspend fun getEmail(id: Int): EmailEntity {
        return withContext(IO) {
            dao.getEmail(id)
        }
    }

    override suspend fun getEmailsBySubject(subject: String): Flow<List<EmailEntity>> {
        return dao.getEmailsBySubject("%$subject%")
    }

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

    override suspend fun getFavoriteEmails(): Flow<List<EmailEntity>> {
        return dao.getFavoriteEmails()
    }

    override suspend fun getEmailsAscByDate(): Flow<List<EmailEntity>> {
        return dao.getEmailsAscByDate()
    }

    override suspend fun getEmailsDescByDate(): Flow<List<EmailEntity>> {
        return dao.getEmailsDescByDate()
    }

    override suspend fun updateEmailAsOpened(emailId: Int) {
        return withContext(IO) {
            dao.updateEmailAsOpened(emailId)
        }
    }

    override suspend fun setFavorite(emailId: Int, isFavorite: Int) {
        return withContext(IO) {
            dao.updateEmailFavoriteStatus(emailId, isFavorite)
        }
    }
}