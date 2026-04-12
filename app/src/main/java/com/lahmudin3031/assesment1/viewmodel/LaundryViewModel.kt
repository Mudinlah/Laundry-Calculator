package com.lahmudin3031.assesment1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lahmudin3031.assesment1.model.LaundryData

class LaundryViewModel : ViewModel() {

    var weight by mutableStateOf("")
    var service by mutableStateOf("Reguler")
    var error by mutableStateOf("")
    var result by mutableStateOf(LaundryData())

    private val hargaReguler = 5000
    private val hargaEkspres = 8000

    fun calculate() : Boolean {
        val w = weight.toDoubleOrNull()

        return if (w == null || w <= 0) {
            error = "Weight must be > 0"
            false
        } else {
            val price = if (service == "Reguler") hargaReguler else hargaEkspres
            val total = (w * price).toInt()

            result = LaundryData(w, service, total)
            error = ""
            true
        }
    }
}