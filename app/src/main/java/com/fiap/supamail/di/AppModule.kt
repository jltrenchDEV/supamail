package com.fiap.supamail.di

import android.app.Application
import android.content.ContentValues
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

                    val contentValues1 = ContentValues().apply {
                        put("sender", "email@outlook.com")
                        put("receiver", "Você")
                        put("subject", "Título Qualquer")
                        put(
                            "body",
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Volutpat blandit aliquam etiam erat velit scelerisque in dictum non. Lorem mollis aliquam ut porttitor leo a."
                        )
                        put("alreadyOpened", false)
                        put("favorite", false)
                        put("sentDate", "2024-06-10")
                    }
                    val contentValues2 = ContentValues().apply {
                        put("sender", "email2@gmail.com")
                        put("receiver", "Você")
                        put("subject", "Aprenda Kotlin!")
                        put(
                            "body",
                            "Quam quisque id diam vel quam elementum pulvinar etiam. Ultrices dui sapien eget mi proin sed. Nec sagittis aliquam malesuada bibendum arcu vitae. Integer quis auctor elit sed vulputate mi sit amet mauris. Fermentum leo vel orci porta non pulvinar neque laoreet. Ornare arcu dui vivamus arcu felis bibendum ut tristique et."
                        )
                        put("alreadyOpened", false)
                        put("favorite", false)
                        put("sentDate", "2024-06-11")
                    }
                    db.insert("emails", OnConflictStrategy.REPLACE, contentValues1)
                    db.insert("emails", OnConflictStrategy.REPLACE, contentValues2)
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