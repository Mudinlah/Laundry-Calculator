package com.lahmudin3031.assesment1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lahmudin3031.assesment1.ui.screen.AboutScreen
import com.lahmudin3031.assesment1.ui.screen.MainScreen
import com.lahmudin3031.assesment1.ui.screen.ResultScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }

        composable(route = Screen.About.route) {
            AboutScreen(navController)
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("berat") { type = NavType.StringType },
                navArgument("layanan") { type = NavType.StringType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val berat = backStackEntry.arguments?.getString("berat") ?: ""
            val layanan = backStackEntry.arguments?.getString("layanan") ?: ""
            val total = backStackEntry.arguments?.getInt("total") ?: 0

            ResultScreen(navController, berat, layanan, total)
        }
    }
}