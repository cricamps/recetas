package com.example.recetas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.clickable
import com.example.recetas.accessibility.*
import com.example.recetas.data.UsuariosRepository

/**
 * Pantalla de Verificación de Código - Paso 2
 * 
 * El usuario ingresa el código de 6 dígitos recibido por email.
 * Incluye:
 * - 6 cajas para ingresar cada dígito
 * - Muestra el código en modo desarrollo
 * - Opción para reenviar código
 * - Validación automática
 * 
 * @param email Email del usuario
 * @param codigoEnviado Código generado (solo para mostrar en modo debug)
 * @param isDarkTheme Estado del tema
 * @param fontScale Escala de fuente
 * @param onBackToRecover Volver a solicitar email
 * @param onCodeVerified Código verificado correctamente
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificarCodigoScreen(
    email: String,
    codigoEnviado: String,
    isDarkTheme: Boolean,
    fontScale: MutableState<FontScale>,
    onBackToRecover: () -> Unit,
    onCodeVerified: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    
    var codigo by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isVerifying by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300)
        focusRequester.requestFocus()
    }
    
    val backgroundColor = if (isDarkTheme) Color(0xFF1A1A1A) else Color(0xFFFFF8E1)
    val cardColor = if (isDarkTheme) Color(0xFF2A2A2A) else Color(0xFFFFFBF0)
    val primaryRed = Color(0xFFD32F2F)
    
    LaunchedEffect(codigo) {
        if (codigo.length == 6) {
            isVerifying = true
            kotlinx.coroutines.delay(300)
            
            if (UsuariosRepository.verificarCodigoRecuperacion(email, codigo)) {
                hapticSuccess(context)
                announceForAccessibility(context, "Código verificado correctamente")
                onCodeVerified()
            } else {
                showError = true
                errorMessage = "Código incorrecto"
                hapticError(context)
                codigo = ""
            }
            
            isVerifying = false
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Verificar Código",
                        fontSize = 20.scaledSp()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        hapticFeedback(context)
                        onBackToRecover()
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver"
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
                        .background(Color(0xFF4CAF50).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📧",
                        fontSize = 80.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Verifica tu código",
                    fontSize = 28.scaledSp(),
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF4A4A4A),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Ingresa el código de 6 dígitos enviado a:",
                    fontSize = 16.scaledSp(),
                    color = if (isDarkTheme) Color.LightGray else Color.Gray,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = email,
                    fontSize = 16.scaledSp(),
                    fontWeight = FontWeight.Bold,
                    color = primaryRed,
                    textAlign = TextAlign.Center
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    focusRequester.requestFocus()
                                    hapticFeedback(context)
                                },
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            repeat(6) { index ->
                                CodeDigitBox(
                                    digit = codigo.getOrNull(index)?.toString() ?: "",
                                    isActive = codigo.length == index,
                                    isDarkTheme = isDarkTheme
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        BasicTextField(
                            value = codigo,
                            onValueChange = { newValue ->
                                if (newValue.length <= 6 && newValue.all { it.isDigit() }) {
                                    codigo = newValue
                                    showError = false
                                    hapticFeedback(context)
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .focusRequester(focusRequester),
                            decorationBox = { innerTextField ->
                                Box(modifier = Modifier.height(1.dp)) {
                                    innerTextField()
                                }
                            }
                        )
                        
                        if (showError) {
                            Text(
                                text = "❌ $errorMessage",
                                color = primaryRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        if (isVerifying) {
                            CircularProgressIndicator(
                                color = primaryRed,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Verificando...",
                                color = if (isDarkTheme) Color.LightGray else Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        TextButton(
                            onClick = {
                                hapticFeedback(context)
                                val nuevoCodigo = UsuariosRepository.solicitarRecuperacionPassword(email)
                                announceForAccessibility(context, "Código reenviado")
                            }
                        ) {
                            Text(
                                text = "🔄 Reenviar código",
                                fontSize = 16.sp,
                                color = primaryRed,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🔧 MODO DESARROLLO",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tu código es:",
                            color = Color(0xFF2E7D32),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = codigoEnviado,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20),
                            fontSize = 32.sp,
                            letterSpacing = 8.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "En producción, esto se enviaría por email",
                            color = Color(0xFF2E7D32),
                            fontSize = 10.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}

/**
 * Caja individual para un dígito del código
 */
@Composable
fun CodeDigitBox(
    digit: String,
    isActive: Boolean,
    isDarkTheme: Boolean
) {
    val boxColor = if (isDarkTheme) Color(0xFF3A3A3A) else Color(0xFFFFE4B5)
    val borderColor = if (isActive) Color(0xFFD32F2F) else Color.LightGray
    
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(boxColor)
            .border(
                width = if (isActive) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkTheme) Color.White else Color(0xFF4A4A4A)
        )
    }
}
