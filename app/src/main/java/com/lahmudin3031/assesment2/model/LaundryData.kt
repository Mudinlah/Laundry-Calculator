package com.lahmudin3031.assesment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laundry_table")
data class LaundryData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val berat: Double,
    val layanan: String,
    val isSetrikaTambah: Boolean,
    val hargaTotal: Int,
    val tanggal: Long = System.currentTimeMillis()
)
