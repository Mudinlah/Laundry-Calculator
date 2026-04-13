package com.lahmudin3031.assesment1.ui.screen

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lahmudin3031.assesment1.R
import com.lahmudin3031.assesment1.error.BeratError
import com.lahmudin3031.assesment1.navigation.Screen
import com.lahmudin3031.assesment1.ui.theme.Assesment1Theme
import com.lahmudin3031.assesment1.viewmodel.LaundryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = Color.Unspecified
                ),
                actions = {
                    IconButton(
                        onClick = { expanded = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.tombol_info)) },
                            onClick = {
                                expanded = false
                                navController.navigate(Screen.About.route)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        InputScreen(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun InputScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel: LaundryViewModel = viewModel()

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = painterResource(R.drawable.laundryimage),
            contentDescription = stringResource(R.string.logo_laundry),
            modifier = Modifier.fillMaxWidth().height(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.berat,
            onValueChange = { viewModel.berat = it },
            label = { Text(stringResource(R.string.berat_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            isError = viewModel.errorState != BeratError.NONE,
            modifier = Modifier.fillMaxWidth()
        )
        if (viewModel.errorState != BeratError.NONE) {
            val errorMessage = when (viewModel.errorState) {
                BeratError.EMPTY -> stringResource(R.string.error_empty)
                BeratError.INVALID -> stringResource(R.string.error_invalid)
                BeratError.NEGATIVE -> stringResource(R.string.error_negative)
                else -> ""
            }
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.tipe_layanan))
        Row {
            RadioButton(
                selected = viewModel.layanan == "Reguler",
                onClick = { viewModel.layanan = "Reguler" }
            )
            Text(stringResource(
                R.string.reguler),
                modifier = Modifier.padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = viewModel.layanan == "Ekspress",
                onClick = { viewModel.layanan = "Ekspress" }
            )
            Text(stringResource(
                R.string.ekspress),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = viewModel.isSetrikaTambah,
                onCheckedChange = { viewModel.isSetrikaTambah = it }
            )
            Text(stringResource(R.string.tambah_setrika))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                val isValid = viewModel.calculate()
                if (isValid) {
                    navController.navigate(
                        Screen.Result.createRoute(
                            berat = viewModel.hasil.berat,
                            layanan = viewModel.hasil.layanan,
                            total = viewModel.hasil.hargaTotal
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.calculate))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    navController: NavController,
    berat: String,
    layanan: String,
    total: Int
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.detail_transaksi)) },
                navigationIcon = {
                    IconButton(onClick = {
                            if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(stringResource(
                R.string.rincian_harga),
                style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp)
            )

            Text(stringResource(R.string.berat_hasil, berat))
            Text(stringResource(R.string.layanan_hasil, layanan))
            Text(stringResource(
                R.string.total_hasil, total.toString()),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = "https://wa.me/628123456789".toUri()
                    }
                    navController.context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.hubungi_kurir))
            }
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    Assesment1Theme {
        MainScreen(rememberNavController())
    }
}
