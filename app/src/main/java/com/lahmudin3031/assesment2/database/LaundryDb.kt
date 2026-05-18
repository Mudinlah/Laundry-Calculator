package com.lahmudin3031.assesment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lahmudin3031.assesment2.model.LaundryData

@Database(entities = [LaundryData::class], version = 1, exportSchema = false)
abstract class LaundryDb : RoomDatabase() {
    abstract val dao: LaundryDao
    companion object {
        @Volatile
        private var INSTANCE: LaundryDb? = null
        fun getInstance(context: Context): LaundryDb {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    LaundryDb::class.java,
                    "laundry.db"
                ).fallbackToDestructiveMigration(false).build().also { INSTANCE = it }
            }
        }
    }
}