package com.example.recetas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import com.example.recetas.ui.theme.BackgroundLight
import com.example.recetas.ui.theme.ChileanRed
import com.example.recetas.ui.theme.TextDark
import com.example.recetas.ui.theme.RecetasTheme
import androidx.compose.animation.core.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi

/**
 * Pantalla de bienvenida (Splash Screen)
 * Muestra el logo y botón para iniciar la aplicación
 * 
 * @param onNavigateToLogin Función que se ejecuta al hacer clic en "Iniciar"
 * @author Cristobal Camps
 * @date Enero 2026
 */
@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animación de carga simple
            Box(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .size(280.dp),
                contentAlignment = Alignment.Center
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "cooking")
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "rotation"
                )
                Text(
                    text = "🍲",
                    fontSize = 120.sp,
                    modifier = Modifier.graphicsLayer { rotationZ = rotation }
                )
            }
            
            // Logo/Título de la app
            Text(
                text = "👨‍🍳",
                fontSize = 60.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Recetas",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ChileanRed,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Sabores de nuestra tierra",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                color = TextDark,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "🇨🇱",
                fontSize = 64.sp
            )
        }
        
        // Botón al final de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ChileanRed,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = "Iniciar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Vista previa del Splash Screen
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    RecetasTheme {
        SplashScreen()
    }
}
