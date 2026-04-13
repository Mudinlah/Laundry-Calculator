package com.lahmudin3031.assesment1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lahmudin3031.assesment1.error.BeratError
import com.lahmudin3031.assesment1.model.LaundryData

class LaundryViewModel : ViewModel() {

    var berat by mutableStateOf("")
    var layanan by mutableStateOf("Reguler")
    var isSetrikaTambah by mutableStateOf(false)
    var errorState by mutableStateOf(BeratError.NONE)
    var hasil by mutableStateOf(LaundryData())

    private val hargaReguler = 5000
    private val hargaEkspres = 8000
    private val hargaSetrika = 2000

    fun calculate() : Boolean {
        if (berat.isBlank()) {
            errorState = BeratError.EMPTY
            return false
        }

        val b = berat.toDoubleOrNull()
        if (b == null) {
            errorState = BeratError.INVALID
            return false
        }

        if (b <= 0) {
            errorState = BeratError.NEGATIVE
            return false
        }

        val hargaAwal = if (layanan == "Reguler") hargaReguler else hargaEkspres
        val hargaAkhirPerKg = if (isSetrikaTambah) hargaAwal + hargaSetrika else hargaAwal

        val total = (b * hargaAkhirPerKg).toInt()

        hasil = LaundryData(b, layanan, total)
        errorState = BeratError.NONE
        return true
    }
}