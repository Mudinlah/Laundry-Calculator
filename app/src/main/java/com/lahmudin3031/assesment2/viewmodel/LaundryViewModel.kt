package com.lahmudin3031.assesment2.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lahmudin3031.assesment2.R
import com.lahmudin3031.assesment2.database.LaundryDao
import com.lahmudin3031.assesment2.model.LaundryData
import com.lahmudin3031.assesment2.util.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LaundryViewModel(
    private val dao: LaundryDao,
    private val dataStore: SettingsDataStore
) : ViewModel() {

    var berat by mutableStateOf("")
    var layanan by mutableStateOf("Reguler")
    var isSetrikaTambah by mutableStateOf(false)

    val dataList: StateFlow<List<LaundryData>> = dao.getAllData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val isDarkTheme: StateFlow<Boolean> = dataStore.isDarkTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val isGridLayout: StateFlow<Boolean> = dataStore.isGridLayout.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private val hargaReguler = 5000
    private val hargaEkspres = 8000
    private val hargaSetrika = 2000

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            dataStore.saveThemePreference(isDark)
        }
    }

    fun toggleLayout(isGrid: Boolean) {
        viewModelScope.launch {
            dataStore.saveLayoutPreference(isGrid)
        }
    }

    fun loadExistingData(entity: LaundryData) {
        berat = entity.berat.toString()
        layanan = entity.layanan
        isSetrikaTambah = entity.isSetrikaTambah
    }

    fun clearForm() {
        berat = ""
        layanan = "Reguler"
        isSetrikaTambah = false
    }


    fun saveData(context: Context, id: Int? = null): Boolean {
        if (berat.isBlank()) {
            Toast.makeText(
                context,
                context.getString(R.string.error_empty),
                Toast.LENGTH_SHORT).show()
            return false
        }

        val b = berat.toDoubleOrNull()
        if (b == null) {
            Toast.makeText(
                context,
                context.getString(R.string.error_invalid),
                Toast.LENGTH_SHORT).show()
            return false
        }

        if (b <= 0) {
            Toast.makeText(
                context,
                context.getString(R.string.error_negative),
                Toast.LENGTH_SHORT).show()
            return false
        }

        val hargaAwal = if (layanan == context.getString(R.string.reguler))
                            hargaReguler
                        else
                            hargaEkspres

        val hargaAkhirPerKg = if (isSetrikaTambah)
                                  hargaAwal + hargaSetrika
                              else
                                  hargaAwal

        val total = (b * hargaAkhirPerKg).toInt()

        viewModelScope.launch {
            if (id == null) {
                dao.insert(
                    LaundryData(
                        berat = b,
                        layanan = layanan,
                        isSetrikaTambah = isSetrikaTambah,
                        hargaTotal = total
                    )
                )
            } else {
                dao.update(
                    LaundryData(
                        id = id,
                        berat = b,
                        layanan = layanan,
                        isSetrikaTambah = isSetrikaTambah,
                        hargaTotal = total,
                        tanggal = System.currentTimeMillis()
                    )
                )
            }
        }
        return true
    }

    fun deleteData(entity: LaundryData) {
        viewModelScope.launch {
            dao.delete(entity)
        }
    }
}