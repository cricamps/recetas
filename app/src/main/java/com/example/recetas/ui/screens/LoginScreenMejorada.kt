package com.example.recetas.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import com.example.recetas.ui.components.InstagramLogo
import com.example.recetas.accessibility.*
import androidx.compose.runtime.MutableState
import com.example.recetas.data.UsuariosRepository
import com.example.recetas.services.EmailService
import kotlinx.coroutines.launch

/**
 * Pantalla de Login mejorada con opción de registro
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenMejorada(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    fontScale: MutableState<FontScale>,
    onFontScaleChange: (FontScale) -> Unit,
    contrastMode: MutableState<ContrastMode>,
    onContrastModeChange: (ContrastMode) -> Unit,
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit = {}
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    

    var isLoginMode by remember { mutableStateOf(true) }
    

    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    

    var registerName by remember { mutableStateOf("") }
    var registerEmail by remember { mutableStateOf("") }
    var registerPassword by remember { mutableStateOf("") }
    var registerConfirmPassword by remember { mutableStateOf("") }
    
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    

    val backgroundColor = if (isDarkTheme) Color(0xFF1A1A1A) else Color(0xFFFFF8E1)
    val cardColor = if (isDarkTheme) Color(0xFF2A2A2A) else Color(0xFFFFFBF0)
    val primaryRed = Color(0xFFD32F2F)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    ContrastModeControl(
                        contrastMode = contrastMode.value,
                        onContrastModeChange = onContrastModeChange,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    FontSizeButton(
                        currentScale = fontScale,
                        onScaleChange = onFontScaleChange
                    )
                    IconButton(onClick = onThemeChange) {
                        Text(
                            text = if (isDarkTheme) "☀️" else "🌙",
                            fontSize = 24.sp
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
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🍲", fontSize = 80.sp)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Recetas Chilenas",
                    fontSize = 32.scaledSp(),
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF4A4A4A)
                )
                
                Text(
                    text = "Sabores tradicionales de Chile",
                    fontSize = 16.scaledSp(),
                    color = if (isDarkTheme) Color.LightGray else Color.Gray,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(cardColor),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TabButton(
                        text = "Iniciar Sesión",
                        isSelected = isLoginMode,
                        onClick = { 
                            isLoginMode = true
                            showError = false
                            showSuccess = false
                        },
                        modifier = Modifier.weight(1f),
                        primaryColor = primaryRed
                    )
                    TabButton(
                        text = "Registrarse",
                        isSelected = !isLoginMode,
                        onClick = { 
                            isLoginMode = false
                            showError = false
                            showSuccess = false
                        },
                        modifier = Modifier.weight(1f),
                        primaryColor = primaryRed
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                

                AnimatedContent(
                    targetState = isLoginMode,
                    transitionSpec = {
                        slideInHorizontally(
                            initialOffsetX = { if (targetState) -it else it },
                            animationSpec = tween(500)
                        ) + fadeIn(tween(500)) togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { if (targetState) it else -it },
                            animationSpec = tween(500)
                        ) + fadeOut(tween(500))
                    },
                    label = "login_register_transition"
                ) { loginMode ->
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
                            if (loginMode) {
                                LoginForm(
                                    email = loginEmail,
                                    onEmailChange = { 
                                        loginEmail = it
                                        showError = false
                                        showSuccess = false
                                    },
                                    password = loginPassword,
                                    onPasswordChange = { 
                                        loginPassword = it
                                        showError = false
                                        showSuccess = false
                                    },
                                    passwordVisible = passwordVisible,
                                    onPasswordVisibilityChange = { passwordVisible = it },
                                    isDarkTheme = isDarkTheme,
                                    onLogin = {
                                        if (loginEmail.isBlank() || loginPassword.isBlank()) {
                                            showError = true
                                            errorMessage = "Completa todos los campos"
                                        } else if (UsuariosRepository.autenticarUsuario(loginEmail, loginPassword)) {
                                            val usuario = UsuariosRepository.buscarPorEmail(loginEmail)
                                            hapticSuccess(context)
                                            announceForAccessibility(context, "Bienvenido ${usuario?.nombre ?: ""}")
                                            onLoginSuccess()
                                        } else {
                                            showError = true
                                            errorMessage = "Credenciales incorrectas"
                                            hapticError(context)
                                        }
                                    },
                                    onForgotPassword = onForgotPassword,
                                    focusManager = focusManager
                                )
                            } else {
                                RegisterForm(
                                    name = registerName,
                                    onNameChange = { registerName = it; showError = false },
                                    email = registerEmail,
                                    onEmailChange = { registerEmail = it; showError = false },
                                    password = registerPassword,
                                    onPasswordChange = { registerPassword = it; showError = false },
                                    confirmPassword = registerConfirmPassword,
                                    onConfirmPasswordChange = { registerConfirmPassword = it; showError = false },
                                    passwordVisible = passwordVisible,
                                    onPasswordVisibilityChange = { passwordVisible = it },
                                    isDarkTheme = isDarkTheme,
                                    onRegister = {
                                        when {
                                            registerName.isBlank() || registerEmail.isBlank() || 
                                            registerPassword.isBlank() || registerConfirmPassword.isBlank() -> {
                                                showError = true
                                                errorMessage = "Completa todos los campos"
                                            }
                                            registerPassword != registerConfirmPassword -> {
                                                showError = true
                                                errorMessage = "Las contraseñas no coinciden"
                                            }
                                            registerPassword.length < 6 -> {
                                                showError = true
                                                errorMessage = "La contraseña debe tener al menos 6 caracteres"
                                            }
                                            else -> {
                                                val nuevoUsuario = UsuariosRepository.registrarUsuario(
                                                    nombre = registerName,
                                                    email = registerEmail,
                                                    password = registerPassword
                                                )
                                                
                                                if (nuevoUsuario != null) {
                                                    hapticSuccess(context)
                                                    announceForAccessibility(context, "Cuenta creada exitosamente. Por favor inicia sesión")
                                                    
                                                    scope.launch {
                                                        val emailEnviado = EmailService.enviarEmailBienvenida(
                                                            nombre = nuevoUsuario.nombre,
                                                            email = nuevoUsuario.email
                                                        )
                                                        
                                                        if (emailEnviado) {
                                                            announceForAccessibility(context, "Email de bienvenida enviado")
                                                        }
                                                    }
                                                    
                                                    showError = false
                                                    showSuccess = true
                                                    successMessage = "¡Cuenta creada! Revisa tu email 📧"
                                                    
                                                    isLoginMode = true
                                                    loginEmail = registerEmail
                                                    loginPassword = ""
                                                    
                                                    registerName = ""
                                                    registerEmail = ""
                                                    registerPassword = ""
                                                    registerConfirmPassword = ""
                                                } else {
                                                    showError = true
                                                    errorMessage = "El email ya está registrado"
                                                    hapticError(context)
                                                }
                                            }
                                        }
                                    },
                                    focusManager = focusManager
                                )
                            }
                            

                            if (showSuccess) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "✅",
                                            fontSize = 24.sp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = successMessage,
                                            color = Color(0xFF2E7D32),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            

                            if (showError) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "❌ $errorMessage",
                                    color = primaryRed,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            

                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "o entra vía Redes Sociales",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isDarkTheme) Color.Gray else Color(0xFF8D6E63)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                SocialButton(
                                    icon = "f",
                                    backgroundColor = Color(0xFF1877F2),
                                    contentDescription = "Continuar con Facebook",
                                    onClick = {
                                        hapticSuccess(context)
                                        announceForAccessibility(context, "Iniciando sesión con Facebook")
                                        onLoginSuccess()
                                    }
                                )
                                
                                InstagramLogo(
                                    onClick = {
                                        hapticSuccess(context)
                                        announceForAccessibility(context, "Iniciando sesión con Instagram")
                                        onLoginSuccess()
                                    }
                                )
                                
                                SocialButton(
                                    icon = "G",
                                    backgroundColor = Color.White,
                                    textColor = Color(0xFF4285F4),
                                    contentDescription = "Continuar con Google",
                                    onClick = {
                                        hapticSuccess(context)
                                        announceForAccessibility(context, "Iniciando sesión con Google")
                                        onLoginSuccess()
                                    },
                                    hasBorder = true,
                                    borderColor = Color(0xFFDDDDDD),
                                    fontSize = 34.sp
                                )
                                
                                SocialButton(
                                    icon = "𝕏",
                                    backgroundColor = Color.Black,
                                    contentDescription = "Continuar con X",
                                    onClick = {
                                        hapticSuccess(context)
                                        announceForAccessibility(context, "Iniciando sesión con X")
                                        onLoginSuccess()
                                    }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                

                if (isLoginMode) {
                    Text(
                        text = "Prueba con:\nadmin@recetas.cl / 123456",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDarkTheme) Color.Gray else Color(0xFF8D6E63),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryColor: Color
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) primaryColor else Color.Transparent,
            contentColor = if (isSelected) Color.White else primaryColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun LoginForm(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    isDarkTheme: Boolean,
    onLogin: () -> Unit,
    onForgotPassword: () -> Unit = {},
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    val fieldColor = if (isDarkTheme) Color(0xFF3A3A3A) else Color(0xFFFFE4B5)
    val context = androidx.compose.ui.platform.LocalContext.current
    
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Correo Electrónico") },
        leadingIcon = { Text("✉️", fontSize = 20.sp) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = fieldColor,
            unfocusedContainerColor = fieldColor
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
    
    Spacer(modifier = Modifier.height(16.dp))
    
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Contraseña") },
        leadingIcon = { Text("🔒", fontSize = 20.sp) },
        trailingIcon = {
            IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
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
            focusedContainerColor = fieldColor,
            unfocusedContainerColor = fieldColor
        ),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { 
                focusManager.clearFocus()
                onLogin()
            }
        )
    )
    
    Spacer(modifier = Modifier.height(24.dp))
    
    Button(
        onClick = onLogin,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD32F2F)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Iniciar Sesión",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(
            onClick = {
                hapticFeedback(context)
                announceForAccessibility(context, "Iniciando proceso de recuperación de contraseña")
                onForgotPassword()
            }
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFD32F2F),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Botón circular para login con redes sociales
 */
