package com.lahmudin3031.assesment2.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutScreen")
    data object Form: Screen("formScreen/{id}") {
        fun createRoute(id: Int) = "formScreen/$id"
    }
}