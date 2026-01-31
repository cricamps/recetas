package com.example.recetas.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.recetas.accessibility.FontScale
import com.example.recetas.accessibility.ContrastMode
import com.example.recetas.ui.screens.*

/**
 * Configuración del grafo de navegación de la aplicación.
 * Define todas las pantallas y las transiciones animadas entre ellas.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    isDarkTheme: MutableState<Boolean>,
    onThemeChange: () -> Unit,
    fontScale: MutableState<FontScale>,
    onFontScaleChange: (FontScale) -> Unit,
    contrastMode: MutableState<ContrastMode>,
    onContrastModeChange: (ContrastMode) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Permisos.route
    ) {
        // Pantalla de Permisos - Fade suave
        composable(
            route = Screen.Permisos.route,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) }
        ) {
            PermisosScreen(
                isDarkTheme = isDarkTheme.value,
                onThemeChange = onThemeChange,
                fontScale = fontScale,
                onFontScaleChange = onFontScaleChange,
                contrastMode = contrastMode,
                onContrastModeChange = onContrastModeChange,
                onPermissionsGranted = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Permisos.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Pantalla de Login - Slide desde la derecha
        composable(
            route = Screen.Login.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)  // 1 SEGUNDO COMPLETO
                ) + fadeIn(tween(1000))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it / 3 },
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)
                ) + fadeOut(tween(1000))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it / 3 },
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(1000))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)
                ) + fadeOut(tween(1000))
            }
        ) {
            LoginScreenMejorada(
                isDarkTheme = isDarkTheme.value,
                onThemeChange = onThemeChange,
                fontScale = fontScale,
                onFontScaleChange = onFontScaleChange,
                contrastMode = contrastMode,
                onContrastModeChange = onContrastModeChange,
                onLoginSuccess = {
                    navController.navigate(Screen.Recetas.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Pantalla principal de Recetas - Slide desde la derecha
        composable(
            route = Screen.Recetas.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it / 3 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeOut(tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it / 3 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeOut(tween(300))
            }
        ) {
            RecetasScreen(
                navController = navController,
                isDarkTheme = isDarkTheme.value,
                onThemeChange = onThemeChange,
                fontScale = fontScale,
                onFontScaleChange = onFontScaleChange,
                contrastMode = contrastMode,
                onContrastModeChange = onContrastModeChange,
                onRecetaClick = { recetaId ->
                    navController.navigate(Screen.DetalleReceta.createRoute(recetaId))
                },
                onAgregarReceta = {
                    navController.navigate(Screen.AgregarReceta.route)
                }
            )
        }
        
        // Pantalla de Detalle - Slide desde abajo (modal)
        composable(
            route = Screen.DetalleReceta.route,
            arguments = listOf(
                navArgument("recetaId") { type = NavType.StringType }
            ),
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(400))
            },
            exitTransition = {
                fadeOut(tween(200))
            },
            popEnterTransition = {
                fadeIn(tween(200))
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + fadeOut(tween(400))
            }
        ) { backStackEntry ->
            val recetaId = backStackEntry.arguments?.getString("recetaId") ?: ""
            DetalleRecetaScreen(
                recetaId = recetaId,
                isDarkTheme = isDarkTheme.value,
                onThemeChange = onThemeChange,
                fontScale = fontScale,
                onFontScaleChange = onFontScaleChange,
                contrastMode = contrastMode,
                onContrastModeChange = onContrastModeChange,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Pantalla Agregar Receta - Slide desde abajo (modal)
        composable(
            route = Screen.AgregarReceta.route,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(400))
            },
            exitTransition = {
                fadeOut(tween(200))
            },
            popEnterTransition = {
                fadeIn(tween(200))
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + fadeOut(tween(400))
            }
        ) {
            AgregarRecetaScreen(
                isDarkTheme = isDarkTheme.value,
                onThemeChange = onThemeChange,
                fontScale = fontScale,
                onFontScaleChange = onFontScaleChange,
                contrastMode = contrastMode,
                onContrastModeChange = onContrastModeChange,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Pantalla de Minuta Semanal - Slide desde la derecha
        // Implementa el requerimiento de Semana 4: Array de 5 recetas con recomendaciones nutricionales
        composable(
            route = Screen.Minuta.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(400))
            },
            exitTransition = {
                fadeOut(tween(200))
            },
            popEnterTransition = {
                fadeIn(tween(200))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + fadeOut(tween(400))
            }
        ) {
            MinutaScreen(
                onNavigateToReceta = { recetaId ->
                    navController.navigate(Screen.DetalleReceta.createRoute(recetaId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