@Composable
fun SocialButton(
    icon: String,
    backgroundColor: Color,
    contentDescription: String,
    onClick: () -> Unit,
    textColor: Color = Color.White,
    hasBorder: Boolean = false,
    borderColor: Color = Color.LightGray,
    fontSize: androidx.compose.ui.unit.TextUnit = 28.sp
) {
    val context = LocalContext.current
    
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .then(
                if (hasBorder) {
                    Modifier.then(
                        Modifier
                            .padding(0.dp)
                            .then(
                                Modifier.background(
                                    androidx.compose.ui.graphics.Brush.linearGradient(
                                        colors = listOf(borderColor, borderColor)
                                    ),
                                    CircleShape
                                )
                            )
                            .padding(2.dp)
                            .background(backgroundColor, CircleShape)
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = {
                hapticFeedback(context)
                onClick()
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = icon,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun RegisterForm(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    isDarkTheme: Boolean,
    onRegister: () -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    val fieldColor = if (isDarkTheme) Color(0xFF3A3A3A) else Color(0xFFFFE4B5)
    
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Nombre Completo") },
        leadingIcon = { Text("👤", fontSize = 20.sp) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = fieldColor,
            unfocusedContainerColor = fieldColor
        ),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )
    )
    
    Spacer(modifier = Modifier.height(12.dp))
    
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Correo Electrónico") },
        leadingIcon = { Text("✉️", fontSize = 20.sp) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = fieldColor,
            unfocusedContainerColor = fieldColor
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
    
    Spacer(modifier = Modifier.height(12.dp))
    
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Contraseña") },
        leadingIcon = { Text("🔒", fontSize = 20.sp) },
        trailingIcon = {
            IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
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
            focusedContainerColor = fieldColor,
            unfocusedContainerColor = fieldColor
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
    
    Spacer(modifier = Modifier.height(12.dp))
    
    OutlinedTextField(
        value = confirmPassword,
        onValueChange = onConfirmPasswordChange,
        label = { Text("Confirmar Contraseña") },
        leadingIcon = { Text("🔒", fontSize = 20.sp) },
        visualTransformation = if (passwordVisible) 
            VisualTransformation.None 
        else 
            PasswordVisualTransformation(),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = fieldColor,
            unfocusedContainerColor = fieldColor
        ),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { 
                focusManager.clearFocus()
                onRegister()
            }
        )
    )
    
    Spacer(modifier = Modifier.height(24.dp))
    
    Button(
        onClick = onRegister,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD32F2F)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Crear Cuenta",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
