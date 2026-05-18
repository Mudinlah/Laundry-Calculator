package com.lahmudin3031.assesment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.lahmudin3031.assesment2.database.LaundryDb
import com.lahmudin3031.assesment2.util.SettingsDataStore
import com.lahmudin3031.assesment2.navigation.AppNavigation
import com.lahmudin3031.assesment2.ui.theme.Assesment2Theme
import com.lahmudin3031.assesment2.viewmodel.LaundryViewModel
import com.lahmudin3031.assesment2.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = LaundryDb.getInstance(this)
        val dataStore = SettingsDataStore(this)
        val factory = ViewModelFactory(database.dao, dataStore)

        val viewModel: LaundryViewModel by viewModels { factory }

        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            Assesment2Theme(darkTheme = isDarkTheme) {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}