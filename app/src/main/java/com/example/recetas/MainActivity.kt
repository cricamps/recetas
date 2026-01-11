package com.example.recetas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.recetas.ui.screens.LoginScreen
import com.example.recetas.ui.screens.RecetasScreen
import com.example.recetas.ui.theme.RecetasTheme

/**
 * Actividad principal de la aplicación de Recetas
 * 
 * Esta actividad maneja la navegación entre las pantallas:
 * - Login Screen (Inicio de sesión) - PANTALLA INICIAL
 * - Recetas Screen (Lista de recetas)
 * 
 * @author Cristobal Camps
 * @date Enero 2026
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecetasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecetasApp()
                }
            }
        }
    }
}

/**
 * Composable principal que maneja la navegación de la app
 * Controla qué pantalla se muestra en cada momento
 * INICIA DIRECTAMENTE EN LOGIN (sin splash screen)
 */
@Composable
fun RecetasApp() {
    // Estado que controla qué pantalla mostrar - INICIA EN "login"
    var currentScreen by remember { mutableStateOf("login") }
    
    when (currentScreen) {
        "login" -> {
            LoginScreen(
                onLoginSuccess = { currentScreen = "recetas" }
            )
        }
        "recetas" -> {
            RecetasScreen()
        }
    }
}
