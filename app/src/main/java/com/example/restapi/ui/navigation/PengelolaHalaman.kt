package com.example.restapi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restapi.ui.view.DestinasiDetail
import com.example.restapi.ui.view.DestinasiEntry
import com.example.restapi.ui.view.DestinasiHome
import com.example.restapi.ui.view.DestinasiUpdate
import com.example.restapi.ui.view.DetailView
import com.example.restapi.ui.view.EntryMhsScreen
import com.example.restapi.ui.view.HomeScreen
import com.example.restapi.ui.view.UpdateScreen


@Composable
fun PengelolaHalaman(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = Modifier,
    ) {
        composable(DestinasiHome.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(DestinasiEntry.route) },
                onDetailClick = { nim ->
                    navController.navigate("${DestinasiDetail.route}/$nim")
                }
            )
        }
        composable(DestinasiEntry.route) {
            EntryMhsScreen(navigateBack = {
                navController.navigate(DestinasiHome.route) {
                    popUpTo(DestinasiHome.route) {
                        inclusive = true
                    }
                }
            })
        }
        // Detail Screen
        composable(
            route = "${DestinasiDetail.route}/{nim}",
        ) { backStackEntry ->
            val nim = backStackEntry.arguments?.getString("nim") ?: ""
            DetailView(
                nim = nim,
                onNavigateBack = { navController.popBackStack() },
                onEditClick = {navController.navigate("${DestinasiUpdate.route}/$nim")}

            )
        }

        // Update Screen
        composable(
            route = "${DestinasiUpdate.route}/{nim}"
        ) { backStackEntry ->
            val nim = backStackEntry.arguments?.getString("nim") ?: ""
            UpdateScreen(
                nim = nim,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}