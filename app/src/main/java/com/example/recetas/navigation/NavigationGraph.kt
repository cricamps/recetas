package com.example.recetas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.recetas.ui.screens.PermisosScreen
import com.example.recetas.ui.screens.LoginScreen
import com.example.recetas.ui.screens.RecetasScreen
import com.example.recetas.ui.screens.DetalleRecetaScreen
import com.example.recetas.ui.screens.AgregarRecetaScreen

/**
 * Configuración del grafo de navegación de la aplicación.
 * Define todas las pantallas y las transiciones entre ellas.
 * 
 * @param navController Controlador de navegación
 * @param isDarkTheme Estado del tema oscuro/claro
 * @param onThemeChange Callback para cambiar el tema
 */
@Composable
fun NavigationGraph(
    navController: NavHostController,
    isDarkTheme: MutableState<Boolean>,
    onThemeChange: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Permisos.route
    ) {
        // Pantalla de Permisos
        composable(route = Screen.Permisos.route) {
            PermisosScreen(
                isDarkTheme = isDarkTheme.value,
                onThemeChange = onThemeChange,
                onPermissionsGranted = {
                    // Navegar a Login después de conceder permisos
                    navController.navigate(Screen.Login.route) {
                        // Limpiar el back stack para que no se pueda volver a permisos
                        popUpTo(Screen.Permisos.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Pantalla de Login
        composable(route = Screen.Login.route) {
            LoginScreen(
                isDarkTheme = isDarkTheme.value,
                onThemeChange = onThemeChange,
                onLoginSuccess = {
                    // Navegar a la pantalla de recetas al hacer login
                    navController.navigate(Screen.Recetas.route) {
                        // Limpiar el back stack para que no se pueda volver al login
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Pantalla principal de Recetas
        composable(route = Screen.Recetas.route) {
            RecetasScreen(
                isDarkTheme = isDarkTheme.value,
                onThemeChange = onThemeChange,
                onRecetaClick = { recetaId ->
                    // Navegar al detalle de la receta seleccionada
                    navController.navigate(Screen.DetalleReceta.createRoute(recetaId))
                },
                onAgregarReceta = {
                    // Navegar a la pantalla de agregar receta
                    navController.navigate(Screen.AgregarReceta.route)
                }
            )
        }
        
        // Pantalla de Detalle de Receta
        composable(
            route = Screen.DetalleReceta.route,
            arguments = listOf(
                navArgument("recetaId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val recetaId = backStackEntry.arguments?.getString("recetaId") ?: ""
            DetalleRecetaScreen(
                recetaId = recetaId,
                isDarkTheme = isDarkTheme.value,
                onThemeChange = onThemeChange,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Pantalla de Agregar Receta
        composable(route = Screen.AgregarReceta.route) {
            AgregarRecetaScreen(
                isDarkTheme = isDarkTheme.value,
                onThemeChange = onThemeChange,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
