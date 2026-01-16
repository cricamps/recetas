package com.example.recetas.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetas.accessibility.*

/**
 * Pantalla de inicio de sesión ACCESIBLE con diseño mejorado.
 * 
 * CARACTERÍSTICAS DE ACCESIBILIDAD:
 * ✅ Feedback háptico en todas las acciones
 * ✅ Descripciones semánticas para TalkBack
 * ✅ Validación en tiempo real al perder foco
 * ✅ Diseño profesional y atractivo
 * 
 * @param isDarkTheme Estado del tema oscuro/claro
 * @param onThemeChange Callback para cambiar el tema
 * @param onLoginSuccess Callback cuando el login es exitoso
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    
    // Detener TTS cuando se sale de la pantalla
    DisposableEffect(Unit) {
        onDispose {
            SpeechManager.stop()
        }
    }
    
    // Estados para los campos de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    // Estados para validación
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var showGeneralError by remember { mutableStateOf(false) }
    
    // Estados de foco
    var emailHasFocus by remember { mutableStateOf(false) }
    var passwordHasFocus by remember { mutableStateOf(false) }
    
    // Función para validar email
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }
    
    // Función para validar contraseña
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
    
    // Validar email al perder foco
    fun validateEmailOnFocusLost() {
        if (email.isNotEmpty() && !emailHasFocus) {
            if (!isValidEmail(email)) {
                emailError = "Formato de correo inválido"
                hapticError(context)
            } else {
                emailError = null
            }
        }
    }
    
    // Validar contraseña al perder foco
    fun validatePasswordOnFocusLost() {
        if (password.isNotEmpty() && !passwordHasFocus) {
            if (!isValidPassword(password)) {
                passwordError = "Mínimo 6 caracteres"
                hapticError(context)
            } else {
                passwordError = null
            }
        }
    }
    
    // Función para validar el formulario
    fun validateForm(): Boolean {
        var isValid = true
        
        // Validar email
        when {
            email.isBlank() -> {
                emailError = "El correo es obligatorio"
                isValid = false
            }
            !isValidEmail(email) -> {
                emailError = "Formato de correo inválido"
                isValid = false
            }
            else -> emailError = null
        }
        
        // Validar contraseña
        when {
            password.isBlank() -> {
                passwordError = "La contraseña es obligatoria"
                isValid = false
            }
            !isValidPassword(password) -> {
                passwordError = "Mínimo 6 caracteres"
                isValid = false
            }
            else -> passwordError = null
        }
        
        return isValid
    }
    
    // Colores personalizados
    val backgroundColor = if (isDarkTheme) Color(0xFF1A1A1A) else Color(0xFFFFF8E1)
    val cardColor = if (isDarkTheme) Color(0xFF2A2A2A) else Color(0xFFFFFBF0)
    val fieldColor = if (isDarkTheme) Color(0xFF3A3A3A) else Color(0xFFFFE4B5)
    val primaryRed = Color(0xFFD32F2F)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(
                        onClick = {
                            hapticFeedback(context)
                            onThemeChange()
                        },
                        modifier = Modifier.semantics {
                            contentDescription = buttonAccessibilityDescription(
                                label = "Cambiar tema",
                                additionalInfo = if (isDarkTheme) 
                                    "Tema oscuro activo, cambiar a tema claro" 
                                else 
                                    "Tema claro activo, cambiar a tema oscuro"
                            )
                        }
                    ) {
                        Text(
                            text = if (isDarkTheme) "☀️" else "🌙",
                            fontSize = 24.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
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
                // Logo de la aplicación (emoji de comida chilena)
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .semantics {
                            contentDescription = "Logo de Recetas Chilenas, plato de comida tradicional"
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🍲",
                        fontSize = 120.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Título de bienvenida
                Text(
                    text = "Bienvenido/a",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF4A4A4A),
                    modifier = Modifier.semantics {
                        contentDescription = "Bienvenido a Recetas Chilenas"
                    }
                )
                
                Text(
                    text = "Inicia sesión para ver tus recetas favoritas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDarkTheme) Color.LightGray else Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 32.dp)
                        .semantics {
                            contentDescription = "Inicia sesión para acceder a tus recetas favoritas"
                        }
                )
                
                // Card contenedor del formulario
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "Formulario de inicio de sesión con email"
                        },
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Inicia con Email",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) Color.LightGray else Color.Gray,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        
                        // Campo de correo electrónico con validación al perder foco
                        TextField(
                            value = email,
                            onValueChange = { 
                                email = it
                                emailError = null
                                showGeneralError = false
                            },
                            placeholder = { 
                                Text(
                                    "Correo Electrónico",
                                    color = if (isDarkTheme) Color.Gray else Color(0xFF8D6E63)
                                ) 
                            },
                            leadingIcon = {
                                Text(
                                    text = "✉️",
                                    fontSize = 24.sp
                                )
                            },
                            singleLine = true,
                            isError = emailError != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = if (emailError != null) 4.dp else 16.dp)
                                .onFocusChanged { focusState ->
                                    emailHasFocus = focusState.isFocused
                                    if (!focusState.isFocused) {
                                        validateEmailOnFocusLost()
                                    }
                                }
                                .semantics {
                                    contentDescription = textFieldAccessibilityDescription(
                                        label = "Correo electrónico",
                                        value = email,
                                        error = emailError,
                                        isRequired = true
                                    )
                                },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = fieldColor,
                                unfocusedContainerColor = fieldColor,
                                disabledContainerColor = fieldColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            )
                        )
                        
                        // Mensaje de error de email
                        if (emailError != null) {
                            Text(
                                text = emailError!!,
                                color = primaryRed,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, bottom = 12.dp)
                            )
                        }
                        
                        // Campo de contraseña con validación al perder foco
                        TextField(
                            value = password,
                            onValueChange = { 
                                password = it
                                passwordError = null
                                showGeneralError = false
                            },
                            placeholder = { 
                                Text(
                                    "Contraseña",
                                    color = if (isDarkTheme) Color.Gray else Color(0xFF8D6E63)
                                ) 
                            },
                            leadingIcon = {
                                Text(
                                    text = "🔒",
                                    fontSize = 24.sp
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { 
                                        hapticFeedback(context)
                                        passwordVisible = !passwordVisible 
                                    },
                                    modifier = Modifier.semantics {
                                        contentDescription = if (passwordVisible)
                                            "Ocultar contraseña, actualmente visible"
                                        else
                                            "Mostrar contraseña, actualmente oculta"
                                    }
                                ) {
                                    Text(
                                        text = if (passwordVisible) "👁️" else "👁️‍🗨️",
                                        fontSize = 20.sp
                                    )
                                }
                            },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) 
                                VisualTransformation.None 
                            else 
                                PasswordVisualTransformation(),
                            isError = passwordError != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = if (passwordError != null) 4.dp else 24.dp)
                                .onFocusChanged { focusState ->
                                    passwordHasFocus = focusState.isFocused
                                    if (!focusState.isFocused) {
                                        validatePasswordOnFocusLost()
                                    }
                                }
                                .semantics {
                                    contentDescription = textFieldAccessibilityDescription(
                                        label = "Contraseña",
                                        value = if (passwordVisible) password else "oculta",
                                        error = passwordError,
                                        isRequired = true
                                    )
                                },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = fieldColor,
                                unfocusedContainerColor = fieldColor,
                                disabledContainerColor = fieldColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { 
                                    focusManager.clearFocus()
                                    if (validateForm()) {
                                        hapticFeedback(context)
                                        if (email == "admin@recetas.cl" && password == "123456") {
                                            hapticSuccess(context)
                                            announceForAccessibility(context, "Inicio de sesión exitoso")
                                            onLoginSuccess()
                                        } else {
                                            hapticError(context)
                                            showGeneralError = true
                                            announceForAccessibility(context, "Error: Credenciales incorrectas")
                                        }
                                    } else {
                                        hapticError(context)
                                    }
                                }
                            )
                        )
                        
                        // Mensaje de error de contraseña
                        if (passwordError != null) {
                            Text(
                                text = passwordError!!,
                                color = primaryRed,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, bottom = 20.dp)
                            )
                        }
                        
                        // Mensaje de error general
                        if (showGeneralError) {
                            Text(
                                text = "❌ Credenciales incorrectas",
                                color = primaryRed,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .semantics {
                                        contentDescription = "Error: Credenciales incorrectas, verifica tu correo y contraseña"
                                    }
                            )
                        }
                        
                        // Botón de inicio de sesión
                        Button(
                            onClick = {
                                hapticFeedback(context)
                                focusManager.clearFocus()
                                
                                if (validateForm()) {
                                    if (email == "admin@recetas.cl" && password == "123456") {
                                        hapticSuccess(context)
                                        announceForAccessibility(context, "Inicio de sesión exitoso")
                                        onLoginSuccess()
                                    } else {
                                        hapticError(context)
                                        showGeneralError = true
                                        announceForAccessibility(context, "Error: Credenciales incorrectas")
                                    }
                                } else {
                                    hapticError(context)
                                    announceForAccessibility(context, "Error: Revisa los campos del formulario")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .semantics {
                                    contentDescription = buttonAccessibilityDescription(
                                        label = "Iniciar Sesión",
                                        isEnabled = email.isNotBlank() && password.isNotBlank(),
                                        additionalInfo = "Toca para iniciar sesión"
                                    )
                                },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Iniciar Sesión",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Divider con texto
                        Text(
                            text = "o entra vía Redes Sociales",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDarkTheme) Color.Gray else Color(0xFF8D6E63),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        
                        // Botones de redes sociales (iconos circulares)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Facebook
                            IconButton(
                                onClick = { 
                                    hapticSuccess(context)
                                    announceForAccessibility(context, "Iniciando sesión con Facebook")
                                    onLoginSuccess()
                                },
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1877F2))
                                    .semantics {
                                        contentDescription = buttonAccessibilityDescription(
                                            label = "Continuar con Facebook",
                                            additionalInfo = "Inicia sesión usando tu cuenta de Facebook"
                                        )
                                    }
                            ) {
                                Text(
                                    text = "f",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            
                            // X (Twitter)
                            IconButton(
                                onClick = { 
                                    hapticSuccess(context)
                                    announceForAccessibility(context, "Iniciando sesión con X")
                                    onLoginSuccess()
                                },
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black)
                                    .semantics {
                                        contentDescription = buttonAccessibilityDescription(
                                            label = "Continuar con X",
                                            additionalInfo = "Inicia sesión usando tu cuenta de X"
                                        )
                                    }
                            ) {
                                Text(
                                    text = "𝕏",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            
                            // Instagram con logo tipo cámara
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(
                                        androidx.compose.ui.graphics.Brush.linearGradient(
                                            listOf(
                                                Color(0xFFF58529),
                                                Color(0xFFDD2A7B),
                                                Color(0xFF8134AF)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = { 
                                        hapticSuccess(context)
                                        announceForAccessibility(context, "Iniciando sesión con Instagram")
                                        onLoginSuccess()
                                    },
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .semantics {
                                            contentDescription = buttonAccessibilityDescription(
                                                label = "Continuar con Instagram",
                                                additionalInfo = "Inicia sesión usando tu cuenta de Instagram"
                                            )
                                        }
                                ) {
                                    // Logo de Instagram - cámara con Canvas
                                    Canvas(
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        val canvasSize = size
                                        val strokeWidth = 2.5.dp.toPx()
                                        val cornerRadius = 6.dp.toPx()
                                        
                                        // Cuadrado exterior con esquinas redondeadas
                                        drawRoundRect(
                                            color = Color.White,
                                            size = canvasSize,
                                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                                        )
                                        
                                        // Círculo interior (lente)
                                        drawCircle(
                                            color = Color.White,
                                            radius = canvasSize.width * 0.28f,
                                            center = center,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                                        )
                                        
                                        // Punto arriba derecha (visor)
                                        drawCircle(
                                            color = Color.White,
                                            radius = 2.5.dp.toPx(),
                                            center = androidx.compose.ui.geometry.Offset(
                                                x = canvasSize.width * 0.75f,
                                                y = canvasSize.height * 0.25f
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Texto informativo con TTS
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Credenciales de prueba:\nadmin@recetas.cl / 123456",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDarkTheme) Color.Gray else Color(0xFF8D6E63),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.semantics {
                            contentDescription = "Para probar: correo admin arroba recetas punto cl, contraseña 1 2 3 4 5 6"
                        }
                    )
                    
                    // Botón TTS para leer credenciales
                    IconButton(
                        onClick = {
                            hablarTexto(context, "Credenciales de prueba: correo: admin arroba recetas punto cl. Contraseña: uno dos tres cuatro cinco seis")
                        }
                    ) {
                        Text(
                            text = "🔊",
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}
