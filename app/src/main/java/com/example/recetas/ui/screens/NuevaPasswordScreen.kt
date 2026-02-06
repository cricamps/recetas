package com.example.recetas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetas.accessibility.*
import com.example.recetas.data.UsuariosRepository

/**
 * Pantalla de Nueva Contraseña - Paso 3 (Final)
 * 
 * FLUJO:
 * 1. Usuario ingresa nueva contraseña
 * 2. Usuario confirma la contraseña
 * 3. Sistema actualiza la contraseña
 * 4. Redirige al login
 * 
 * @param email Email del usuario
 * @param isDarkTheme Estado del tema oscuro/claro
 * @param fontScale Escala de fuente para accesibilidad
 * @param onPasswordChanged Callback cuando la contraseña se cambió exitosamente
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaPasswordScreen(
    email: String,
    isDarkTheme: Boolean,
    fontScale: MutableState<FontScale>,
    onPasswordChanged: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    
    // Estados
    var nuevaPassword by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    // Validaciones en tiempo real
    val passwordLength = nuevaPassword.length >= 6
    val passwordsMatch = nuevaPassword == confirmarPassword && confirmarPassword.isNotEmpty()
    val hasUpperCase = nuevaPassword.any { it.isUpperCase() }
    val hasNumber = nuevaPassword.any { it.isDigit() }
    
    // Colores
    val backgroundColor = if (isDarkTheme) Color(0xFF1A1A1A) else Color(0xFFFFF8E1)
    val cardColor = if (isDarkTheme) Color(0xFF2A2A2A) else Color(0xFFFFFBF0)
    val primaryRed = Color(0xFFD32F2F)
    val primaryGreen = Color(0xFF4CAF50)
    
    // Función para cambiar contraseña
    fun cambiarPassword() {
        focusManager.clearFocus()
        
        when {
            nuevaPassword.isBlank() || confirmarPassword.isBlank() -> {
                showError = true
                errorMessage = "Completa todos los campos"
                hapticError(context)
            }
            nuevaPassword.length < 6 -> {
                showError = true
                errorMessage = "La contraseña debe tener al menos 6 caracteres"
                hapticError(context)
            }
            nuevaPassword != confirmarPassword -> {
                showError = true
                errorMessage = "Las contraseñas no coinciden"
                hapticError(context)
            }
            else -> {
                isLoading = true
                
                val exitoso = UsuariosRepository.cambiarPassword(email, nuevaPassword)
                
                isLoading = false
                
                if (exitoso) {
                    hapticSuccess(context)
                    announceForAccessibility(
                        context, 
                        "Contraseña cambiada exitosamente. Inicia sesión con tu nueva contraseña"
                    )
                    onPasswordChanged()
                } else {
                    showError = true
                    errorMessage = "Error al cambiar la contraseña. Intenta nuevamente"
                    hapticError(context)
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Nueva Contraseña",
                        fontSize = 20.scaledSp()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icono de éxito
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(primaryGreen.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✅",
                        fontSize = 80.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Crear Nueva Contraseña",
                    fontSize = 28.scaledSp(),
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF4A4A4A),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Tu nueva contraseña debe ser diferente a las anteriores",
                    fontSize = 16.scaledSp(),
                    color = if (isDarkTheme) Color.LightGray else Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Card con formulario
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Campo nueva contraseña
                        OutlinedTextField(
                            value = nuevaPassword,
                            onValueChange = { 
                                nuevaPassword = it
                                showError = false
                            },
                            label = { Text("Nueva Contraseña") },
                            leadingIcon = { Text("🔒", fontSize = 20.sp) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Text(
                                        text = if (passwordVisible) "👁️" else "👁️‍🗨️",
                                        fontSize = 20.sp
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) 
                                VisualTransformation.None 
                            else 
                                PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = if (isDarkTheme) Color(0xFF3A3A3A) else Color(0xFFFFE4B5),
                                unfocusedContainerColor = if (isDarkTheme) Color(0xFF3A3A3A) else Color(0xFFFFE4B5)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Campo confirmar contraseña
                        OutlinedTextField(
                            value = confirmarPassword,
                            onValueChange = { 
                                confirmarPassword = it
                                showError = false
                            },
                            label = { Text("Confirmar Contraseña") },
                            leadingIcon = { Text("🔒", fontSize = 20.sp) },
                            visualTransformation = if (passwordVisible) 
                                VisualTransformation.None 
                            else 
                                PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = if (isDarkTheme) Color(0xFF3A3A3A) else Color(0xFFFFE4B5),
                                unfocusedContainerColor = if (isDarkTheme) Color(0xFF3A3A3A) else Color(0xFFFFE4B5)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { cambiarPassword() }
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        // Requisitos de contraseña
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Requisitos de la contraseña:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color(0xFF4A4A4A)
                            )
                            
                            RequisitoPassword(
                                texto = "Mínimo 6 caracteres",
                                cumplido = passwordLength
                            )
                            
                            RequisitoPassword(
                                texto = "Al menos una mayúscula",
                                cumplido = hasUpperCase
                            )
                            
                            RequisitoPassword(
                                texto = "Al menos un número",
                                cumplido = hasNumber
                            )
                            
                            RequisitoPassword(
                                texto = "Las contraseñas coinciden",
                                cumplido = passwordsMatch
                            )
                        }
                        
                        // Mensaje de error
                        if (showError) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "❌ $errorMessage",
                                color = primaryRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Botón cambiar contraseña
                        Button(
                            onClick = { cambiarPassword() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryGreen
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading && passwordLength && passwordsMatch
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "Cambiar Contraseña",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Información adicional
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔐",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column {
                            Text(
                                text = "Seguridad",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Tu contraseña se guardará de forma segura",
                                color = Color(0xFF2E7D32),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Componente para mostrar un requisito de contraseña
 */
@Composable
fun RequisitoPassword(
    texto: String,
    cumplido: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = if (cumplido) "✅" else "⭕",
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = texto,
            fontSize = 14.sp,
            color = if (cumplido) Color(0xFF4CAF50) else Color.Gray
        )
    }
}
