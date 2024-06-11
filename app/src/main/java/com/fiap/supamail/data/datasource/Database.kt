package com.fiap.supamail.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fiap.supamail.data.entity.EmailEntity

@Database(
    entities = [EmailEntity::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract val dao: EmailDao
}