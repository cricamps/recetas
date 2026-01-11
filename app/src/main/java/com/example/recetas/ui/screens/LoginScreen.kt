package com.example.recetas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetas.ui.theme.*

/**
 * Pantalla de Login
 * Permite al usuario iniciar sesión con email y contraseña
 * o mediante redes sociales
 * 
 * @param onLoginSuccess Función que se ejecuta cuando el login es exitoso
 * @author Cristobal Camps
 * @date Enero 2026
 */
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    
    // Función para validar email
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundLight
    ) {
        // AGREGADO: verticalScroll para permitir scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Logo circular
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Emoji de comida como logo
                    Text(
                        text = "🍲",
                        fontSize = 120.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(14.dp))
            
            Text(
                text = "Bienvenido/a",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = "Inicia sesión para ver tus recetas favoritas",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
            
            Spacer(modifier = Modifier.height(18.dp))
            
            // Card principal con formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Inicia con Email",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Campo de Email con validación al perder foco
                    OutlinedTextField(
                        value = email,
                        onValueChange = { 
                            email = it
                            emailError = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .onFocusChanged { focusState ->
                                // Validar cuando pierde el foco y hay texto
                                if (!focusState.isFocused && email.isNotEmpty()) {
                                    emailError = !isValidEmail(email)
                                }
                            },
                        label = { Text("Correo Electrónico") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = if (emailError) Color.Red else ChileanRed
                            )
                        },
                        isError = emailError,
                        supportingText = {
                            if (emailError) {
                                Text(
                                    text = "Correo electrónico inválido",
                                    color = Color.Red,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ChileanRed,
                            unfocusedContainerColor = BorderSoft,
                            focusedLabelColor = ChileanRed,
                            cursorColor = ChileanRed,
                            errorBorderColor = Color.Red,
                            errorLabelColor = Color.Red
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Campo de Contraseña con ojito
                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            passwordError = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (passwordError) Color.Red else ChileanRed
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Text(
                                    text = if (passwordVisible) "👁️" else "👁️‍🗨️",
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        },
                        isError = passwordError,
                        supportingText = {
                            if (passwordError) {
                                Text(
                                    text = "La contraseña debe tener al menos 6 caracteres",
                                    color = Color.Red,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) 
                            VisualTransformation.None 
                        else 
                            PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ChileanRed,
                            unfocusedContainerColor = BorderSoft,
                            focusedLabelColor = ChileanRed,
                            cursorColor = ChileanRed,
                            errorBorderColor = Color.Red,
                            errorLabelColor = Color.Red
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Botón de Iniciar Sesión con validación
                    Button(
                        onClick = {
                            // Validar antes de continuar
                            emailError = !isValidEmail(email) || email.isEmpty()
                            passwordError = password.length < 6
                            
                            if (!emailError && !passwordError) {
                                onLoginSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
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
                            text = "Iniciar Sesión",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BorderSoft, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Redes sociales abajo
                    Text(
                        text = "o entra vía Redes Sociales",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Botones de redes sociales con iconos correctos
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SocialCircle(
                            icon = "f",
                            label = "Facebook",
                            backgroundColor = Color(0xFF1877F2),
                            contentColor = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        SocialCircle(
                            icon = "𝕏",
                            label = "X",
                            backgroundColor = Color(0xFF000000),
                            contentColor = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        SocialCircle(
                            icon = "◉",
                            label = "Instagram",
                            backgroundColor = Color(0xFFE4405F),
                            contentColor = Color.White
                        )
                    }
                }
            }
            
            // Espaciado al final para que el scroll funcione bien
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Componente de círculo social
 * Representa un botón de red social con colores de marca
 * 
 * @param icon Icono o letra de la red social
 * @param label Nombre de la red social
 * @param backgroundColor Color de fondo del botón
 * @param contentColor Color del contenido (texto/icono)
 */
@Composable
private fun SocialCircle(
    icon: String,
    label: String,
    backgroundColor: Color,
    contentColor: Color
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(1.dp, backgroundColor.copy(alpha = 0.3f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            color = contentColor,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp
        )
    }
}

/**
 * Vista previa del Login Screen
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    RecetasTheme {
        LoginScreen()
    }
}
