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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetas.accessibility.*
import com.example.recetas.data.UsuariosRepository
import com.example.recetas.services.EmailService
import kotlinx.coroutines.launch

/**
 * Pantalla de Recuperación de Contraseña - Paso 1: Solicitar Email
 * 
 * FLUJO:
 * 1. Usuario ingresa su email
 * 2. Sistema genera código de 6 dígitos
 * 3. Navega a pantalla de verificación
 * 
 * @param isDarkTheme Estado del tema oscuro/claro
 * @param fontScale Escala de fuente para accesibilidad
 * @param onBackToLogin Callback para volver al login
 * @param onCodeSent Callback cuando el código se envía (recibe email y código)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarPasswordScreen(
    isDarkTheme: Boolean,
    fontScale: MutableState<FontScale>,
    onBackToLogin: () -> Unit,
    onCodeSent: (email: String, codigo: String) -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    
    var email by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    val backgroundColor = if (isDarkTheme) Color(0xFF1A1A1A) else Color(0xFFFFF8E1)
    val cardColor = if (isDarkTheme) Color(0xFF2A2A2A) else Color(0xFFFFFBF0)
    val primaryRed = Color(0xFFD32F2F)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Recuperar Contraseña",
                        fontSize = 20.scaledSp()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        hapticFeedback(context)
                        onBackToLogin()
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver al login"
                        )
                    }
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
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(primaryRed.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔐",
                        fontSize = 80.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    fontSize = 28.scaledSp(),
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF4A4A4A),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "No te preocupes, te enviaremos un código de verificación a tu correo electrónico",
                    fontSize = 16.scaledSp(),
                    color = if (isDarkTheme) Color.LightGray else Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(40.dp))
                
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
                        OutlinedTextField(
                            value = email,
                            onValueChange = { 
                                email = it
                                showError = false
                            },
                            label = { Text("Correo Electrónico") },
                            leadingIcon = { Text("✉️", fontSize = 20.sp) },
                            placeholder = { Text("ejemplo@correo.cl") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = if (isDarkTheme) Color(0xFF3A3A3A) else Color(0xFFFFE4B5),
                                unfocusedContainerColor = if (isDarkTheme) Color(0xFF3A3A3A) else Color(0xFFFFE4B5)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            ),
                            isError = showError
                        )
                        
                        if (showError) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "❌ $errorMessage",
                                color = primaryRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                
                                when {
                                    email.isBlank() -> {
                                        showError = true
                                        errorMessage = "Ingresa tu correo electrónico"
                                        hapticError(context)
                                    }
                                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                                        showError = true
                                        errorMessage = "Correo electrónico inválido"
                                        hapticError(context)
                                    }
                                    else -> {
                                        isLoading = true
                                        
                                        val codigo = UsuariosRepository.solicitarRecuperacionPassword(email)
                                        
                                        if (codigo != null) {
                                            scope.launch {
                                                val emailEnviado = EmailService.enviarEmailRecuperacion(
                                                    email = email,
                                                    codigo = codigo
                                                )
                                                
                                                isLoading = false
                                                
                                                if (emailEnviado) {
                                                    hapticSuccess(context)
                                                    announceForAccessibility(
                                                        context, 
                                                        "Código enviado a $email. Revisa tu correo."
                                                    )
                                                    onCodeSent(email, codigo)
                                                } else {
                                                    showError = true
                                                    errorMessage = "Error al enviar el email. Intenta nuevamente."
                                                    hapticError(context)
                                                }
                                            }
                                        } else {
                                            isLoading = false
                                            showError = true
                                            errorMessage = "Este correo no está registrado"
                                            hapticError(context)
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryRed
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "Enviar Código",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                TextButton(onClick = {
                    hapticFeedback(context)
                    onBackToLogin()
                }) {
                    Text(
                        text = "← Volver al inicio de sesión",
                        fontSize = 16.sp,
                        color = primaryRed,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                // Info adicional
                Spacer(modifier = Modifier.height(24.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ℹ️",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column {
                            Text(
                                text = "Modo de desarrollo",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1976D2),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "El código se mostrará en pantalla para propósitos de prueba",
                                color = Color(0xFF1976D2),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
