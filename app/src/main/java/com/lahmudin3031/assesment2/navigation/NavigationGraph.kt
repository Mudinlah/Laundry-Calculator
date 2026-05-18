package com.lahmudin3031.assesment2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lahmudin3031.assesment2.ui.screen.AboutScreen
import com.lahmudin3031.assesment2.ui.screen.FormScreen
import com.lahmudin3031.assesment2.ui.screen.MainScreen
import com.lahmudin3031.assesment2.viewmodel.LaundryViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: LaundryViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            MainScreen(navController, viewModel)
        }
        composable(route = Screen.About.route) {
            AboutScreen(navController)
        }
        composable(
            route = Screen.Form.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            FormScreen(navController, viewModel, id)
        }
    }
}