package com.lahmudin3031.assesment1.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutScreen")
    data object Result: Screen("hasil/{berat}/{layanan}/{total}") {
        fun createRoute(berat: Double, layanan: String, total: Int) = "hasil/$berat/$layanan/$total"
    }
}