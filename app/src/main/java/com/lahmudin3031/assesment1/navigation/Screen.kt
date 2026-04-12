package com.lahmudin3031.assesment1.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutScreen")
    data object Result: Screen("result/{weight}/{service}/{total}") {
        fun createRoute(weight: Double, service: String, total: Int) = "result/$weight/$service/$total"
    }
}