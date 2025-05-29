package com.example.gotit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gotit.data.database.AppDatabase
import com.example.gotit.ui.screens.*
import com.example.gotit.viewmodel.ColeccionViewModel
import com.example.gotit.viewmodel.ColeccionViewModelFactory

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val viewModelFactory = ColeccionViewModelFactory(database.objetoDao())
    val viewModel: ColeccionViewModel = viewModel(factory = viewModelFactory)

    NavHost(navController = navController, startDestination = "inicio") {

        composable("inicio") {
            PantallaInicio(navController)
        }

        composable("coleccion") {
            PantallaColeccion(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable("agregar") {
            PantallaAgregar(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("editar/{objetoId}") { backStackEntry ->
            val objetoId = backStackEntry.arguments?.getString("objetoId")?.toIntOrNull()
            if (objetoId != null) {
                PantallaEditar(
                    objetoId = objetoId,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
