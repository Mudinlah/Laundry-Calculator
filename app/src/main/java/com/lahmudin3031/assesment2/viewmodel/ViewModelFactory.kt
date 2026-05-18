package com.lahmudin3031.assesment2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lahmudin3031.assesment2.database.LaundryDao
import com.lahmudin3031.assesment2.util.SettingsDataStore

class ViewModelFactory(
    private val dao: LaundryDao,
    private val dataStore: SettingsDataStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LaundryViewModel::class.java)) {
            return LaundryViewModel(dao, dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}