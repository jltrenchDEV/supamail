package com.fiap.supamail.di

import android.app.Application
import android.content.ContentValues
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fiap.supamail.data.datasource.Database
import com.fiap.supamail.data.repository.EmailRepository
import com.fiap.supamail.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): Database {
        return Room.databaseBuilder(
            app,
            Database::class.java,
            "Database"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Valores iniciais
                    val contentValues1 = ContentValues().apply {
                        put("sender", "example1@example.com")
                        put("subject", "Assunto Exemplo 1")
                        put("body", "Corpo do email exemplo 1")
                        put("alreadyOpened", true)
                        put("favorite", false)
                        put("sentDate", "2024-06-10")
                    }
                    val contentValues2 = ContentValues().apply {
                        put("sender", "example2@example.com")
                        put("subject", "Assunto Exemplo 2")
                        put("body", "Corpo do email exemplo 2")
                        put("alreadyOpened", false)
                        put("favorite", true)
                        put("sentDate", "2024-06-11")
                    }
                    db.insert("emails", OnConflictStrategy.IGNORE, contentValues1)
                    db.insert("emails", OnConflictStrategy.IGNORE, contentValues2)
                }
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRepository(db: Database): EmailRepository {
        return RepositoryImpl(db.dao)
    }
}