package com.lahmudin3031.assesment2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lahmudin3031.assesment2.model.LaundryData
import kotlinx.coroutines.flow.Flow

@Dao
interface LaundryDao {
    @Insert
    suspend fun insert(laundry: LaundryData)
    @Update
    suspend fun update(laundry: LaundryData)
    @Delete
    suspend fun delete(laundry: LaundryData)
    @Query("SELECT * FROM laundry_table ORDER BY tanggal DESC")
    fun getAllData(): Flow<List<LaundryData>>
}