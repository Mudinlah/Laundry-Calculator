@file:Suppress("AssignedValueIsNeverRead")

package com.lahmudin3031.assesment2.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lahmudin3031.assesment2.R
import com.lahmudin3031.assesment2.ui.theme.Assesment2Theme
import com.lahmudin3031.assesment2.viewmodel.LaundryViewModel

@Composable
fun FormScreen(
    navController: NavHostController,
    viewModel: LaundryViewModel,
    id: Int
) {
    val context = LocalContext.current
    val dataList by viewModel.dataList.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    val isUpdateMode = id > 0

    LaunchedEffect(id, dataList) {
        if (isUpdateMode) {
            val existingItem = dataList.find { it.id == id }
            existingItem?.let { viewModel.loadExistingData(it) }
        } else {
            viewModel.clearForm()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.konfirmasi_hapus)) },
            text = { Text(stringResource(R.string.yakin_hapus)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val existingItem = dataList.find { it.id == id }
                        existingItem?.let {
                            viewModel.deleteData(it)
                            Toast.makeText(
                                context,
                                R.string.data_dihapus,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        showDeleteDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text(stringResource(
                        R.string.hapus),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.batal))
                }
            }
        )
    }

    FormScreenContent(
        isUpdateMode = isUpdateMode,
        berat = viewModel.berat,
        onBeratChange = { viewModel.berat = it },
        layanan = viewModel.layanan,
        onLayananChange = { viewModel.layanan = it },
        isSetrikaTambah = viewModel.isSetrikaTambah,
        onSetrikaChange = { viewModel.isSetrikaTambah = it },
        menuExpanded = menuExpanded,
        onMenuExpandedChange = { menuExpanded = it },
        onDeleteClick = { showDeleteDialog = true },
        onBackClick = { navController.popBackStack() },
        onSaveClick = {
            val success = viewModel.saveData(context, if (isUpdateMode) id else null)
            if (success) {
                Toast.makeText(
                    context, R.string.data_disimpan,
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreenContent(
    isUpdateMode: Boolean,
    berat: String,
    onBeratChange: (String) -> Unit,
    layanan: String,
    onLayananChange: (String) -> Unit,
    isSetrikaTambah: Boolean,
    onSetrikaChange: (Boolean) -> Unit,
    menuExpanded: Boolean,
    onMenuExpandedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isUpdateMode) stringResource(R.string.ubah_data_laundry)
                        else stringResource(R.string.tambah_data_laundry)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    if (isUpdateMode) {
                        IconButton(onClick = { onMenuExpandedChange(true) }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = stringResource(R.string.menu)
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { onMenuExpandedChange(false) }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.hapus)) },
                                onClick = {
                                    onMenuExpandedChange(false)
                                    onDeleteClick()
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = berat,
                onValueChange = onBeratChange,
                label = { Text(stringResource(R.string.berat_label)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(stringResource(R.string.tipe_layanan))
            Row {
                RadioButton(
                    selected = layanan == "Reguler",
                    onClick = { onLayananChange("Reguler") }
                )
                Text(stringResource(
                    R.string.reguler),
                    modifier = Modifier.padding(top = 12.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = layanan == "Ekspres",
                    onClick = { onLayananChange("Ekspres") }
                )
                Text(stringResource(
                    R.string.ekspress),
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isSetrikaTambah,
                    onCheckedChange = onSetrikaChange
                )
                Text(stringResource(R.string.tambah_setrika))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.simpan))
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun FormScreenPreview() {
    Assesment2Theme {
        FormScreenContent(
            isUpdateMode = false,
            berat = "5",
            onBeratChange = {},
            layanan = "Reguler",
            onLayananChange = {},
            isSetrikaTambah = false,
            onSetrikaChange = {},
            menuExpanded = false,
            onMenuExpandedChange = {},
            onDeleteClick = {},
            onBackClick = {},
            onSaveClick = {}
        )
    }
}